/**
 * Copyright (c) 2016-2018 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.pm.qa;

import com.jcabi.matchers.XhtmlMatchers;
import com.zerocracy.cash.Cash;
import com.zerocracy.farm.fake.FkProject;
import com.zerocracy.pm.ClaimOut;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.xembly.Xembler;

/**
 * Test case for {@link Reviews}.
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.22
 * @checkstyle JavadocMethodCheck (500 lines)
 */
public final class ReviewsTest {

    @Test
    public void addsAndRemovesReview() throws Exception {
        final Reviews reviews = new Reviews(new FkProject()).bootstrap();
        final String job = "gh:yegor256/0pdd#3";
        reviews.add(
            job, "yegor256", "dmarkov",
            new Cash.S("$10"), 1, new Cash.S("$15")
        );
        final ClaimOut out = reviews.remove(job, true, new ClaimOut());
        MatcherAssert.assertThat(
            new Xembler(out).xmlQuietly(),
            XhtmlMatchers.hasXPaths(
                "/claim/params/param[@name='cash' and .='$25.00']"
            )
        );
    }

    @Test
    public void fetchesInspector() throws Exception {
        final Reviews reviews = new Reviews(new FkProject()).bootstrap();
        final String job = "gh:yegor256/0pdd#99";
        final String inspector = "yegor1";
        reviews.add(
            job, inspector, "dmarkov1",
            new Cash.S("$11"), 1, new Cash.S("$199")
        );
        MatcherAssert.assertThat(
            reviews.inspector(job),
            Matchers.equalTo(inspector)
        );
    }

    @Test
    public void fetchesPerformer() throws Exception {
        final Reviews reviews = new Reviews(new FkProject()).bootstrap();
        final String job = "gh:yegor256/0pdd#199";
        final String performer = "yegor2";
        reviews.add(
            job, "dmarkov2", performer,
            new Cash.S("$110"), 1, new Cash.S("$194")
        );
        MatcherAssert.assertThat(
            reviews.performer(job),
            Matchers.equalTo(performer)
        );
        MatcherAssert.assertThat(
            reviews.requested(job),
            Matchers.not(Matchers.nullValue())
        );
    }

}
