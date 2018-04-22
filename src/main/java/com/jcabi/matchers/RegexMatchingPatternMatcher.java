/**
 * Copyright (c) 2011-2017, jcabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.matchers;

import com.jcabi.aspects.Immutable;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matcher of Regex patterns against a String, similar to
 * {@link String#matches(String)}.
 *
 * <p>Objects of this class are immutable and thread-safe.
 *
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 * @since 1.3
 */
@Immutable
@ToString
@EqualsAndHashCode(callSuper = false, of = "pattern")
final class RegexMatchingPatternMatcher extends TypeSafeMatcher<String> {

    /**
     * The Regex pattern.
     */
    private final transient String pattern;

    /**
     * Public ctor.
     * @param regex The regular expression to match against.
     */
    RegexMatchingPatternMatcher(final String regex) {
        super();
        this.pattern = regex;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("a String matching the regular expression ")
            .appendText(this.pattern);
    }

    @Override
    public boolean matchesSafely(final String item) {
        return item.matches(this.pattern);
    }

}
