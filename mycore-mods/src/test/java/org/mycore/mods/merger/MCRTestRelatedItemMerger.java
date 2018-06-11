/*
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * MyCoRe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyCoRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyCoRe.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mycore.mods.merger;

import org.junit.Test;
import org.mycore.common.MCRTestCase;

public class MCRTestRelatedItemMerger extends MCRTestCase {

    @Test
    public void testMergeHost() throws Exception {
        String a = "[mods:relatedItem[@type='host'][mods:identifier='foo']]";
        String b = "[mods:relatedItem[@type='host'][mods:note='bar']]";
        String e = "[mods:relatedItem[@type='host'][mods:identifier='foo'][mods:note='bar']]";
        MCRTestMerger.test(a, b, e);
    }

    @Test
    public void testMergeSeries() throws Exception {
        String a = "[mods:relatedItem[@type='series'][mods:identifier='foo']]";
        String b = "[mods:relatedItem[@type='series'][mods:note='bar']]";
        String e = "[mods:relatedItem[@type='series'][mods:identifier='foo']]"
            + "[mods:relatedItem[@type='series'][mods:note='bar']]";
        MCRTestMerger.test(a, b, e);
    }
}
