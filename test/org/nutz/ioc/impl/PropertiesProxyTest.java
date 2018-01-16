package org.nutz.ioc.impl;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertiesProxyTest {

    private PropertiesProxy pp;

    private static final String UTF8_CHARSET = "UTF-8";
    private static final String CHINESE_STR = "Nutz 超级棒的Framework!谁用谁喜欢，嘻嘻。";

    private static final String CHARSET_KEY = "charset";
    private static final String LONG_STR = "longstr";

    @Before
    public void init() {
        pp = new PropertiesProxy(false, "/config/conf.properties");
    }

    @Test
    public void testUTF8Properties() {
        PropertiesProxy i18nPP = new PropertiesProxy(false, "/config/conf-utf8.properties");
        i18nPP.setIgnoreResourceNotFound(true);

        Assert.assertEquals(UTF8_CHARSET, i18nPP.get(CHARSET_KEY));
        Assert.assertEquals(CHINESE_STR, i18nPP.get(LONG_STR));
    }

    @Test
    public void testString() throws UnsupportedEncodingException {
        Assert.assertEquals("Nutz ", pp.get("str"));
        Assert.assertEquals("Nutz", pp.getTrim("str"));
        Assert.assertEquals("坚果", new String(pp.getTrim("chinese")));
    }

    @Test
    public void testNumber() {
        Assert.assertEquals(153, pp.getLong("number"));
        Assert.assertEquals(153, pp.getInt("number"));
    }

    @Test
    public void testBoolean() {
        Assert.assertEquals(true, pp.getBoolean("bool"));
    }

    @Test
    public void testHas() {
        Assert.assertTrue(pp.has("str"));
    }

    @Test
    public void testSize() {
        Assert.assertEquals(pp.getKeys().size(), 4);
        Assert.assertEquals(pp.getValues().size(), 4);
    }
}
