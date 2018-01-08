/*
 * Copyright (c) 2001-2017, Zoltan Farkas All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Additionally licensed with:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.spf4j.zel.vm;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author zoly
 */
public final  class TestDecimalOperations {


    @Test
    public void test() throws CompileException, ExecutionException, InterruptedException {
        Program prog = Program.compile("use dec 64; 1.0/3");
        BigDecimal result = (BigDecimal) prog.execute();
        String psRes = result.toPlainString();
        System.out.println(psRes);
        Assert.assertEquals(18, psRes.length());
    }

    @Test
    public void test2() throws CompileException, ExecutionException, InterruptedException {
        Program prog = Program.compile("use dec 128; 1.0/3");
        BigDecimal result = (BigDecimal) prog.execute();
        String psRes = result.toPlainString();
        System.out.println(psRes);
        Assert.assertEquals(36, psRes.length());
    }

    @Test
    public void test3() throws CompileException, ExecutionException, InterruptedException {
        BigDecimal result = (BigDecimal) Program.compile("use dec 128; (1.0/3) * 3 + (1 - 1.0/3*3)").execute();
        System.out.println(result.toPlainString());
        Assert.assertEquals(0, BigDecimal.ONE.compareTo(result));
    }


}
