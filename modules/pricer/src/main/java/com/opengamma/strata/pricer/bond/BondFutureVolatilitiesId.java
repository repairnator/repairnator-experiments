/*
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.pricer.bond;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.joda.beans.impl.direct.DirectPrivateBeanBuilder;

import com.opengamma.strata.data.MarketDataName;
import com.opengamma.strata.data.NamedMarketDataId;

/**
 * An identifier used to access bond future volatilities by name.
 * <p>
 * This is used when there is a need to obtain an instance of {@link BondFutureVolatilities}.
 */
@BeanDefinition(builderScope = "private", cacheHashCode = true)
public final class BondFutureVolatilitiesId
    implements NamedMarketDataId<BondFutureVolatilities>, ImmutableBean, Serializable {

  /**
   * The name of the volatilities.
   */
  @PropertyDefinition(validate = "notNull")
  private final BondFutureVolatilitiesName name;

  //-------------------------------------------------------------------------
  /**
   * Obtains an identifier used to find bond future volatilities.
   *
   * @param name  the name
   * @return an identifier for the volatilities
   */
  public static BondFutureVolatilitiesId of(String name) {
    return new BondFutureVolatilitiesId(BondFutureVolatilitiesName.of(name));
  }

  /**
   * Obtains an identifier used to find bond future volatilities.
   *
   * @param name  the name
   * @return an identifier for the volatilities
   */
  public static BondFutureVolatilitiesId of(BondFutureVolatilitiesName name) {
    return new BondFutureVolatilitiesId(name);
  }

  //-------------------------------------------------------------------------
  @Override
  public Class<BondFutureVolatilities> getMarketDataType() {
    return BondFutureVolatilities.class;
  }

  @Override
  public MarketDataName<BondFutureVolatilities> getMarketDataName() {
    return name;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code BondFutureVolatilitiesId}.
   * @return the meta-bean, not null
   */
  public static BondFutureVolatilitiesId.Meta meta() {
    return BondFutureVolatilitiesId.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(BondFutureVolatilitiesId.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The cached hash code, using the racy single-check idiom.
   */
  private int cachedHashCode;

  private BondFutureVolatilitiesId(
      BondFutureVolatilitiesName name) {
    JodaBeanUtils.notNull(name, "name");
    this.name = name;
  }

  @Override
  public BondFutureVolatilitiesId.Meta metaBean() {
    return BondFutureVolatilitiesId.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the name of the volatilities.
   * @return the value of the property, not null
   */
  public BondFutureVolatilitiesName getName() {
    return name;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      BondFutureVolatilitiesId other = (BondFutureVolatilitiesId) obj;
      return JodaBeanUtils.equal(name, other.name);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = cachedHashCode;
    if (hash == 0) {
      hash = getClass().hashCode();
      hash = hash * 31 + JodaBeanUtils.hashCode(name);
      cachedHashCode = hash;
    }
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("BondFutureVolatilitiesId{");
    buf.append("name").append('=').append(JodaBeanUtils.toString(name));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code BondFutureVolatilitiesId}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<BondFutureVolatilitiesName> name = DirectMetaProperty.ofImmutable(
        this, "name", BondFutureVolatilitiesId.class, BondFutureVolatilitiesName.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "name");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return name;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends BondFutureVolatilitiesId> builder() {
      return new BondFutureVolatilitiesId.Builder();
    }

    @Override
    public Class<? extends BondFutureVolatilitiesId> beanType() {
      return BondFutureVolatilitiesId.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public MetaProperty<BondFutureVolatilitiesName> name() {
      return name;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return ((BondFutureVolatilitiesId) bean).getName();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code BondFutureVolatilitiesId}.
   */
  private static final class Builder extends DirectPrivateBeanBuilder<BondFutureVolatilitiesId> {

    private BondFutureVolatilitiesName name;

    /**
     * Restricted constructor.
     */
    private Builder() {
      super(meta());
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return name;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          this.name = (BondFutureVolatilitiesName) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public BondFutureVolatilitiesId build() {
      return new BondFutureVolatilitiesId(
          name);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(64);
      buf.append("BondFutureVolatilitiesId.Builder{");
      buf.append("name").append('=').append(JodaBeanUtils.toString(name));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
