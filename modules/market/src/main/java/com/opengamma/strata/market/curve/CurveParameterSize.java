/*
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.market.curve;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.joda.beans.impl.direct.DirectPrivateBeanBuilder;

import com.opengamma.strata.collect.ArgChecker;

/**
 * The curve name and number of parameters.
 * <p>
 * This holds the curve name and the number of parameters that define it.
 */
@BeanDefinition(builderScope = "private")
public final class CurveParameterSize
    implements ImmutableBean, Serializable {

  /**
   * The curve name.
   */
  @PropertyDefinition(validate = "notNull")
  private final CurveName name;
  /**
   * The number of parameters.
   */
  @PropertyDefinition(validate = "ArgChecker.notNegativeOrZero")
  private final int parameterCount;

  //-------------------------------------------------------------------------
  /**
   * Obtains an instance, specifying the name and parameter count.
   * 
   * @param name  the curve name
   * @param parameterCount  the parameter count
   * @return the curve data
   */
  public static CurveParameterSize of(CurveName name, int parameterCount) {
    return new CurveParameterSize(name, parameterCount);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code CurveParameterSize}.
   * @return the meta-bean, not null
   */
  public static CurveParameterSize.Meta meta() {
    return CurveParameterSize.Meta.INSTANCE;
  }

  static {
    MetaBean.register(CurveParameterSize.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private CurveParameterSize(
      CurveName name,
      int parameterCount) {
    JodaBeanUtils.notNull(name, "name");
    ArgChecker.notNegativeOrZero(parameterCount, "parameterCount");
    this.name = name;
    this.parameterCount = parameterCount;
  }

  @Override
  public CurveParameterSize.Meta metaBean() {
    return CurveParameterSize.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the curve name.
   * @return the value of the property, not null
   */
  public CurveName getName() {
    return name;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the number of parameters.
   * @return the value of the property
   */
  public int getParameterCount() {
    return parameterCount;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CurveParameterSize other = (CurveParameterSize) obj;
      return JodaBeanUtils.equal(name, other.name) &&
          (parameterCount == other.parameterCount);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(name);
    hash = hash * 31 + JodaBeanUtils.hashCode(parameterCount);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("CurveParameterSize{");
    buf.append("name").append('=').append(name).append(',').append(' ');
    buf.append("parameterCount").append('=').append(JodaBeanUtils.toString(parameterCount));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CurveParameterSize}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<CurveName> name = DirectMetaProperty.ofImmutable(
        this, "name", CurveParameterSize.class, CurveName.class);
    /**
     * The meta-property for the {@code parameterCount} property.
     */
    private final MetaProperty<Integer> parameterCount = DirectMetaProperty.ofImmutable(
        this, "parameterCount", CurveParameterSize.class, Integer.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "name",
        "parameterCount");

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
        case 1107332838:  // parameterCount
          return parameterCount;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends CurveParameterSize> builder() {
      return new CurveParameterSize.Builder();
    }

    @Override
    public Class<? extends CurveParameterSize> beanType() {
      return CurveParameterSize.class;
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
    public MetaProperty<CurveName> name() {
      return name;
    }

    /**
     * The meta-property for the {@code parameterCount} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Integer> parameterCount() {
      return parameterCount;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return ((CurveParameterSize) bean).getName();
        case 1107332838:  // parameterCount
          return ((CurveParameterSize) bean).getParameterCount();
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
   * The bean-builder for {@code CurveParameterSize}.
   */
  private static final class Builder extends DirectPrivateBeanBuilder<CurveParameterSize> {

    private CurveName name;
    private int parameterCount;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return name;
        case 1107332838:  // parameterCount
          return parameterCount;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          this.name = (CurveName) newValue;
          break;
        case 1107332838:  // parameterCount
          this.parameterCount = (Integer) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public CurveParameterSize build() {
      return new CurveParameterSize(
          name,
          parameterCount);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("CurveParameterSize.Builder{");
      buf.append("name").append('=').append(JodaBeanUtils.toString(name)).append(',').append(' ');
      buf.append("parameterCount").append('=').append(JodaBeanUtils.toString(parameterCount));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
