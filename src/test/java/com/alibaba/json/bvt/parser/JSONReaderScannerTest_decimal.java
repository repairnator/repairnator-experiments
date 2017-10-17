package com.alibaba.json.bvt.parser;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONReaderScanner;

public class JSONReaderScannerTest_decimal extends TestCase {

    public void test_scanInt() throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append('[');
        for (int i = 0; i < 1024; ++i) {
            if (i != 0) {
                buf.append(',');
            }
            buf.append(i + ".0");
        }
        buf.append(']');

        Reader reader = new StringReader(buf.toString());

        JSONReaderScanner scanner = new JSONReaderScanner(reader);

        DefaultJSONParser parser = new DefaultJSONParser(scanner);
        JSONArray array = (JSONArray) parser.parse();
        for (int i = 0; i < array.size(); ++i) {
            BigDecimal value = new BigDecimal(i + ".0");
            Assert.assertEquals(value, array.get(i));
        }
    }
}
