/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geoserver.wps.oskari.oskari;

import com.vividsolutions.jts.densify.Densifier;
import com.vividsolutions.jts.geom.*;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.util.*;
import java.util.logging.Logger;

/**
 * A process providing the intersection between two feature collections
 *
 * @author Gianni Barrotta - Sinergis
 * @author Andrea Di Nora - Sinergis
 * @author Pietro Arena - Sinergis
 * @author Luca Paolino - GeoSolutions
 */
@DescribeProcess(title = "intersection2", description = "test -- Returns the intersections between two feature "
        + "collections adding the attributes from both of them")
public class IntersectionFeatureCollection2 implements GSProcess {
    private static final Logger logger = Logger
            .getLogger("org.geoserver.wps.oskari.oskari.IntersectionFeatureCollection2");

    // Skip gml attributes
    private static final List<String> SKIP_GML_FIELDS = Arrays.asList(
             "metaDataProperty", "description", "boundedBy", "name");

    public static enum IntersectionMode {
        INTERSECTION, FIRST, SECOND, SECOND_CONTAINS, SECOND_CLIP
    }

    static final String ECKERT_IV_WKT = "PROJCS[\"World_Eckert_IV\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Eckert_IV\"],PARAMETER[\"Central_Meridian\",0.0],UNIT[\"Meter\",1.0]]";

    @DescribeResult(description = "test -- feature collection containg the intersections between the two feature "
            + "collections and the attributes from both of them")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "first feature collection", description = "First feature collection") SimpleFeatureCollection firstFeatures,
            @DescribeParameter(name = "second feature collection", description = "Second feature collection") SimpleFeatureCollection secondFeatures,
            @DescribeParameter(name = "first attributes to retain", collectionType = String.class, min = 0, description = "List of the first feature collection attributes to output") List<String> firstAttributes,
            @DescribeParameter(name = "second attributes to retain", collectionType = String.class, min = 0, description = "List of the second feature collection attributes to output") List<String> sndAttributes,
            @DescribeParameter(name = "intersectionMode", min = 0, description = "The operations to perform: set INTERSECTION if the geometry is the intersection, FIRST if the geometry is extracted by firstFeatures, SECOND if it is extracted by secondFeatures, SECOND_CONTAINS like SECOND but contains instead of intersect, SECOND_CLIP like SECOND but clip geometries (DEFAULT=INTERSECTION)") IntersectionMode intersectionMode,
            @DescribeParameter(name = "percentagesEnabled", min = 0, description = "Set it true to get the intersection percentage parameters, false  otherwise (DEFAULT=false)") Boolean percentagesEnabled,
            @DescribeParameter(name = "areasEnabled", min = 0, description = "Set it true to get the area attributes , false  otherwise (DEFAULT=false)") Boolean areasEnabled) {
        // assign defaults
        logger.fine("INTERSECTION FEATURE COLLECTION WPS STARTED");

        if (percentagesEnabled == null) {
            percentagesEnabled = false;
        }
        if (areasEnabled == null) {
            areasEnabled = false;
        }
        if (intersectionMode == null) {
            intersectionMode = IntersectionMode.INTERSECTION;
        }

        // basic geometry checks
        Class firstGeomType = firstFeatures.getSchema().getGeometryDescriptor().getType()
                .getBinding();
        Class secondGeomType = secondFeatures.getSchema().getGeometryDescriptor().getType()
                .getBinding();
        if ((percentagesEnabled || areasEnabled)
                && (!isGeometryTypeIn(firstGeomType, MultiPolygon.class, Polygon.class) || !isGeometryTypeIn(
                secondGeomType, MultiPolygon.class, Polygon.class))) {
            throw new IllegalArgumentException(
                    "In case of opMode or areaMode are true, the features in the first (" + firstGeomType.getName()
                            + ") and second (" + secondGeomType.getName() + ") collection must be polygonal");
        }
        if (!isGeometryTypeIn(firstGeomType, MultiPolygon.class, Polygon.class,
                MultiLineString.class, LineString.class) && !firstGeomType.isAssignableFrom(com.vividsolutions.jts.geom.Geometry.class)  ) {
            throw new IllegalArgumentException(
                    "First feature collection must be polygonal or linear. Was: " + firstGeomType.getName());
        }

        return new IntersectedFeatureCollection(firstFeatures, firstAttributes, secondFeatures,
                sndAttributes, intersectionMode, percentagesEnabled, areasEnabled);
    }

    /**
     * Checks if the specified class is same, or subclass, of any of the specified target classes
     *
     * @param test
     * @param targets
     * @return
     */
    static boolean isGeometryTypeIn(Class test, Class... targets) {
        for (Class target : targets) {
            if (target.isAssignableFrom(test)) {
                return true;
            }
        }
        return false;
    }

    static Geometry densify(Geometry geom, CoordinateReferenceSystem crs, double maxAreaError)
            throws FactoryException, TransformException {
        // basic checks
        if (maxAreaError <= 0) {
            throw new IllegalArgumentException("maxAreaError must be greater than 0");
        }
        if (!(geom instanceof Polygon) && !(geom instanceof MultiPolygon)) {
            throw new IllegalArgumentException("Geom must be poligonal");
        }
        if (crs == null) {
            throw new IllegalArgumentException("CRS cannot be set to null");
        }
        double previousArea = 0.0;
        CoordinateReferenceSystem targetCRS = CRS.parseWKT(ECKERT_IV_WKT);
        MathTransform firstTransform = CRS.findMathTransform(crs, targetCRS);
        GeometryFactory geomFactory = new GeometryFactory();
        int ngeom = geom.getNumGeometries();
        Geometry densifiedGeometry = geom;
        double areaError = 1.0d;
        int maxIterate = 0;
        do {
            double max = 0;
            maxIterate++;
            // check the maximum side length of the densifiedGeometry
            for (int j = 0; j < ngeom; j++) {
                Geometry geometry = densifiedGeometry.getGeometryN(j);
                Coordinate[] coordinates = geometry.getCoordinates();
                int n = coordinates.length;
                for (int i = 0; i < (n - 1); i++) {
                    Coordinate[] coords = new Coordinate[2];
                    coords[0] = coordinates[i];
                    coords[1] = coordinates[i + 1];
                    LineString lineString = geomFactory.createLineString(coords);
                    if (lineString.getLength() > max)
                        max = lineString.getLength();
                }
            }

            // calculate the denified geometry
            densifiedGeometry = Densifier.densify(densifiedGeometry, max / 2);

            // reproject densifiedGeometry to Eckert IV
            Geometry targetGeometry = JTS.transform(densifiedGeometry, firstTransform);
            double nextArea = targetGeometry.getArea();

            // evaluate the current error
            areaError = Math.abs(previousArea - nextArea) / nextArea;
            //      logger3.info("AREA ERROR"+areaError);
            previousArea = nextArea;
            // check whether the current error is greater than the maximum allowed
        } while (areaError > maxAreaError && maxIterate < 10);
        return densifiedGeometry;
    }

    static double getIntersectionArea(Geometry first, CoordinateReferenceSystem firstCRS,
                                      Geometry second, CoordinateReferenceSystem secondCRS, boolean divideFirst) {
        // basic checks
        if (firstCRS == null || secondCRS == null)
            throw new IllegalArgumentException("CRS cannot be set to null");
        if (!Polygon.class.isAssignableFrom(first.getClass())
                && !MultiPolygon.class.isAssignableFrom(first.getClass()))
            throw new IllegalArgumentException("first geometry must be poligonal");
        if (!Polygon.class.isAssignableFrom(second.getClass())
                && !MultiPolygon.class.isAssignableFrom(second.getClass()))
            throw new IllegalArgumentException("second geometry must be poligonal");
        try {
            Geometry firstTargetGeometry = reprojectAndDensify(first, firstCRS, null);
            Geometry secondTargetGeometry = reprojectAndDensify(second, firstCRS, null);
            double numeratorArea = (double) (firstTargetGeometry.intersection(secondTargetGeometry))
                    .getArea();
            if (divideFirst) {
                double denom = firstTargetGeometry.getArea();
                if (denom != 0) return numeratorArea / denom;
                return 0;
            }
            double denom = secondTargetGeometry.getArea();
            if (denom != 0) return numeratorArea / denom;
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

     static Geometry getIntersectionGeometry(Geometry first, CoordinateReferenceSystem firstCRS,
                                      Geometry second, CoordinateReferenceSystem secondCRS) {
        // basic checks
        if (firstCRS == null || secondCRS == null)
            throw new IllegalArgumentException("CRS cannot be set to null");
        if (!Polygon.class.isAssignableFrom(first.getClass())
                && !MultiPolygon.class.isAssignableFrom(first.getClass()))
            throw new IllegalArgumentException("first geometry must be poligonal");
        if (!Polygon.class.isAssignableFrom(second.getClass())
                && !MultiPolygon.class.isAssignableFrom(second.getClass()))
            throw new IllegalArgumentException("second geometry must be poligonal");
        try {
            Geometry firstTargetGeometry = reprojectAndDensify(first, firstCRS, null);
            Geometry secondTargetGeometry = reprojectAndDensify(second, firstCRS, null);
            Geometry clipGeometry = firstTargetGeometry.intersection(secondTargetGeometry);
           
            return clipGeometry;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static Geometry reprojectAndDensify(Geometry first, CoordinateReferenceSystem sourceCRS,
                                        CoordinateReferenceSystem targetCRS) throws FactoryException, TransformException {
        if (targetCRS == null) {
            targetCRS = CRS.parseWKT(ECKERT_IV_WKT);
        }
        MathTransform firstTransform = CRS.findMathTransform(sourceCRS, targetCRS);
        Geometry geometry = JTS.transform(densify(first, sourceCRS, 0.01d), firstTransform);
        return geometry;
    }

    static AttributeDescriptor getIntersectionType(SimpleFeatureCollection first,
                                                   SimpleFeatureCollection second) {
        Class firstGeomType = first.getSchema().getGeometryDescriptor().getType().getBinding();
        Class secondGeomType = second.getSchema().getGeometryDescriptor().getType().getBinding();

        // figure out the output geometry type
        Class binding;
        if (isGeometryTypeIn(secondGeomType, Point.class)) {
            binding = Point.class;
        } else if (isGeometryTypeIn(secondGeomType, MultiPoint.class)) {
            binding = MultiPoint.class;
        } else if (isGeometryTypeIn(secondGeomType, LineString.class, MultiLineString.class)) {
            binding = MultiLineString.class;
        } else if (isGeometryTypeIn(secondGeomType, Polygon.class, MultiPolygon.class)) {
            if (isGeometryTypeIn(firstGeomType, Polygon.class, MultiPolygon.class)) {
                binding = MultiPolygon.class;
            } else {
                binding = MultiLineString.class;
            }
        } else {
            // we can't be more precise than this
            binding = Geometry.class;
        }

        AttributeTypeBuilder builder = new AttributeTypeBuilder();
        builder.setName("the_geom");
        builder.setBinding(binding);
        builder.setCRS(first.features().next().getFeatureType().getCoordinateReferenceSystem());
        AttributeDescriptor descriptor = builder.buildDescriptor("the_geom");
        return descriptor;
    }

    /**
     * Delegate that will compute the intersections on the go
     */
    static class IntersectedFeatureCollection extends DecoratingSimpleFeatureCollection {

        @Override
        public SimpleFeatureType getSchema() {

            return fb.getFeatureType();
        }

        SimpleFeatureCollection features;

        List<String> firstAttributes = null;

        List<String> sndAttributes = null;

        IntersectionMode intersectionMode;

        boolean percentagesEnabled;

        boolean areasEnabled;
        // added
        SimpleFeatureBuilder fb;
        AttributeDescriptor geomType = null;

        public IntersectedFeatureCollection(SimpleFeatureCollection delegate,
                                            List<String> firstAttributes, SimpleFeatureCollection features,
                                            List<String> sndAttributes, IntersectionMode intersectionMode,
                                            boolean percentagesEnabled, boolean areasEnabled) {
            super(delegate);
            this.features = features;
            this.firstAttributes = firstAttributes;
            this.sndAttributes = sndAttributes;
            this.intersectionMode = intersectionMode;
            this.percentagesEnabled = percentagesEnabled;
            this.areasEnabled = areasEnabled;
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

            SimpleFeatureType firstFeatureCollectionSchema = delegate.getSchema();
            SimpleFeatureType secondFeatureCollectionSchema = features.getSchema();

            if (intersectionMode == IntersectionMode.FIRST) {
                geomType = firstFeatureCollectionSchema.getGeometryDescriptor();
            }
            if (intersectionMode == IntersectionMode.SECOND) {
                geomType = secondFeatureCollectionSchema.getGeometryDescriptor();
            }
            if (intersectionMode == IntersectionMode.SECOND_CONTAINS) {
                geomType = secondFeatureCollectionSchema.getGeometryDescriptor();
            }
            if (intersectionMode == IntersectionMode.SECOND_CLIP) {
                geomType = secondFeatureCollectionSchema.getGeometryDescriptor();
            }
            if (intersectionMode == IntersectionMode.INTERSECTION) {
                geomType = getIntersectionType(delegate, features);
            }
            tb.add(geomType);

            // gather the attributes from the first feature collection and skip
            collectAttributes(firstFeatureCollectionSchema, firstAttributes, tb);
            // gather the attributes from the second feature collection
            collectAttributes(secondFeatureCollectionSchema, sndAttributes, tb);
            // add the dyamic attributes as needed
            if (percentagesEnabled) {
                tb.add("percentageA", Double.class);
                tb.add("percentageB", Double.class);
            }
            if (areasEnabled) {
                tb.add("areaA", Double.class);
                tb.add("areaB", Double.class);
            }
            tb.add("INTERSECTION_ID", Integer.class);
            tb.setDescription(firstFeatureCollectionSchema.getDescription());
            tb.setCRS(firstFeatureCollectionSchema.getCoordinateReferenceSystem());
            tb.setAbstract(firstFeatureCollectionSchema.isAbstract());
            tb.setSuperType((SimpleFeatureType) firstFeatureCollectionSchema.getSuper());
            tb.setName(firstFeatureCollectionSchema.getName());

            this.fb = new SimpleFeatureBuilder(tb.buildFeatureType());


        }

        private void collectAttributes(SimpleFeatureType schema, List<String> retainedAttributes,
                                       SimpleFeatureTypeBuilder tb) {
            for (AttributeDescriptor descriptor : schema.getAttributeDescriptors()) {
                // check whether descriptor has been selected in the attribute list
                boolean isInRetainList = true;
                if (retainedAttributes != null) {

                    isInRetainList = retainedAttributes.contains(descriptor.getLocalName());
                    logger.fine("Checking " + descriptor.getLocalName() + " --> " + isInRetainList);
                }
                if (!isInRetainList || schema.getGeometryDescriptor() == descriptor) {
                    continue;
                }
                // Skip gml specific attributes - namespace is not available - don't use similiar attribute names to gml
                if( SKIP_GML_FIELDS.contains(descriptor.getLocalName())) {
                    continue;
                }

                // build the attribute to return
                AttributeTypeBuilder builder = new AttributeTypeBuilder();
                builder.setName(schema.getName().getLocalPart() + "_" + descriptor.getName());
                builder.setNillable(descriptor.isNillable());
                builder.setBinding(descriptor.getType().getBinding());
                builder.setMinOccurs(descriptor.getMinOccurs());
                builder.setMaxOccurs(descriptor.getMaxOccurs());
                builder.setDefaultValue(descriptor.getDefaultValue());
                builder.setCRS(schema.getCoordinateReferenceSystem());
                AttributeDescriptor intersectionDescriptor = builder.buildDescriptor(schema
                        .getName().getLocalPart() + "_" + descriptor.getName(),
                        descriptor.getType());
                tb.add(intersectionDescriptor);
                tb.addBinding(descriptor.getType());
            }
        }

        @Override
        public SimpleFeatureIterator features() {
            return new IntersectedFeatureIterator(delegate.features(), delegate, features,
                    delegate.getSchema(), features.getSchema(), firstAttributes, sndAttributes,
                    intersectionMode, percentagesEnabled, areasEnabled, fb);
        }
    }

    /**
     * Builds the intersections while streaming
     */
    static class IntersectedFeatureIterator implements SimpleFeatureIterator {
        SimpleFeatureIterator delegate;

        SimpleFeatureCollection firstFeatures;

        SimpleFeatureCollection secondFeatures;

        SimpleFeatureCollection subFeatureCollection;

        SimpleFeatureBuilder fb;

        SimpleFeature next;

        SimpleFeature first;

        Integer iterationIndex = 0;

        boolean complete = true;

        boolean added = false;

        SimpleFeatureCollection intersectedGeometries;

        SimpleFeatureIterator iterator;

        String dataGeomName;

        List<String> retainAttributesFst = null;

        List<String> retainAttributesSnd = null;

        AttributeDescriptor geomType = null;

        boolean percentagesEnabled;

        boolean areasEnabled;

        IntersectionMode intersectionMode;
        int id = 0;

        public IntersectedFeatureIterator(SimpleFeatureIterator delegate,
                                          SimpleFeatureCollection firstFeatures, SimpleFeatureCollection secondFeatures,
                                          SimpleFeatureType firstFeatureCollectionSchema,
                                          SimpleFeatureType secondFeatureCollectionSchema,
                                          List<String> retainAttributesFstPar, List<String> retainAttributesSndPar,
                                          IntersectionMode intersectionMode, boolean percentagesEnabled, boolean areasEnabled,
                                          SimpleFeatureBuilder sfb) {
            this.retainAttributesFst = retainAttributesFstPar;
            this.retainAttributesSnd = retainAttributesSndPar;
            this.delegate = delegate;
            this.firstFeatures = firstFeatures;
            this.secondFeatures = secondFeatures;
            this.percentagesEnabled = percentagesEnabled;
            this.areasEnabled = areasEnabled;
            this.intersectionMode = intersectionMode;

            logger.fine("Creating schema");
            // create the geometry attribute descriptor for the result
            //          SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            if (intersectionMode == IntersectionMode.FIRST) {
                geomType = firstFeatureCollectionSchema.getGeometryDescriptor();
            }
            if (intersectionMode == IntersectionMode.SECOND) {
                geomType = secondFeatureCollectionSchema.getGeometryDescriptor();
            }
            if (intersectionMode == IntersectionMode.SECOND_CONTAINS) {
                geomType = secondFeatureCollectionSchema.getGeometryDescriptor();
            }
             if (intersectionMode == IntersectionMode.SECOND_CLIP) {
                geomType = secondFeatureCollectionSchema.getGeometryDescriptor();
            }
            if (intersectionMode == IntersectionMode.INTERSECTION) {
                geomType = getIntersectionType(firstFeatures, secondFeatures);
            }

            this.fb = sfb;
            subFeatureCollection = this.secondFeatures;

            this.dataGeomName = this.firstFeatures.getSchema().getGeometryDescriptor()
                    .getLocalName();
            logger.fine("Schema created");
        }


        public void close() {
            delegate.close();
        }

        public boolean hasNext() {
            //   logger.info("qui");
            logger.finer("HAS NEXT");
            while ((next == null && delegate.hasNext()) || (next == null && added)) {
                //     logger.info("qui nel while");
                if (complete) {
                    first = delegate.next();
                    intersectedGeometries = null;
                }
                //               logger.info("qui dopo check if (complete)");
                //logger.finer("control HAS NEXT");
                for (Object attribute : first.getAttributes()) {
                    if (attribute instanceof Geometry
                            && attribute.equals(first.getDefaultGeometry())) {
                        Geometry currentGeom = (Geometry) attribute;

                        if (intersectedGeometries == null && !added) {

                            intersectedGeometries = filteredCollection(currentGeom,
                                    subFeatureCollection);

                            iterator = intersectedGeometries.features();
                        }
                        try {
                            while (iterator.hasNext()) {
                                added = false;
                                SimpleFeature second = iterator.next();
                                if (currentGeom.intersects(
                                        ((Geometry) second.getDefaultGeometry()))) {
                                    // compute geometry
                                    if (intersectionMode == IntersectionMode.INTERSECTION) {
                                        attribute = currentGeom.intersection((Geometry) second
                                                .getDefaultGeometry());

                                        GeometryFilterImpl filter = new GeometryFilterImpl(geomType
                                                .getType().getBinding());
                                        ((Geometry) attribute).apply(filter);
                                        attribute = filter.getGeometry();
                                    } else if (intersectionMode == IntersectionMode.FIRST) {
                                        attribute = currentGeom;
                                    } else if (intersectionMode == IntersectionMode.SECOND) {
                                        attribute = (Geometry) second.getDefaultGeometry();
                                    } else if (intersectionMode == IntersectionMode.SECOND_CONTAINS) {
                                        // Only contains
                                        attribute = null;
                                        if (currentGeom.contains(((Geometry) second.getDefaultGeometry())))
                                            attribute = (Geometry) second.getDefaultGeometry();
                                    } else if (intersectionMode == IntersectionMode.SECOND_CLIP) {
                                        attribute = (Geometry) currentGeom.intersection((Geometry) second.getDefaultGeometry());
                                    }
                                    if (attribute != null && ((Geometry) attribute).getNumGeometries() > 0) {
                                        fb.add(attribute);
                                        fb.set("INTERSECTION_ID", id++);
                                        // add the non geometric attributes
                                        addAttributeValues(first, retainAttributesFst, fb);
                                        addAttributeValues(second, retainAttributesSnd, fb);
                                        // add the dynamic attributes
                                        if (percentagesEnabled) {
                                            addPercentages(currentGeom, second);
                                        }
                                        if (areasEnabled) {
                                            addAreas(currentGeom, second);
                                        }

                                        // build the feature
                                        next = fb.buildFeature(iterationIndex.toString());

                                        // update iterator status
                                        if (iterator.hasNext()) {
                                            complete = false;
                                            added = true;
                                            iterationIndex++;
                                            return next != null;
                                        }
                                        iterationIndex++;
                                    }

                                }
                                complete = false;
                            }
                            complete = true;
                        } finally {
                            if (!added) {
                                iterator.close();
                            }
                        }
                    }
                }
            }
            return next != null;
        }


        private void addAttributeValues(SimpleFeature feature, List<String> retained,
                                        SimpleFeatureBuilder fb) {
            Iterator<AttributeDescriptor> firstIterator = feature.getType().getAttributeDescriptors()
                    .iterator();
            while (firstIterator.hasNext()) {
                AttributeDescriptor ad = firstIterator.next();
                Object firstAttribute = feature.getAttribute(ad.getLocalName());
                if ((retained == null || retained.contains(ad.getLocalName()))
                        && ad.toString().indexOf("Geometry") == -1 && !(firstAttribute instanceof Geometry)
                        && !SKIP_GML_FIELDS.contains(ad.getLocalName())) {
                    fb.add(feature.getAttribute(ad.getLocalName()));
                }
            }
        }

        private void addAreas(Geometry currentGeom, SimpleFeature second) {
            CoordinateReferenceSystem firstCRS = firstFeatures.getSchema().getCoordinateReferenceSystem();
            CoordinateReferenceSystem secondCRS = secondFeatures.getSchema().getCoordinateReferenceSystem();

            try {
                double areaA = IntersectionFeatureCollection2.reprojectAndDensify(currentGeom,
                        firstCRS, null).getArea();
                double areaB = IntersectionFeatureCollection2.reprojectAndDensify(
                        (Geometry) second.getDefaultGeometry(), secondCRS, null).getArea();
                fb.set("areaA", areaA);
                fb.set("areaB", areaB);
            } catch (Exception e) {
                System.out.println("" + e);
                fb.set("areaA", -1);
                fb.set("areaB", -1);
            }
        }

        private void addPercentages(Geometry currentGeom, SimpleFeature second) {
            CoordinateReferenceSystem firstCRS = firstFeatures.getSchema().getCoordinateReferenceSystem();

            CoordinateReferenceSystem secondCRS = secondFeatures.getSchema().getCoordinateReferenceSystem();


            double percentageA = IntersectionFeatureCollection2.getIntersectionArea(currentGeom,
                    firstCRS, (Geometry) second.getDefaultGeometry(), secondCRS, true);

            double percentageB = IntersectionFeatureCollection2.getIntersectionArea(currentGeom,
                    firstCRS, (Geometry) second.getDefaultGeometry(), secondCRS, false);

            fb.set("percentageA", percentageA);

            fb.set("percentageB", percentageB);

        }

        public SimpleFeature next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("hasNext() returned false!");
            }

            SimpleFeature result = next;
            next = null;
            return result;
        }

        private SimpleFeatureCollection filteredCollection(Geometry currentGeom,
                                                           SimpleFeatureCollection subFeatureCollection) {
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
            Filter intersectFilter = ff.intersects(ff.property(dataGeomName),
                    ff.literal(currentGeom));
            SimpleFeatureCollection subFeatureCollectionIntersection = this.subFeatureCollection
                    .subCollection(intersectFilter);
            if (subFeatureCollectionIntersection.size() == 0) {
                subFeatureCollectionIntersection = subFeatureCollection;
            }
            return subFeatureCollectionIntersection;
        }

    }

    static class GeometryFilterImpl implements GeometryFilter {
        GeometryFactory factory = new GeometryFactory();

        ArrayList<Geometry> collection = new ArrayList<Geometry>();

        Class binding = null;

        GeometryFilterImpl(Class binding) {
            this.binding = binding;
        }

        @Override
        public void filter(Geometry gmtr) {
            if (MultiPolygon.class.isAssignableFrom(binding)) {
                if (gmtr.getArea() != 0.0d && gmtr.getGeometryType().equals("Polygon")) {
                    collection.add(gmtr);
                }
            }
            if (MultiLineString.class.isAssignableFrom(binding)) {
                if (gmtr.getLength() != 0.0d && gmtr.getGeometryType().equals("LineString")) {
                    collection.add(gmtr);
                }
            }
            if (MultiPoint.class.isAssignableFrom(binding)) {
                if (gmtr.getNumGeometries() > 0 && gmtr.getGeometryType().equals("Point")) {
                    collection.add(gmtr);
                }
            }
            if (Point.class.isAssignableFrom(binding)) {
                if (gmtr.getGeometryType().equals("Point")) {
                    collection.add(gmtr);
                }
            }
        }

        public Geometry getGeometry() {
            int n = collection.size();
            if (MultiPolygon.class.isAssignableFrom(binding)) {
                Polygon[] array = new Polygon[n];
                for (int i = 0; i < n; i++)
                    array[i] = (Polygon) collection.get(i);
                return factory.createMultiPolygon(array);
            }
            if (MultiLineString.class.isAssignableFrom(binding)) {
                LineString[] array = new LineString[n];
                for (int i = 0; i < n; i++)
                    array[i] = (LineString) collection.get(i);
                return factory.createMultiLineString(array);
            }
            if (MultiPoint.class.isAssignableFrom(binding)) {
                Point[] array = new Point[n];
                for (int i = 0; i < n; i++)
                    array[i] = (Point) collection.get(i);
                return factory.createMultiPoint(array);
            }
            return null;

        }

    }
}
