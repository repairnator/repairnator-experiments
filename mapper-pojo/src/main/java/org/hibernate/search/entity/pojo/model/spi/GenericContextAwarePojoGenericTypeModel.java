/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.pojo.model.spi;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.hibernate.search.entity.pojo.util.impl.GenericTypeContext;
import org.hibernate.search.entity.pojo.util.impl.ReflectionUtils;

/**
 * An abstract base for implementations of {@link PojoGenericTypeModel} that take advantage of the context
 * in which a given property appears to derive more precise type information.
 * <p>
 * Instances wrap a {@link PojoRawTypeModel}, and propagate generics information to properties
 * and their type by wrapping the property models as well.
 * <p>
 * For instance, given the following model:
 * <pre><code>
 * class A&lt;T extends C&gt; {
 *   GenericType&lt;T&gt; propertyOfA;
 * }
 * class B extends A&lt;D&gt; {
 * }
 * class C {
 * }
 * class D extends C {
 * }
 * class GenericType&lt;T&gt; {
 *   T propertyOfGenericType;
 * }
 * </code></pre>
 *
 * ... if an instance of this implementation was used to model the type of {@code B.propertyOfA},
 * then the property {@code B.propertyOfA} would appear to have type {@code List<D>} as one would expect,
 * instead of type {@code T extends C} if we inferred the type solely based on generics information from type {@code A}.
 *
 * This will also be true for more deeply nested references to a type variable,
 * for instance the type of property {@code B.propertyOfA.propertyOfGenericType} will correctly be inferred as D.
 */
public final class GenericContextAwarePojoGenericTypeModel<T> implements PojoGenericTypeModel<T> {

	private final Helper helper;
	private final PojoRawTypeModel<? super T> rawTypeModel;
	private final GenericTypeContext genericTypeContext;

	private final Map<Object, PojoPropertyModel<?>> genericPropertyCache = new HashMap<>();

	public interface Helper {
		<T> PojoRawTypeModel<T> getRawTypeModel(Class<T> clazz);

		Object getPropertyCacheKey(PojoPropertyModel<?> rawPropertyModel);

		Type getPropertyGenericType(PojoPropertyModel<?> rawPropertyModel);
	}

	public static class RawTypeDeclaringContext<T> {

		private final Helper helper;

		private final GenericTypeContext genericTypeContext;

		public RawTypeDeclaringContext(Helper helper, Class<T> rawType) {
			this.helper = helper;
			this.genericTypeContext = new GenericTypeContext( rawType );
		}

		@SuppressWarnings( "unchecked" ) // The cast is safe by contract, see the called method
		public <U> PojoGenericTypeModel<U> createGenericTypeModel(Class<U> declaredType) {
			return (PojoGenericTypeModel<U>) createGenericTypeModel( (Type) declaredType );
		}

		/**
		 * @param declaredType The type to create a generic type model for
		 * @return A generic type model for {@code declaredType} in this context.
		 * The type parameter can safely be assumed to be exactly the type {@code declaredType}.
		 * For instance if {@code declaredType} is {@code String.class},
		 * the returned type model will be an instance of {@code PojoGenericTypeModel<String>}.
		 * If {@code declaredType} is {@code List<String>}, it will be {@code PojoGenericTypeModel<List<String>>},
		 * and so on.
		 */
		public PojoGenericTypeModel<?> createGenericTypeModel(Type declaredType) {
			/*
			 * The declaring type, even raw, could extend parameterized types in which the given "type" was declared.
			 * Thus we need the declaring context even for property types on raw types,
			 * so that those types are resolved properly.
			 */
			return new GenericContextAwarePojoGenericTypeModel<>(
					helper, new GenericTypeContext( genericTypeContext, declaredType )
			);
		}
	}

	private GenericContextAwarePojoGenericTypeModel(Helper helper, GenericTypeContext genericTypeContext) {
		this.helper = helper;
		this.rawTypeModel = helper.getRawTypeModel(
				(Class<? super T>) ReflectionUtils.getRawType( genericTypeContext.getResolvedType() )
		);
		this.genericTypeContext = genericTypeContext;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + genericTypeContext.getResolvedType().getTypeName() + "]";
	}

	@Override
	public PojoRawTypeModel<? super T> getRawType() {
		return rawTypeModel;
	}

	@Override
	public PojoPropertyModel<?> getProperty(String propertyName) {
		return wrapProperty( rawTypeModel.getProperty( propertyName ) );
	}

	@Override
	public Optional<PojoGenericTypeModel<?>> getTypeArgument(Class<?> rawSuperType, int typeParameterIndex) {
		return genericTypeContext.resolveTypeArgument( rawSuperType, typeParameterIndex )
				.map( type -> new GenericContextAwarePojoGenericTypeModel<>(
						helper, new GenericTypeContext( genericTypeContext.getDeclaringContext(), type )
				) );
	}

	@Override
	public Optional<PojoGenericTypeModel<?>> getArrayElementType() {
		return genericTypeContext.resolveArrayElementType()
				.map( type -> new GenericContextAwarePojoGenericTypeModel<>(
						helper, new GenericTypeContext( genericTypeContext.getDeclaringContext(), type )
				) );
	}

	private <U> PojoPropertyModel<? extends U> wrapProperty(PojoPropertyModel<U> rawPropertyModel) {
		Object cacheKey = helper.getPropertyCacheKey( rawPropertyModel );
		PojoPropertyModel<?> cached = genericPropertyCache.get( cacheKey );
		if ( cached != null ) {
			return (PojoPropertyModel<? extends U>) cached;
		}
		Type propertyType = helper.getPropertyGenericType( rawPropertyModel );
		GenericTypeContext propertyGenericTypeContext = new GenericTypeContext( genericTypeContext, propertyType );
		GenericContextAwarePojoGenericTypeModel<? extends U> genericPropertyTypeModel =
				new GenericContextAwarePojoGenericTypeModel<>( helper, propertyGenericTypeContext );
		PojoPropertyModel<? extends U> propertyModel =
				new GenericContextAwarePojoPropertyModel<>( rawPropertyModel, genericPropertyTypeModel );
		genericPropertyCache.put( cacheKey, propertyModel );
		return propertyModel;
	}

}
