package com.alibaba.json.test;

import com.alibaba.fastjson.util.IOUtils;
import junit.framework.TestCase;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by wenshao on 24/07/2017.
 */
public class UTF8Test_decode extends TestCase {
    Charset charset = Charset.forName("UTF-8");


    char[] chars;
    byte[] bytes;


    CharBuffer charBuffer;

    protected void setUp() throws Exception {
        String text = "央视的报道《陆军第82集团军：聚合重塑 强军路上当先锋！》披露，从南昌起义到平江起义，从井冈山斗争到两万五千里长征，从首战平型关到历经三大战役，从穿插三所里到全歼美军北极熊团，22勇士飞夺泸定桥、18勇士强渡乌江、鏖战冀中“野八旅”、屡战屡胜“老虎连”、万岁军，一支支善战的部队，一个个滚烫的名字，熔铸成第82集团军新的灵魂。";
        text = "Model and actress Emily Ratajkowski would you like you to know she has wonderful abs. We don’t know this because we’re psychic, but rather can surmise this desire from her many photos she posts on Instagram. Whether it’s due to genetics, diet, great Instagram techniques, or some combination of the above, the rising star takes ample opportunity to show you what she’s got, and her fans love it.";

        bytes = text.getBytes(charset);
        chars = new char[bytes.length];
        charBuffer = CharBuffer.allocate(bytes.length);
    }

    public void test_encode() throws Exception {


        for (int i = 0; i < 10; ++i) {
            long start = System.currentTimeMillis();
            f0();
            long millis = System.currentTimeMillis() - start;
            System.out.println("millis : " + millis);
        }
    }

    private void f0() throws Exception {
        for (int i = 0; i < 1000 * 1000; ++i) {
            new String(bytes, charset);
        }
    }

    private void f1() throws Exception {
        for (int i = 0; i < 1000 * 1000; ++i) {
            IOUtils.decodeUTF8(bytes, 0, bytes.length, chars);
        }
    }

    private void f2() throws Exception {
        for (int i = 0; i < 1000 * 1000; ++i) {
            charset.newDecoder().decode(ByteBuffer.wrap(bytes));
        }
    }
}
