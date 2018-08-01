/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.pojo.model.spi;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Member;

import org.hibernate.search.util.SearchException;

/**
 * @author Yoann Rodiere
 */
public final class MemberPropertyHandle implements PropertyHandle {

	private final String name;
	private final Member member;
	private final MethodHandle getter;

	public MemberPropertyHandle(String name, Member member, MethodHandle getter) {
		this.name = name;
		this.member = member;
		this.getter = getter;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + member + "]";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object get(Object thiz) {
		try {
			return getter.invoke( thiz );
		}
		catch (Error e) {
			throw e;
		}
		catch (Throwable e) {
			if ( e instanceof InterruptedException ) {
				Thread.currentThread().interrupt();
			}
			throw new SearchException( "Exception while invoking '" + member + "' on '" + thiz + "'" , e );
		}
	}

	@Override
	public int hashCode() {
		return member.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ( obj == null || !obj.getClass().equals( getClass() ) ) {
			return false;
		}
		MemberPropertyHandle other = (MemberPropertyHandle) obj;
		return name.equals( other.name ) && member.equals( other.member );
	}

}
