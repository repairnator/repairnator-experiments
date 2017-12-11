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
 * Copyright (c) 2017, Chris Fraire <cfraire@me.com>.
 */

package org.opensolaris.opengrok.analysis.perl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.opensolaris.opengrok.analysis.Resettable;
import org.opensolaris.opengrok.util.StringUtils;
import org.opensolaris.opengrok.web.HtmlConsts;
import org.opensolaris.opengrok.analysis.JFlexJointLexer;
import org.opensolaris.opengrok.util.RegexUtils;

/**
 * Represents an API for object's using {@link PerlLexHelper}
 */
interface PerlLexer extends JFlexJointLexer {
    void maybeIntraState();

    /**
     * Indicates that a premature end of quoting occurred. Everything up to the
     * causal character has been written, and anything following will be
     * indicated via {@link yypushback}.
     */
    void abortQuote() throws IOException;
}

/**
 * Represents a helper for Perl lexers to work around jflex's lack of lex
 * inheritance
 */
class PerlLexHelper implements Resettable {

    // Using equivalent of {Identifier} from PerlProductions.lexh
    private final static Pattern HERE_TERMINATOR_MATCH = Pattern.compile(
        "^[a-zA-Z0-9_]+");

    private final PerlLexer lexer;

    private final int QUOxLxN;
    private final int QUOxN;
    private final int QUOxL;
    private final int QUO;
    private final int HEREinxN;
    private final int HERExN;
    private final int HEREin;
    private final int HERE;

    private Queue<HereDocSettings> hereSettings;

    /**
     * When matching a quoting construct like qq[], q(), m//, s```, etc., the
     * operator name (e.g., "m" or "tr") is stored. Unlike {@code endqchar} it
     * is not unset when the quote ends, because it is useful to indicate if
     * quote modifier characters are expected.
     */
    private String qopname;

    /**
     * When matching a quoting construct like qq[], q(), m//, s```, etc., the
     * terminating character is stored.
     */
    private char endqchar;

    /**
     * When matching a quoting construct like qq[], q(), m&lt;&gt;, s{}{} that
     * nest, the begin character ('[', '&lt;', '(', or '{') is stored so that
     * nesting is tracked and {@code nendqchar} is incremented appropriately.
     * Otherwise, {@code nestqchar} is set to '\0' if no nesting occurs.
     */
    private char nestqchar;

    /**
     * When matching a quoting construct like qq//, m//, or s```, etc., the
     * number of remaining end separators in an operator section is stored.
     * It starts at 1, and any nesting increases the value.
     */
    private int nendqchar;

    /**
     * When matching a quoting construct like qq//, m//, or s```, etc., the
     * number of sections for the operator is stored. E.g., for m//, it is 1;
     * for tr/// it is 2.
     */
    private int nsections;

    /**
     * When matching a two part construct like tr/// or s```, etc., after the
     * first end separator, {@code waitq} is set to true so that nesting is not
     * active.
     */
    private boolean waitq;

    /**
     * When matching a quoting construct, a Pattern to identify collateral
     * capture characters is stored.
     */
    private Pattern collateralCapture;

    public PerlLexHelper(int qUO, int qUOxN, int qUOxL, int qUOxLxN,
        PerlLexer lexer,
        int hERE, int hERExN, int hEREin, int hEREinxN) {
        if (lexer == null) {
            throw new IllegalArgumentException("`lexer' is null");
        }
        this.lexer = lexer;
        this.QUOxLxN = qUOxLxN;
        this.QUOxN = qUOxN;
        this.QUOxL = qUOxL;
        this.QUO = qUO;
        this.HEREinxN = hEREinxN;
        this.HERExN = hERExN;
        this.HEREin = hEREin;
        this.HERE = hERE;
    }

    /**
     * Resets the instance to an initial state.
     */
    @Override
    public void reset() {
        collateralCapture = null;
        endqchar = '\0';
        if (hereSettings != null) hereSettings.clear();
        nendqchar = 0;
        nestqchar = '\0';
        nsections = 0;
        qopname = null;
        waitq = false;
    }

    /**
     * Determine if the quote should end based on the first character of
     * {@code capture}, recognizing quote-like operators that allow nesting to
     * increase the nesting level if appropriate.
     * <p>
     * Calling this method has side effects to possibly modify {@code nqchar},
     * {@code waitq}, or {@code endqchar}.
     * @return true if the quote state should end
     */
    public boolean maybeEndQuote(String capture) {
        char c = capture.charAt(0);
        if (c == endqchar) {
            if (--nendqchar <= 0) {
                if (--nsections <= 0) {
                    endqchar = '\0';
                    nestqchar = '\0';
                    return true;
                } else if (nestqchar != '\0') {
                    waitq = true;
                } else {
                    nendqchar = 1;
                }
            }
        } else if (nestqchar != '\0' && c == nestqchar) {
            if (waitq) {
                waitq = false;
                nendqchar = 1;
            } else {
                ++nendqchar;
            }
        }
        return false;
    }

    /**
     * Gets a value indicating if modifiers are OK at the end of the last
     * quote-like operator.
     * @return true if modifiers are OK
     */
    public boolean areModifiersOK() {
        switch (qopname) {
            case "m":
            case "qr":
            case "s":
            case "tr":
            case "y":
                return true;
            default:
                return false;
        }
    }

    /**
     * Starts a quote-like operator as specified in a syntax fragment,
     * {@code op}, and gives the {@code op} for the {@code lexer} to take.
     */
    public void qop(String op, int namelength, boolean nointerp)
        throws IOException {
        qop(true, op, namelength, nointerp);
    }

    /**
     * Starts a quote-like operator as specified in a syntax fragment,
     * {@code op}, and gives the {@code capture} for the {@code lexer} to
     * take if {@code doWrite} is true.
     */
    public void qop(boolean doWrite, String capture, int namelength,
        boolean nointerp) throws IOException {
        // If namelength is positive, allow that a non-zero-width word boundary
        // character may have needed to be matched since jflex does not conform
        // with \b as a zero-width simple word boundary. Excise it into
        // `boundary'.
        String boundary = "";
        String postboundary = capture;
        qopname = "";
        if (namelength > 0) {
            postboundary = capture.replaceFirst("^\\W+", "");
            boundary = capture.substring(0, capture.length() -
                postboundary.length());
            qopname = postboundary.substring(0, namelength);
        }
        waitq = false;
        nendqchar = 1;
        collateralCapture = null;

        switch (qopname) {
            case "tr":
            case "s":
            case "y":
                nsections = 2;
                break;
            default:
                nsections = 1;
                break;
        }

        String postop = postboundary.substring(qopname.length());
        String ltpostop = postop.replaceFirst("^\\s+", "");
        char opc = ltpostop.charAt(0);
        setEndQuoteChar(opc);
        setState(ltpostop, nointerp);

        if (doWrite) {
            lexer.offerNonword(boundary);
            if (qopname.length() > 0) {
                lexer.offerSymbol(qopname, boundary.length(), false);
            } else {
                lexer.skipSymbol();
            }
            lexer.disjointSpan(HtmlConsts.STRING_CLASS);
            lexer.offerNonword(postop);
        }
    }

    /**
     * Sets the jflex state reflecting {@code ltpostop} and {@code nointerp}.
     */
    public void setState(String ltpostop, boolean nointerp) {
        int state;
        boolean nolink = false;

        // "no link" for values in the rules for "string links" if `ltpostop'
        // starts path-like or with the e-mail delimiter.
        if (StringUtils.startsWithFpathChar(ltpostop) ||
            ltpostop.startsWith("@")) {
            nolink = true;
        }

        if (nointerp) {
            state = nolink ? QUOxLxN : QUOxN;
        } else {
            state = nolink ? QUOxL : QUO;
        }
        lexer.maybeIntraState();
        lexer.yypush(state);
    }

    /**
     * Sets a special {@code endqchar} if appropriate for {@code opener} or
     * just tracks {@code opener} as {@code endqchar}.
     */
    private void setEndQuoteChar(char opener) {
        switch (opener) {
            case '[':
                nestqchar = opener;
                endqchar = ']';
                break;
            case '<':
                nestqchar = opener;
                endqchar = '>';
                break;
            case '(':
                nestqchar = opener;
                endqchar = ')';
                break;
            case '{':
                nestqchar = opener;
                endqchar = '}';
                break;
            default:
                nestqchar = '\0';
                endqchar = opener;
                break;
        }
    }

    /**
     * Begins a quote-like state for a heuristic match of the shorthand // of
     * m// where the {@code capture} ends with "/", begins with punctuation,
     * and the intervening whitespace may contain LFs -- and writes the parts
     * to output.
     */
    public void hqopPunc(String capture) throws IOException {
        // `preceding' is everything before the '/'; 'lede' is the initial part
        // before any whitespace; and `intervening' is any whitespace.
        String preceding = capture.substring(0, capture.length() - 1);
        String lede = preceding.replaceFirst("\\s+$", "");
        String intervening = preceding.substring(lede.length());

        // OK to pass a fake "m/" with doWrite=false
        qop(false, "m/", 1, false);
        lexer.offerNonword(lede);
        takeWhitespace(intervening);
        lexer.disjointSpan(HtmlConsts.STRING_CLASS);
        lexer.offer("/");
    }

    /**
     * Begins a quote-like state for a heuristic match of the shorthand // of
     * m// where the {@code capture} ends with "/", begins with an initial
     * symbol, and the intervening whitespace may contain LFs -- and writes the
     * parts to output.
     */
    public void hqopSymbol(String capture) throws IOException {
        // `preceding' is everything before the '/'; 'lede' is the initial part
        // before any whitespace; and `intervening' is any whitespace.
        String preceding = capture.substring(0, capture.length() - 1);
        String lede = preceding.replaceFirst("\\s+$", "");
        String intervening = preceding.substring(lede.length());

        // OK to pass a fake "m/" with doWrite=false
        qop(false, "m/", 1, false);
        lexer.offerSymbol(lede, 0, false);
        takeWhitespace(intervening);
        lexer.disjointSpan(HtmlConsts.STRING_CLASS);
        lexer.offer("/");
    }

    /**
     * Write {@code whsp} to output -- if it does not contain any LFs then the
     * full String is written; otherwise, pre-LF spaces are condensed as usual.
     */
    private void takeWhitespace(String whsp) throws IOException {
        int i;
        if ((i = whsp.indexOf("\n")) == -1) {
            lexer.offer(whsp);
        } else {
            int numlf = 1, off = i + 1;
            while ((i = whsp.indexOf("\n", off)) != -1) {
                ++numlf;
                off = i + 1;
            }
            while (numlf-- > 0) lexer.startNewLine();
            if (off < whsp.length()) lexer.offer(whsp.substring(off));
        }
    }

    /**
     * Parses a Here-document declaration, and takes the {@code capture} using
     * {@link PerlLexer#offerNonword(java.lang.String)}. If the
     * declaration is valid, {@code hereSettings} will have been appended.
     */
    public void hop(String capture) throws IOException {
        if (!capture.startsWith("<<")) {
            throw new IllegalArgumentException("bad HERE: " + capture);
        }

        lexer.offerNonword(capture);
        if (hereSettings == null) hereSettings = new LinkedList<>();

        String remaining = capture;
        int i = 0;
        HereDocSettings settings;
        boolean indented = false;
        boolean nointerp;
        String terminator;

        String opener = remaining.substring(0, i + 2);
        remaining = remaining.substring(opener.length());
        if (remaining.startsWith("~")) {
            indented = true;
            remaining = remaining.substring(1);
        }
        remaining = remaining.replaceFirst("^\\s+", "");
        char c = remaining.charAt(0);
        switch (c) {
            case '\'':
                nointerp = true;
                remaining = remaining.substring(1);
                break;
            case '`':
            case '\"':
                nointerp = false;
                remaining = remaining.substring(1);
                break;
            case '\\':
                c = '\0';
                nointerp = true;
                remaining = remaining.substring(1);
                break;
            default:
                c = '\0';
                nointerp = false;
                break;
        }

        if (c != '\0') {
            if ((i = remaining.indexOf(c)) < 1) {
                terminator = remaining;
            } else {
                terminator = remaining.substring(0, i);
            }
        } else {
            Matcher m = HERE_TERMINATOR_MATCH.matcher(remaining);
            if (!m.find()) return;
            terminator = m.group(0);
        }

        int state;
        if (nointerp) {
            state = indented ? HEREinxN : HERExN;
        } else {
            state = indented ? HEREin : HERE;
        }
        settings = new HereDocSettings(terminator, state);
        hereSettings.add(settings);
    }

    /**
     * Pushes the first Here-document state if any declarations were parsed, or
     * else does nothing.
     * @return true if a Here state was pushed
     */
    public boolean maybeStartHere() throws IOException {
        if (hereSettings != null && hereSettings.size() > 0) {
            HereDocSettings settings = hereSettings.peek();
            lexer.yypush(settings.state);
            lexer.disjointSpan(HtmlConsts.STRING_CLASS);
            return true;
        }
        return false;
    }

    /**
     * Process the {@code capture}, possibly ending the Here-document state
     * just beforehand.
     * @return true if the quote state ended
     */
    public boolean maybeEndHere(String capture) throws IOException {
        String trimmed = capture.replaceFirst("^\\s+", "");
        HereDocSettings settings = hereSettings.peek();

        boolean didZspan = false;
        if (trimmed.equals(settings.terminator)) {
            lexer.disjointSpan(null);
            didZspan = true;
            hereSettings.remove();
        }

        lexer.offerNonword(capture);

        if (hereSettings.size() > 0) {
            settings = hereSettings.peek();
            lexer.yybegin(settings.state);
            if (didZspan) lexer.disjointSpan(HtmlConsts.STRING_CLASS);
            return false;
        } else {
            lexer.yypop();
            return true;
        }
    }

    /**
     * Splits a sigil identifier -- where the {@code capture} starts with
     * a sigil and ends in an identifier and where Perl allows whitespace after
     * the sigil -- and write the parts to output.
     * <p>
     * Seeing the {@link endqchar} in the capture will affect an active
     * quote-like operator.
     */
    public void sigilID(String capture) throws IOException {
        String sigil = capture.substring(0, 1);

        if (capture.charAt(0) == endqchar) {
            lexer.skipSymbol();
            lexer.offerNonword(sigil);
            if (maybeEndQuote(sigil)) lexer.abortQuote();
            lexer.yypushback(capture.length() - 1);
            return;
        }

        String postsigil = capture.substring(1);
        String id = postsigil.replaceFirst("^\\s+", "");
        String s0 = postsigil.substring(0, postsigil.length() - id.length());

        int ohnooo;
        if ((ohnooo = id.indexOf(endqchar)) == -1) {
            lexer.offerNonword(sigil);
            lexer.offer(s0);
            lexer.offerSymbol(id, sigil.length() + s0.length(), true);
        } else {
            // If the identifier contains the end quoting character, then it
            // may or may not parse in Perl. Treat everything before the first
            // instance of the `endqchar' as inside the quote -- and possibly
            // as real identifier -- and possibly abort the quote early.
            // e.g.: qr z$abcz;
            //     OK
            // e.g.: qr z$abziz;
            //     Unknown regexp modifier "/z" at -e line 1, near "= "
            //     Global symbol "$ab" requires explicit package name ...
            // e.g.: qr i$abixi;
            //     OK
            String w0 = id.substring(0, ohnooo);
            String p0 = id.substring(ohnooo, ohnooo + 1);
            String w1 = id.substring(ohnooo + 1);
            lexer.offerNonword(sigil);
            lexer.offer(s0);
            if (w0.length() > 0) {
                lexer.offerSymbol(w0, sigil.length() + s0.length(), true);
            } else {
                lexer.skipSymbol();
            }
            lexer.offerNonword(p0);
            if (maybeEndQuote(p0)) lexer.abortQuote();
            lexer.yypushback(w1.length());
        }
    }

    /**
     * Splits a braced sigil identifier -- where the {@code capture} starts
     * with a sigil and ends with a '}' and where Perl allows whitespace after
     * the sigil and around the identifier -- and write the parts to output.
     */
    public void bracedSigilID(String capture) throws IOException {
        // $      {      identifier      }
        //
        // S|_________interior0_________|r
        //        |_____ltinterior0_____|r
        //        l|_____interior1______|r
        //               |_ltinterior1__|r
        // S|_s0_|l|_s1_||---id---||_s2_|r

        String sigil = capture.substring(0, 1);
        String rpunc = capture.substring(capture.length() - 1);
        String interior0 = capture.substring(1, capture.length() - 1);
        String ltinterior0 = interior0.replaceFirst("^\\s+", "");
        String s0 = interior0.substring(0, interior0.length() -
            ltinterior0.length());

        String lpunc = ltinterior0.substring(0, 1);
        String interior1 = ltinterior0.substring(1);
        String ltinterior1 = interior1.replaceFirst("^\\s+", "");
        String s1 = interior1.substring(0, interior1.length() -
            ltinterior1.length());

        String s2 = ltinterior1.replaceFirst("^\\S+", "");
        String id = ltinterior1.substring(0, ltinterior1.length() -
            s2.length());

        lexer.offerNonword(sigil);
        lexer.offer(s0);
        lexer.offerNonword(lpunc);
        lexer.offer(s1);
        lexer.offerSymbol(id, sigil.length() + s0.length() +
            lpunc.length() + s1.length(), true);
        lexer.offer(s2);
        lexer.offerNonword(rpunc);
    }

    /**
     * Write a special identifier as a keyword -- unless {@link endqchar} is in
     * the {@code capture}, which will affect an active quote-like operator
     * instead.
     */
    public void specialID(String capture) throws IOException {
        if (capture.indexOf(endqchar) == -1) {
            lexer.offerKeyword(capture);
        } else {
            for (int i = 0; i < capture.length(); ++i) {
                char c = capture.charAt(i);
                String w = new String(new char[] {c});
                lexer.offerNonword(w);
                if (maybeEndQuote(w)) {
                    lexer.abortQuote();
                    lexer.yypushback(capture.length() - i - 1);
                    break;
                }
            }
        }
    }

    /**
     * Gets a pattern to match the collateral capture for the current quoting
     * state or null if there is no active quoting state. 
     * @return a defined pattern or null
     */
    public Pattern getCollateralCapturePattern() {
        if (endqchar == '\0') return null;
        if (collateralCapture != null) return collateralCapture;

        StringBuilder patb = new StringBuilder("[");
        patb.append(Pattern.quote(String.valueOf(endqchar)));
        if (nestqchar != '\0') {
            patb.append(Pattern.quote(String.valueOf(nestqchar)));
        }
        patb.append("]");
        patb.append(RegexUtils.getNotFollowingEscapePattern());
        collateralCapture = Pattern.compile(patb.toString());
        return collateralCapture;
    }

    class HereDocSettings {
        private final String terminator;
        private final int state;

        public HereDocSettings(String terminator, int state) {
            this.terminator = terminator;
            this.state = state;
        }

        public String getTerminator() { return terminator; }

        public int getState() { return state; }
    }
}
