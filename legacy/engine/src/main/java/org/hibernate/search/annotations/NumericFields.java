/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Numeric extension for Fields annotation
 *
 * @author Gustavo Fernandes
 */
@Retention( RetentionPolicy.RUNTIME )
@Target({ ElementType.METHOD, ElementType.FIELD })
@Documented
public @interface NumericFields {

	NumericField[] value();
}
