/* (c) 2014 - 2016 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.ows.kvp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.platform.GeoServerExtensions;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.geotools.util.logging.Logging;

/**
 * Parses the {@code elevation} parameter of the request.
 * 
 * @author Ariel Nunez, GeoSolutions S.A.S.
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @version $Id$
 */
public class ElevationParser {
    
    static final Logger LOGGER = Logging.getLogger(ElevationParser.class);

    /**
     * Built-in limits
     */
    private final static int MAX_ELEMENTS_ELEVATIONS_KVP;

    private final static int DEFAULT_MAX_ELEMENTS_ELEVATIONS_KVP = 100;

    static {
        // initialization of the renderer choice flag
        String value = GeoServerExtensions.getProperty("MAX_ELEMENTS_ELEVATIONS_KVP");
        // default to true, but allow switching on
        if (value == null)
            MAX_ELEMENTS_ELEVATIONS_KVP = DEFAULT_MAX_ELEMENTS_ELEVATIONS_KVP;
        else {
            int iVal = -1;
            try {
                iVal = Integer.parseInt(value.trim());
            } catch (Exception e) {
                iVal = DEFAULT_MAX_ELEMENTS_ELEVATIONS_KVP;
            }
            MAX_ELEMENTS_ELEVATIONS_KVP = iVal;
        }
    }

    /**
     * Parses the elevation given in parameter. The string may contains either a single double, or a
     * start value, end value and a period. In the first case, this method returns a singleton
     * containing only the parsed value. In the second case, this method returns a list including
     * all elevations from start value up to the end value with the interval specified in the
     * {@code value} string.
     * 
     * @param value
     *            The elevation item or items to parse.
     * @return A list of doubles, or an empty list of the {@code value} string is null or empty.
     * @throws ParseException
     *             if the string can not be parsed.
     */
    @SuppressWarnings( { "unchecked", "rawtypes" })
    public Collection parse(String value) throws ParseException {
        if (value == null) {
            return Collections.emptyList();
        }
        value = value.trim();
        if (value.length() == 0) {
            return Collections.emptyList();
        }
        final Set values = new TreeSet(new Comparator() {

            public int compare(Object o1, Object o2) {
                final boolean o1Double = o1 instanceof Double;
                final boolean o2Double = o2 instanceof Double;

                // o1 date
                if (o1Double) {
                    final Double left = (Double) o1;
                    if (o2Double) {
                        // o2 date
                        return left.compareTo((Double) o2);
                    }
                    // o2 number range
                    return left.compareTo(((NumberRange<Double>) o2).getMinValue());
                }

                // o1 number range
                final NumberRange left = (NumberRange) o1;
                if (o2Double) {
                    // o2 date
                    return left.getMinValue().compareTo(((Double) o2));
                }
                // o2 daterange
                return left.getMinValue().compareTo(((NumberRange) o2).getMinValue());
            }
        });
        final String[] listValues = value.split(",");
        for (String d : listValues) {
            if (d.indexOf("/") <= 0) {
                addValue(values, Double.valueOf(d.trim()));
            } else {
                // period

                String[] period = d.split("/");
                // Only one value given.
                if (period.length == 2) {
                    // Period continuous
                    final Double begin = Double.valueOf(period[0]);
                    final Double end = Double.valueOf(period[1]);
                    addPeriod(values, NumberRange.create(begin, end));
                } else if (period.length == 3) {
                    // Period discrete
                    final Double begin = Double.valueOf(period[0]);
                    final Double end = Double.valueOf(period[1]);
                    final Double increment = Double.valueOf(period[2]);

                    Double step;
                    int j = 0;
                    while ((step = j * increment + begin) <= end) {
                        addValue(values, step);
                        j++;

                        // limiting discrete period elements
                        if (j >= MAX_ELEMENTS_ELEVATIONS_KVP) {
                            if (LOGGER.isLoggable(Level.INFO))
                                LOGGER.info("Lmiting number of elements in this periodo to "
                                        + MAX_ELEMENTS_ELEVATIONS_KVP);
                            break;

                        }
                    }
                } else {
                    throw new ParseException("Invalid elevation parameter: " + period, 0);
                }
            }
        }

        return new ArrayList(values);
    }

    private void addValue(Collection result, Double step) {
        for (Iterator it = result.iterator(); it.hasNext();) {
            final Object element = it.next();
            if (element instanceof Double) {
                // convert
                final Double local = (Double) element;
                if (local.equals(step))
                    return;
            } else {
                // convert
                final DateRange local = (DateRange) element;
                if (local.contains(step))
                    return;
            }
        }
        result.add(step);

    }

    private void addPeriod(Collection result, NumberRange<Double> newRange) {
        for (Iterator it = result.iterator(); it.hasNext();) {
            final Object element = it.next();
            if (element instanceof Double) {
                // convert
                if (newRange.contains((Number) element)) {
                    it.remove();
                }
            } else {
                // convert
                final NumberRange<Double> local = (NumberRange<Double>) element;
                if (local.contains(newRange))
                    return;
                if (newRange.contains(local))
                    it.remove();
            }
        }
        result.add(newRange);

    }
    
}
