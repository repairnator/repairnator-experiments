/* LanguageTool, a natural language style checker 
 * Copyright (C) 2005 Daniel Naber (http://www.danielnaber.de)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package org.languagetool.synthesis.sk;

import org.junit.Test;
import org.languagetool.AnalyzedToken;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SlovakSynthesizerTest {

  @Test
  public final void testSynthesizeStringString() throws IOException {
    SlovakSynthesizer synth = new SlovakSynthesizer();
    assertEquals(synth.synthesize(dummyToken("blablabla"), 
        "blablabla").length, 0);
    
    assertEquals("[časopisu]", Arrays.toString(synth.synthesize(dummyToken("časopis"), "SSis2")));    
    //with regular expressions
    assertEquals("[časopisy, časopisov, časopisom, časopisy, časopisy, časopisoch, časopismi, časopis, časopisu, časopisu, časopis, časopis, časopise, časopisom]", Arrays.toString(synth.synthesize(dummyToken("časopis"), "SS.*", true)));    
  }
  
  private AnalyzedToken dummyToken(String tokenStr) {
    return new AnalyzedToken(tokenStr, tokenStr, tokenStr);
  }

}
