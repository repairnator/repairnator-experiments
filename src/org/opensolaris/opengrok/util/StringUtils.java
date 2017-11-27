/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * See LICENSE.txt included in this distribution for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at LICENSE.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 */

/*
 * Copyright (c) 2008, 2017, Oracle and/or its affiliates. All rights reserved.
 * Portions Copyright (c) 2017, Chris Fraire <cfraire@me.com>.
 */

package org.opensolaris.opengrok.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Various String utility methods.
 *
 * @author austvik
 */
public final class StringUtils {

    /**
     * Matches an apostrophe not following a backslash escape or following an
     * even number¹ of backslash escapes:
     * <pre>
     * {@code
     * \'((?<=^.)|(?<=[^\\].)|(?<=^(\\\\){1,3}.)|(?<=[^\\](\\\\){1,3}.))
     * }
     * </pre>
     * (Edit above and paste below [in NetBeans] for easy String escaping.)
     * <p>
     * ¹"even number" is limited to 2,4,6 because Java look-behind is not
     * variable length but instead must have a definite upper bound in the
     * regex definition.
     */
    public static final Pattern APOS_NO_BSESC =
        Pattern.compile("\\'((?<=^.)|(?<=[^\\\\].)|(?<=^(\\\\\\\\){1,3}.)|(?<=[^\\\\](\\\\\\\\){1,3}.))");

    /**
     * Matches the close of a C comment:
     * <pre>
     * {@code
     * \*\/
     * }
     * </pre>
     * (Edit above and paste below [in NetBeans] for easy String escaping.)
     */
    public static final Pattern END_C_COMMENT =
        Pattern.compile("\\*\\/");

    /**
     * Matches the same possible character as CommonPath.lexh's {FNameChar}:
     * <pre>
     * {@code
     * [a-zA-Z0-9_\-\.]
     * }
     * </pre>
     * (Edit above and paste below [in NetBeans] for easy String escaping.)
     */
    private static final String FNAME_CHARS_PAT =
        "[a-zA-Z0-9_\\-\\.]";

    private static final Pattern FNAME_CHARS_ANYMATCH =
        Pattern.compile(FNAME_CHARS_PAT);

    private static final Pattern FNAME_CHARS_STARTSMATCH =
        Pattern.compile("^" + FNAME_CHARS_PAT);

    /**
     * Matches one of the same¹ possible characters as Common.lexh's {URIChar}:
     * <pre>
     * {@code
     * [a-zA-Z0-9\-\._~%:/\?\#\[\]@!\$&\'\(\)\*\+,;=]
     * }
     * </pre>
     * (Edit above and paste below [in NetBeans] for easy String escaping.)
     * <p>
     * ¹Backslash, '\', was in {URIChar} in many .lex files, but that is not
     * a valid URI character per RFC-3986.
     */
    private static final String URI_CHARS_PAT =
        "[a-zA-Z0-9\\-\\._~%:/\\?\\#\\[\\]@!\\$&\\'\\(\\)\\*\\+,;=]";

    private static final Pattern URI_CHARS_ANYMATCH =
        Pattern.compile(URI_CHARS_PAT);

    private static final Pattern URI_CHARS_STARTSMATCH =
        Pattern.compile("^" + URI_CHARS_PAT);

    private StringUtils() {
        // Only static utility methods
    }

    /**
     * Returns true if the string is empty or only includes whitespace characters.
     *
     * @param str the string to be checked
     * @return true if string is empty or only contains whitespace characters
     */
    public static boolean isOnlyWhitespace(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    static final Pattern javaClassPattern =
        Pattern.compile("([a-z][A-Za-z]*\\.)+[A-Z][A-Za-z0-9]*");
    /**
     * Returns true if the string is possibly a full java class name
     *
     * @param s the string to be checked
     * @return true if string could be a java class name
    */
    public static boolean isPossiblyJavaClass(String s) {
    // Only match a small subset of possible class names to prevent false
    // positives:
    //    - class must be qualified with a package name
    //    - only letters in package name, starting with lower case
    //    - class name must be in CamelCase, starting with upper case
    return javaClassPattern.matcher(s).matches();
  }

  /**
   * Convert value in milliseconds to readable time.
   * @param time_ms delta in milliseconds
   * @return human readable string   
   */
  public static String getReadableTime(long time_ms) {
      String output = "";
      long time_delta = time_ms;

      int milliseconds = (int) (time_delta % 1000);
      time_delta /= 1000;
      int seconds = (int) (time_delta % 60);
      time_delta /= 60;
      int minutes = (int) (time_delta % 60);
      time_delta /= 60;
      int hours = (int) (time_delta % 24);
      int days = (int) (time_delta / 24);

      if (days != 0) {
          output += String.format("%d day", days);
          if (days > 1) {
              output += "s";
          }
          if ((hours != 0) || (minutes != 0) || (seconds != 0)) {
              output += " ";
          }
      }
      if ((hours != 0) || (minutes != 0)) {
          return output + String.format("%d:%02d:%02d", hours, minutes, seconds);
      }
      if (seconds != 0) {
          return output + String.format("%d.%d seconds", seconds, milliseconds);
      }
      if (milliseconds != 0) {
          return output + String.format("%d ms", milliseconds);
      }

      return (output.length() == 0 ? "0" : output);
  }

    /**
     * Finds n-th index of a given substring in a string.
     *
     * @param str an original string
     * @param substr a substring to match
     * @param n n-th occurrence
     * @return the index of the first character of the substring in the original
     * string where the substring occurred n-th times in the string. If the n-th
     * candidate does not exist, -1 is returned.
     */
    public static int nthIndexOf(String str, String substr, int n) {
        int pos = -1;
        while (n > 0) {
            if (pos >= str.length()) {
                return -1;
            }
            if ((pos = str.indexOf(substr, pos + 1)) == -1) {
                break;
            }
            n--;
        }
        return pos;
    }

    /**
     * Determines if the {@code value} contains characters matching
     * Common.lexh's {FNameChar}.
     * @param value the input to test
     * @return true if {@code value} matches anywhere
     */
    public static boolean containsFnameChars(String value) {
        return FNAME_CHARS_ANYMATCH.matcher(value).matches();
    }

    /**
     * Determines if the {@code value} starts with characters matching
     * Common.lexh's {FNameChar}.
     * @param value the input to test
     * @return true if {@code value} matches at its start
     */
    public static boolean startsWithFnameChars(String value) {
        return FNAME_CHARS_STARTSMATCH.matcher(value).matches();
    }

    /**
     * Determines if the {@code value} contains characters matching
     * RFC-3986 and Common.lexh's definitions for allowable URI characters.
     * @param value the input to test
     * @return true if {@code value} matches anywhere
     */
    public static boolean containsURIChars(String value) {
        return URI_CHARS_ANYMATCH.matcher(value).matches();
    }

    /**
     * Determines if the {@code value} starts with characters matching
     * RFC-3986 and Common.lexh's definitions for allowable URI characters.
     * @param value the input to test
     * @return true if {@code value} matches at its start
     */
    public static boolean startsWithURIChars(String value) {
        return URI_CHARS_STARTSMATCH.matcher(value).matches();
    }

    /**
     * Count the number of ending pushback characters from a matched URI.
     * <p>
     * jflex does not support negative lookbehind, so modifying a URI matcher
     * to backtrack on ending characters that are otherwise normally valid
     * (e.g. '.') is difficult. Instead, this method allows counting and
     * pushing back.
     * @param value the URI to test
     * @return the number of characters greater than or equal to zero which
     * could be pushed back.
     */
    public static int countURIEndingPushback(String value) {
        int n = 0;
        for (int i = value.length() - 1; i >= 0; --i) {
            char c = value.charAt(i);
            if (c == '.') {
                ++n;
            } else {
                break;
            }
        }
        return n;
    }

    /**
     * Find out if string contains only alphanumeric characters.
     * @param str string to check
     * @return {@code true} if the string is alphanumeric, {@code false} otherwise
     */
    public static boolean isAlphanumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isDigit(c) && !Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines if the specified {@code pattern} matches in the specified
     * {@code value}.
     * @param value the string to inspect
     * @param pattern the pattern to match
     * @return the index of the first occurrence of the specified pattern, or
     * -1 if there is no such occurrence
     */
    public static int patindexOf(String value, Pattern pattern) {
        Matcher m = pattern.matcher(value);
        if (!m.find()) return -1;
        return m.start();
    }
}
