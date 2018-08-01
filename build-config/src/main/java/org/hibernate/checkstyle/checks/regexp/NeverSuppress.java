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
 * An implementation of {@link MatchSuppressor} that never suppresses a
 * match.
 *
 * @author oliver
 */
public final class NeverSuppress implements MatchSuppressor {
	/**
	 * The shared instance.
	 */
	public static final MatchSuppressor INSTANCE = new NeverSuppress();

	/**
	 * Stop creation of instances.
	 */
	private NeverSuppress() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean shouldSuppress(int aStartLineNo, int aStartColNo,
			int aEndLineNo, int aEndColNo) {
		return false;
	}
}



