package org.grobid.core.engines;

import org.grobid.core.GrobidModels;
import org.grobid.core.data.Date;
import org.grobid.core.exceptions.GrobidException;
import org.grobid.core.features.FeaturesVectorDate;
import org.grobid.core.utilities.TextUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Patrice Lopez
 */
public class DateParser extends AbstractParser {

    public DateParser() {
        super(GrobidModels.DATE);
    }

    /**
     * Processing of authors in header
     */
    public List<Date> processing(String input) {
        if (input == null)
            return null;

        List<Date> dates = null;

        List<String> dateBlocks = new ArrayList<String>();
        try {
            //StringTokenizer st = new StringTokenizer(input, "([" + TextUtilities.punctuations, true);
			List<String> tokenizations = analyzer.tokenize(input);
            //if (st.countTokens() == 0)
			if (tokenizations.size() == 0) 
                return null;
            //while (st.hasMoreTokens()) {
            //    String tok = st.nextToken();
			for(String tok : tokenizations) {
                if (!tok.equals(" ")) {
                    dateBlocks.add(tok + " <date>");
                }
            }
//          dateBlocks.add("\n");

            String headerDate = FeaturesVectorDate.addFeaturesDate(dateBlocks);

            // add context
//            st = new StringTokenizer(headerDate, "\n");
            //TODO:
//            String res = getTaggerResult(st, "<date>");
            String res = label(headerDate);
            // extract results from the processed file

            //System.out.print(res.toString());
            StringTokenizer st2 = new StringTokenizer(res, "\n");
            String lastTag = null;
            org.grobid.core.data.Date date = new Date();
            int lineCount = 0;
            String currentMarker = null;
            while (st2.hasMoreTokens()) {
                String line = st2.nextToken();
                if ((line.trim().length() == 0)) {
                    if (date.isNotNull()) {
                        if (dates == null)
                            dates = new ArrayList<Date>();
                        normalize(date);
                        dates.add(date);
                    }
                    date = new Date();
                    continue;
                }
                StringTokenizer st3 = new StringTokenizer(line, "\t ");
                int ll = st3.countTokens();
                int i = 0;
                String s1 = null;
                String s2 = null;
                //String s3 = null;
                //List<String> localFeatures = new ArrayList<String>();
                while (st3.hasMoreTokens()) {
                    String s = st3.nextToken().trim();
                    if (i == 0) {
                        s2 = s; // string
                    } /*else if (i == ll - 2) {
                        s3 = s; // pre-label, in this case it should always be <date>
                    } */ 
					else if (i == ll - 1) {
                        s1 = s; // label
                    }
                    /*else {
                             localFeatures.add(s);
                         }*/
                    i++;
                }

                if (s1.equals("<year>") || s1.equals("I-<year>")) {
                    //if (s3.equals("<date>")) 
					{
                        if (date.getYearString() != null) {
                            if ((s1.equals("I-<year>")) ||
                                    (!s1.equals(lastTag) && !lastTag.equals("I-<year>"))
                                    ) {
                                // new date
                                if (date.isNotNull()) {
                                    if (dates == null)
                                        dates = new ArrayList<Date>();
                                    normalize(date);
                                    dates.add(date);
                                }

                                date = new Date();
                                date.setYearString(s2);
                            } else {
                                if (date.getYearString().length() == 0)
                                    date.setYearString(s2);
                                else if ((date.getYearString().charAt(date.getYearString().length() - 1) == '-')
                                        | (date.getYearString().charAt(date.getYearString().length() - 1) == '\''))
                                    date.setYearString(date.getYearString() + s2);
                                else
                                    date.setYearString(date.getYearString() + " " + s2);
                            }
                        } else {
                            date.setYearString(s2);
                        }
                    }
                } else if (s1.equals("<month>") || s1.equals("I-<month>")) {
                    //if (s3.equals("<date>")) 
					{
                        if (date.getMonthString() != null) {
                            if ((s1.equals("I-<month>")) ||
                                    (!s1.equals(lastTag) && !lastTag.equals("I-<month>"))
                                    ) {
                                // new date
                                if (date.isNotNull()) {
                                    if (dates == null)
                                        dates = new ArrayList<Date>();
                                    normalize(date);
                                    dates.add(date);
                                }

                                date = new Date();
                                date.setMonthString(s2);
                            } else {
                                if (date.getMonthString().length() == 0)
                                    date.setMonthString(s2);
                                else if ((date.getMonthString().charAt(date.getMonthString().length() - 1) == '-')
                                        | (date.getMonthString().charAt(date.getMonthString().length() - 1) == '\''))
                                    date.setMonthString(date.getMonthString() + s2);
                                else
                                    date.setMonthString(date.getMonthString() + " " + s2);
                            }
                        } else {
                            date.setMonthString(s2);
                        }
                    }
                } else if (s1.equals("<day>") || s1.equals("I-<day>")) {
                    //if (s3.equals("<date>")) 
					{
                        if (date.getDayString() != null) {
                            if ((s1.equals("I-<day>")) ||
                                    (!s1.equals(lastTag) && !lastTag.equals("I-<day>"))
                                    ) {
                                // new date
                                if (date.isNotNull()) {
                                    if (dates == null)
                                        dates = new ArrayList<Date>();
                                    normalize(date);
                                    dates.add(date);
                                }

                                date = new Date();
                                date.setDayString(s2);
                            } else {
                                if (date.getDayString().length() == 0)
                                    date.setDayString(s2);
                                else if ((date.getDayString().charAt(date.getDayString().length() - 1) == '-')
                                        | (date.getDayString().charAt(date.getDayString().length() - 1) == '\''))
                                    date.setDayString(date.getDayString() + s2);
                                else
                                    date.setDayString(date.getDayString() + " " + s2);
                            }
                        } else {
                            date.setDayString(s2);
                        }
                    }
                }

                lastTag = s1;
                lineCount++;
            }
            if (date.isNotNull()) {
                if (dates == null)
                    dates = new ArrayList<Date>();
                normalize(date);
                dates.add(date);
            }

        } catch (Exception e) {
//			e.printStackTrace();
            throw new GrobidException("An exception occured while running Grobid.", e);
        }
        return dates;
    }

    public static final Pattern jan =
            Pattern.compile("([Jj]an$|[Jj]anuary$|[Jj]anvier$|[Jj]annewaori$|[Jj]anuar$|[Ee]nero$|[Jj]anuaro$|[Jj]anuari$|[Jj]aneiro$|[Gg]ennaio$|[Gg]en$|[Oo]cak$|[Jj]a$|(^1$)|(^01$)|(1月))");
    public static final Pattern feb =
            Pattern.compile("([Ff]eb$|[Ff]ebruary$|[Ff][eé]vrier$|[Ff]ebruar$|[Ff]ebrewaori$|[Ff]ebrero$|[Ff]evereiro$|[Ff]ebbraio$|[Ll]uty$|[Ss]tyczeń$|Ş$|ubat$|[Ff]e$|^2$|^02$|2月)");
    public static final Pattern mar =
            Pattern.compile("([Mm]ar$|[Mm]arch$|[Mm]ars$|[Mm]eert$|[Mm]ärz$|[Mm]arzo$|[Mm]arço$|[Mm]art$|[Mm]a$|[Mm]a$|^3$|^03$|3月)");
    public static final Pattern apr =
            Pattern.compile("([Aa]pr$|[Aa]br$|[Aa]vr$|[Aa]pril$|[Aa]vril$|[Aa]pril$|[Aa]prile$|[Aa]bril$|[Nn]isan$|[Aa]p$|^4$|^04$|4月)");
    public static final Pattern may =
            Pattern.compile("([Mm]ay$|[Mm]ai$|[Mm]ay$|[Mm]ayıs$|[Mm]ei$|[Mm]aio$|[Mm]aggio$|[Mm]eie$|[Mm]a$|^5$|^05$|5月)");
    public static final Pattern jun =
            Pattern.compile("([Jj]un$|[Jj]une$|[Jj]uin$|[Jj]uni$|[Jj]unho$|[Gg]iugno$|[Hh]aziran$|^6$|^06$|6月)");
    public static final Pattern jul =
            Pattern.compile("([Jj]ul$|[Jj]uly$|[Jj]uillet$|[Jj]uli$|[Tt]emmuz$|[Ll]uglio$|[Jj]ulho$|^7$|^07$|7月)");
    public static final Pattern aug =
            Pattern.compile("([Aa]ug$|[Aa]ugust$|[Aa]o[uû]t$|[Aa]ugust$|[Aa]gosto$|[Aa]ugustus$|[Aa]ğustos$|^8$|^08$|8月)");
    public static final Pattern sep =
            Pattern.compile("([Ss]ep$|[Ss]ept$|[Ss]eptember$|[Ss]eptembre$|[Ss]eptember$|[Ss]ettembre$|[Ss]etembro$|[Ee]ylül$|^9$|^09$|9月)");
    public static final Pattern oct =
            Pattern.compile("([Oo]ct$|[Oo]cto$|[Oo]ctober$|[Oo]ctobre$|[Ee]kim$|[Oo]ktober$|[Oo]ttobre$|[Oo]utubro$|^10$|10月)");
    public static final Pattern nov =
            Pattern.compile("([Nn]ov$|[Nn]ovember$|[Nn]ovembre$|[Kk]asım$|[Nn]oviembre$|[Dd]icembre$|[Nn]ovembro$|^11$|11月)");
    public static final Pattern dec =
            Pattern.compile("([Dd]ec$|[Dd]ecember$|[Dd][eé]cembre$|[Dd]iciembre$|[Aa]ralık$|^12$|12月)");

    public static final Pattern[] months = {jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec};

    public void normalize(Date date) {
        // normalize day
        if (date.getDayString() != null) {
            String dayStringBis = "";
            String dayString = date.getDayString().trim();
            for (int n = 0; n < dayString.length(); n++) {
                char c = dayString.charAt(n);
                if (Character.isDigit(c)) {
                    dayStringBis += c;
                }
            }
            try {
                int day = Integer.parseInt(dayStringBis);
                date.setDay(day);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

        //normalize month
        if (date.getMonthString() != null) {
            String month = date.getMonthString().trim();
            int n = 0;
            while (n < 12) {
                Matcher ma = months[n].matcher(month);
                if (ma.find()) {
                    date.setMonth(n + 1);
                    break;
                }
                n++;
            }
        }

        if (date.getYearString() != null) {
            String yearStringBis = "";
            String yearString = date.getYearString().trim();
            for (int n = 0; n < yearString.length(); n++) {
                char c = yearString.charAt(n);
                if (Character.isDigit(c)) {
                    yearStringBis += c;
                }
            }
            try {
                int year = Integer.parseInt(yearStringBis);
                if ((year >= 20) && (year < 100)) {
                    year = year + 1900;
                } else if ((year >= 0) && (year < 20)) {
                    year = year + 2000;
                }
                date.setYear(year);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }


    /**
     * Extract results from a date string in the training format without any string modification.
     */
    public StringBuilder trainingExtraction(List<String> inputs) {
        StringBuilder buffer = new StringBuilder();
        try {
            if (inputs == null)
                return null;

            if (inputs.size() == 0)
                return null;

            List<String> tokenizations = null;
            List<String> dateBlocks = new ArrayList<String>();
            for (String input : inputs) {
                if (input == null)
                    continue;

                //StringTokenizer st = new StringTokenizer(input, " \t\n"+TextUtilities.fullPunctuations, true);
                //StringTokenizer st = new StringTokenizer(input, "([" + TextUtilities.punctuations, true);
				tokenizations = analyzer.tokenize(input);
				
                //if (st.countTokens() == 0)
				if (tokenizations.size() == 0)
                    return null;
                //while (st.hasMoreTokens()) {
                //    String tok = st.nextToken();
				for(String tok : tokenizations) {
                    if (tok.equals("\n")) {
                        dateBlocks.add("@newline");
                    } else if (!tok.equals(" ")) {
                        dateBlocks.add(tok + " <date>");
                    }
                    //tokenizations.add(tok);
                }
                dateBlocks.add("\n");
            }

            String headerDate = FeaturesVectorDate.addFeaturesDate(dateBlocks);
            // clear internal context
//            StringTokenizer st = new StringTokenizer(headerDate, "\n");
            //TODO:
//            String res = getTaggerResult(st, "<date>");
            String res = label(headerDate);

            // extract results from the processed file

            //System.out.print(res.toString());
            StringTokenizer st2 = new StringTokenizer(res, "\n");
            String lastTag = null;
            boolean tagClosed = false;
            int q = 0;
            boolean addSpace;
            boolean hasYear = false;
            boolean hasMonth = false;
            boolean hasDay = false;
            String lastTag0;
            String currentTag0;
            boolean start = true;
            while (st2.hasMoreTokens()) {
                String line = st2.nextToken();
                addSpace = false;
                if ((line.trim().length() == 0)) {
                    // new date
                    buffer.append("</date>\n");
                    hasYear = false;
                    hasMonth = false;
                    hasDay = false;
                    buffer.append("\t<date>");
                    continue;
                } else {
                    String theTok = tokenizations.get(q);
                    while (theTok.equals(" ")) {
                        addSpace = true;
                        q++;
                        theTok = tokenizations.get(q);
                    }
                    q++;
                }

                StringTokenizer st3 = new StringTokenizer(line, "\t");
                int ll = st3.countTokens();
                int i = 0;
                String s1 = null;
                String s2 = null;
                //String s3 = null;
                //List<String> localFeatures = new ArrayList<String>();
                while (st3.hasMoreTokens()) {
                    String s = st3.nextToken().trim();
                    if (i == 0) {
                        s2 = TextUtilities.HTMLEncode(s); // string
                    } /*else if (i == ll - 2) {
                        s3 = s; // pre-label, in this case it should always be <date>
                    } */
					else if (i == ll - 1) {
                        s1 = s; // label
                    }
                    i++;
                }

                if (start && (s1 != null)) {
                    buffer.append("\t<date>");
                    start = false;
                }

                lastTag0 = null;
                if (lastTag != null) {
                    if (lastTag.startsWith("I-")) {
                        lastTag0 = lastTag.substring(2, lastTag.length());
                    } else {
                        lastTag0 = lastTag;
                    }
                }
                currentTag0 = null;
                if (s1 != null) {
                    if (s1.startsWith("I-")) {
                        currentTag0 = s1.substring(2, s1.length());
                    } else {
                        currentTag0 = s1;
                    }
                }

                tagClosed = lastTag0 != null && testClosingTag(buffer, currentTag0, lastTag0);

                /*if (newLine) {
                        if (tagClosed) {
                            buffer.append("\t\t\t\t\t\t\t<lb/>\n");
                        }
                        else {
                            buffer.append("<lb/>");
                        }

                    }*/

                String output = writeField(s1, lastTag0, s2, "<day>", "<day>", addSpace, 0);
                if (output != null) {
                    if (lastTag0 != null) {
                        if (hasDay && !lastTag0.equals("<day>")) {
                            buffer.append("</date>\n");
                            hasYear = false;
                            hasMonth = false;
                            buffer.append("\t<date>");
                        }
                    }
                    hasDay = true;
                    buffer.append(output);
                    lastTag = s1;
                    continue;
                } else {
                    output = writeField(s1, lastTag0, s2, "<other>", "<other>", addSpace, 0);
                }
                if (output == null) {
                    output = writeField(s1, lastTag0, s2, "<month>", "<month>", addSpace, 0);
                } else {
                    buffer.append(output);
                    lastTag = s1;
                    continue;
                }
                if (output == null) {
                    output = writeField(s1, lastTag0, s2, "<year>", "<year>", addSpace, 0);
                } else {
                    if (lastTag0 != null) {
                        if (hasMonth && !lastTag0.equals("<month>")) {
                            buffer.append("</date>\n");
                            hasYear = false;
                            hasDay = false;
                            buffer.append("\t<date>");
                        }
                    }
                    buffer.append(output);
                    hasMonth = true;
                    lastTag = s1;
                    continue;
                }
                if (output != null) {
                    if (lastTag0 != null) {
                        if (hasYear && !lastTag0.equals("<year>")) {
                            buffer.append("</date>\n");
                            hasDay = false;
                            hasMonth = false;
                            buffer.append("\t<date>");
                        }
                    }
                    buffer.append(output);
                    hasYear = true;
                    lastTag = s1;
                    continue;
                }
                lastTag = s1;
            }

            if (lastTag != null) {
                if (lastTag.startsWith("I-")) {
                    lastTag0 = lastTag.substring(2, lastTag.length());
                } else {
                    lastTag0 = lastTag;
                }
                currentTag0 = "";
                testClosingTag(buffer, currentTag0, lastTag0);
                buffer.append("</date>\n");
            }
        } catch (Exception e) {
//			e.printStackTrace();
            throw new GrobidException("An exception occured while running Grobid.", e);
        }
        return buffer;
    }

    private String writeField(String s1,
                              String lastTag0,
                              String s2,
                              String field,
                              String outField,
                              boolean addSpace,
                              int nbIndent) {
        String result = null;
        if ((s1.equals(field)) || (s1.equals("I-" + field))) {
            if ((s1.equals("<other>") || s1.equals("I-<other>"))) {
                if (addSpace)
                    result = " " + s2;
                else
                    result = s2;
            } else if (s1.equals(lastTag0) || s1.equals("I-" + lastTag0)) {
                if (addSpace)
                    result = " " + s2;
                else
                    result = s2;
            } else {
                result = "";
                for (int i = 0; i < nbIndent; i++) {
                    result += "\t";
                }
                if (addSpace)
                    result += " " + outField + s2;
                else
                    result += outField + s2;
            }
        }
        return result;
    }

    private boolean testClosingTag(StringBuilder buffer,
                                   String currentTag0,
                                   String lastTag0) {
        boolean res = false;
        if (!currentTag0.equals(lastTag0)) {
            res = true;
            // we close the current tag
            if (lastTag0.equals("<other>")) {
                buffer.append("");
            } else if (lastTag0.equals("<day>")) {
                buffer.append("</day>");
            } else if (lastTag0.equals("<month>")) {
                buffer.append("</month>");
            } else if (lastTag0.equals("<year>")) {
                buffer.append("</year>");
            } else {
                res = false;
            }

        }
        return res;
    }

}