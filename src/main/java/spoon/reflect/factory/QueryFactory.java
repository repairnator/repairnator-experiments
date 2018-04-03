/**
 * Copyright (C) 2006-2017 INRIA and contributors
 * Spoon - http://spoon.gforge.inria.fr/
 *
 * This software is governed by the CeCILL-C License under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-C license as
 * circulated by CEA, CNRS and INRIA at http://www.cecill.info.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the CeCILL-C License for more details.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C license and that you accept its terms.
 */
package spoon.reflect.factory;

import spoon.reflect.visitor.chain.CtQuery;
import spoon.reflect.visitor.chain.CtQueryImpl;

/**
 * A factory to create some queries on the Spoon metamodel.
 */
public class QueryFactory extends SubFactory {

	/**
	 * Creates the evaluation factory.
	 */
	public QueryFactory(Factory factory) {
		super(factory);
	}

	/**
	 * Creates a unbound query. Use {@link CtQuery#setInput(Object...)}
	 * before {@link CtQuery#forEach(spoon.reflect.visitor.chain.CtConsumer)}
	 * or {@link CtQuery#list()} is called
	 */
	public CtQuery createQuery() {
		return new CtQueryImpl();
	}

	/**
	 * Creates a bound query. Use directly
	 * {@link CtQuery#forEach(spoon.reflect.visitor.chain.CtConsumer)}
	 * or {@link CtQuery#list()} to evaluate the query
	 */
	public CtQuery createQuery(Object input) {
		return new CtQueryImpl(input);
	}

	/**
	 * Creates a bound query. Use directly
	 * {@link CtQuery#forEach(spoon.reflect.visitor.chain.CtConsumer)}
	 * or {@link CtQuery#list()} to evaluate the query
	 */
	public CtQuery createQuery(Iterable<?> inputs) {
		return new CtQueryImpl().addInput(inputs);
	}

	/**
	 * Creates a bound query with an optional number
	 * of inputs elements to the query (see {@link CtQuery#setInput(Object...)})
	 */
	public CtQuery createQuery(Object... input) {
		return new CtQueryImpl(input);
	}
}
