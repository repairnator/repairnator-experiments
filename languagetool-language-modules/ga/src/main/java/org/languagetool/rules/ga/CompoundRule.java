/* LanguageTool, a natural language style checker
 * Copyright (C) 2006 Daniel Naber (http://www.danielnaber.de)
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
package org.languagetool.rules.ga;

import java.io.IOException;
import java.util.ResourceBundle;

import org.languagetool.rules.AbstractCompoundRule;
import org.languagetool.rules.CompoundRuleData;
import org.languagetool.rules.Example;

/**
 * Checks that compounds (if in the list) are not written as separate words.
 */
public class CompoundRule extends AbstractCompoundRule {

  private static final CompoundRuleData compoundData = new CompoundRuleData("/ga/compounds.txt");

  public CompoundRule(ResourceBundle messages) throws IOException {
    super(messages,
            "Litrítear an focal seo le fleiscín de ghnáth.",
            "Litrítear an focal seo mar fhocal amháin de ghnáth.",
            "Litrítear an nath seo mar fhocal amháin nó le fleiscín.",
            "Fadhb leis an bhfleiscíniú");
    addExamplePair(Example.wrong("Tá tú i do chónaí i <marker>mBaile-Átha-Cliath</marker>."),
                   Example.fixed("Tá tú i do chónaí i <marker>mBaile Átha Cliath</marker>."));
  }

  @Override
  public String getId() {
    return "GA_COMPOUNDS";
  }

  @Override
  public String getDescription() {
    return "Focail fhleiscínithe, e.g., Moltar 'ró-úsáid' seachas 'róúsáid'";
  }

  @Override
  protected CompoundRuleData getCompoundRuleData() {
    return compoundData;
  }

}
