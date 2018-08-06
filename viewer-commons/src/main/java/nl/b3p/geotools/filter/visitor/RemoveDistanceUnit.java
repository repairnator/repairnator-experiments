/*
 * Copyright (C) 2012-2013 B3Partners B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.geotools.filter.visitor;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.DWithin;

/**
 *
 * @author Matthijs Laan
 */
public class RemoveDistanceUnit extends DuplicatingFilterVisitor {
    @Override
    public Object visit(DWithin filter, Object data) {
        return CommonFactoryFinder.getFilterFactory2().dwithin(
            filter.getExpression1(), 
            filter.getExpression2(),
            filter.getDistance(),
            null, // fix Oracle does not know "meters" 
            filter.getMatchAction());
    }    
    
    @Override
    public Object visit(Beyond filter, Object data) {
        return CommonFactoryFinder.getFilterFactory2().beyond(
            filter.getExpression1(), 
            filter.getExpression2(),
            filter.getDistance(),
            null, // fix Oracle does not know "meters" 
            filter.getMatchAction());
    }    
}
