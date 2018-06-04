// code originates from Checkstyle and has been copied since it is package private
////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2012  Oliver Burn
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////
package org.hibernate.checkstyle.checks.regexp;

/**
 * Represents a suppressor for matches.
 *
 * @author oliver
 */
interface MatchSuppressor {
	/**
	 * Checks if the specified selection should be suppressed.
	 *
	 * @param aStartLineNo the starting line number
	 * @param aStartColNo the starting column number
	 * @param aEndLineNo the ending line number
	 * @param aEndColNo the ending column number
	 *
	 * @return true if the positions intersects with a comment.
	 */
	boolean shouldSuppress(int aStartLineNo, int aStartColNo, int aEndLineNo,
			int aEndColNo);
}


