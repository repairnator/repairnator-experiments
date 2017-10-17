package com.alibaba.json.bvt.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;
import org.junit.Assert;

public class DateFieldFormatTest extends TestCase {

    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_format_() throws Exception {
        Date now = new Date();
        Model model = new Model();
        model.serverTime = now;
        model.publishTime = now;
        model.setStartDate( now );
        
        String text = JSON.toJSONString(model);
        System.out.println(text);

        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        df1.setTimeZone(JSON.defaultTimeZone);
        df2.setTimeZone(JSON.defaultTimeZone);
        df3.setTimeZone(JSON.defaultTimeZone);

        String t1 = df1.format(now);
        String t2 = df2.format(now);
        String t3 = df3.format(now);

        Assert.assertEquals("{\"publishTime\":\""+t2+"\",\"serverTime\":\""+t1+"\",\"startDate\":\""+t3+"\"}",text);
        
        Model model2 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(t2,new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA).format(model2.publishTime));
        Assert.assertEquals(t1,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(model2.serverTime));
        Assert.assertEquals(t3,new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(model2.getStartDate()));
        
    }

    public static class Model {

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        public Date serverTime;

        @JSONField(format = "yyyy/MM/dd HH:mm:ss")
        public Date publishTime;

        @JSONField(format = "yyyy-MM-dd")
        private Date startDate;

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }
    }
}
