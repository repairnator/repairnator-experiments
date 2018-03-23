/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.mapper.helper;

import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.util.StringUtil;

/**
 * @author liuzh_3nofxnp
 * @since 2016-08-29 22:02
 */
public class CamelCaseTest {

    @Test
    public void testCamelhumpToUnderline() {
        Assert.assertEquals("user_id", StringUtil.camelhumpToUnderline("userId"));
        Assert.assertEquals("sys_user", StringUtil.camelhumpToUnderline("sysUser"));
        Assert.assertEquals("sys_user_role", StringUtil.camelhumpToUnderline("sysUserRole"));
        Assert.assertEquals("s_function", StringUtil.camelhumpToUnderline("sFunction"));
    }
}
