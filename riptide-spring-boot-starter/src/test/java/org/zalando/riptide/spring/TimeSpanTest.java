package org.zalando.riptide.spring;

import org.junit.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class TimeSpanTest {

    @Test
    public void shouldParseEmpty() {
        final TimeSpan span = TimeSpan.valueOf("");
        assertThat(span.getAmount(), is(0L));
    }

    @Test
    public void shouldParseSingular() {
        final TimeSpan span = TimeSpan.valueOf("1 second");
        assertThat(span.to(SECONDS), is(1L));
    }

    @Test
    public void shouldParsePlural() {
        final TimeSpan span = TimeSpan.valueOf("17 milliseconds");
        assertThat(span.to(MILLISECONDS), is(17L));
    }

    @Test
    public void shouldParseNonLowerCase() {
        final TimeSpan span = TimeSpan.valueOf("17 Seconds");
        assertThat(span.to(SECONDS), is(17L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailOnUnknownTimeUnit() {
        TimeSpan.valueOf("1 decade");
    }

    @Test
    public void shouldRenderToString() {
        assertThat(TimeSpan.valueOf("17 seconds"), hasToString("17 seconds"));
    }

}
