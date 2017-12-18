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

import com.opengamma.strata.market.param.ParameterMetadata;
import com.opengamma.strata.market.param.ParameterPerturbation;
import com.opengamma.strata.market.param.UnitParameterSensitivity;

/**
 * A curve formed from two curves, the fixed curve and the spread curve.
 * <p>
 * The spread curve is the primary curve, providing the metadata, parameters, sensitivity and perturbation.
 * The fixed curve only affects the shape of the curve via the {@link #yValue(double)}
 * and {@link #firstDerivative(double)} methods. The fixed curve is not exposed in the parameters.
 */
@BeanDefinition(builderScope = "private")
public final class AddFixedCurve
    implements Curve, ImmutableBean, Serializable {

  /**
   * The fixed curve. Also called base or shape curve.
   */
  @PropertyDefinition(validate = "notNull")
  private final Curve fixedCurve;
  /**
   * The spread curve. Also called the variable curve.
   */
  @PropertyDefinition(validate = "notNull")
  private final Curve spreadCurve;

  //-------------------------------------------------------------------------
  /**
   * Creates a curve as the sum of a fixed curve and a spread curve.
   * 
   * @param fixedCurve  the fixed curve
   * @param spreadCurve  the spread curve
   * @return the curve
   */
  public static AddFixedCurve of(Curve fixedCurve, Curve spreadCurve) {
    return new AddFixedCurve(fixedCurve, spreadCurve);
  }

  //-------------------------------------------------------------------------
  @Override
  public CurveMetadata getMetadata() {
    return spreadCurve.getMetadata();
  }

  @Override
  public AddFixedCurve withMetadata(CurveMetadata metadata) {
    return new AddFixedCurve(fixedCurve, spreadCurve.withMetadata(metadata));
  }

  //-------------------------------------------------------------------------
  @Override
  public int getParameterCount() {
    return spreadCurve.getParameterCount();
  }

  @Override
  public double getParameter(int parameterIndex) {
    return spreadCurve.getParameter(parameterIndex);
  }

  @Override
  public ParameterMetadata getParameterMetadata(int parameterIndex) {
    return spreadCurve.getParameterMetadata(parameterIndex);
  }

  @Override
  public AddFixedCurve withParameter(int parameterIndex, double newValue) {
    return new AddFixedCurve(fixedCurve, spreadCurve.withParameter(parameterIndex, newValue));
  }

  @Override
  public AddFixedCurve withPerturbation(ParameterPerturbation perturbation) {
    return new AddFixedCurve(fixedCurve, spreadCurve.withPerturbation(perturbation));
  }

  //-------------------------------------------------------------------------
  @Override
  public double yValue(double x) {
    return fixedCurve.yValue(x) + spreadCurve.yValue(x);
  }

  @Override
  public UnitParameterSensitivity yValueParameterSensitivity(double x) {
    return spreadCurve.yValueParameterSensitivity(x);
  }

  @Override
  public double firstDerivative(double x) {
    return fixedCurve.firstDerivative(x) + spreadCurve.firstDerivative(x);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code AddFixedCurve}.
   * @return the meta-bean, not null
   */
  public static AddFixedCurve.Meta meta() {
    return AddFixedCurve.Meta.INSTANCE;
  }

  static {
    MetaBean.register(AddFixedCurve.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private AddFixedCurve(
      Curve fixedCurve,
      Curve spreadCurve) {
    JodaBeanUtils.notNull(fixedCurve, "fixedCurve");
    JodaBeanUtils.notNull(spreadCurve, "spreadCurve");
    this.fixedCurve = fixedCurve;
    this.spreadCurve = spreadCurve;
  }

  @Override
  public AddFixedCurve.Meta metaBean() {
    return AddFixedCurve.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the fixed curve. Also called base or shape curve.
   * @return the value of the property, not null
   */
  public Curve getFixedCurve() {
    return fixedCurve;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the spread curve. Also called the variable curve.
   * @return the value of the property, not null
   */
  public Curve getSpreadCurve() {
    return spreadCurve;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      AddFixedCurve other = (AddFixedCurve) obj;
      return JodaBeanUtils.equal(fixedCurve, other.fixedCurve) &&
          JodaBeanUtils.equal(spreadCurve, other.spreadCurve);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(fixedCurve);
    hash = hash * 31 + JodaBeanUtils.hashCode(spreadCurve);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("AddFixedCurve{");
    buf.append("fixedCurve").append('=').append(fixedCurve).append(',').append(' ');
    buf.append("spreadCurve").append('=').append(JodaBeanUtils.toString(spreadCurve));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code AddFixedCurve}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code fixedCurve} property.
     */
    private final MetaProperty<Curve> fixedCurve = DirectMetaProperty.ofImmutable(
        this, "fixedCurve", AddFixedCurve.class, Curve.class);
    /**
     * The meta-property for the {@code spreadCurve} property.
     */
    private final MetaProperty<Curve> spreadCurve = DirectMetaProperty.ofImmutable(
        this, "spreadCurve", AddFixedCurve.class, Curve.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "fixedCurve",
        "spreadCurve");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1682092507:  // fixedCurve
          return fixedCurve;
        case 2130054972:  // spreadCurve
          return spreadCurve;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends AddFixedCurve> builder() {
      return new AddFixedCurve.Builder();
    }

    @Override
    public Class<? extends AddFixedCurve> beanType() {
      return AddFixedCurve.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code fixedCurve} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Curve> fixedCurve() {
      return fixedCurve;
    }

    /**
     * The meta-property for the {@code spreadCurve} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Curve> spreadCurve() {
      return spreadCurve;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1682092507:  // fixedCurve
          return ((AddFixedCurve) bean).getFixedCurve();
        case 2130054972:  // spreadCurve
          return ((AddFixedCurve) bean).getSpreadCurve();
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
   * The bean-builder for {@code AddFixedCurve}.
   */
  private static final class Builder extends DirectPrivateBeanBuilder<AddFixedCurve> {

    private Curve fixedCurve;
    private Curve spreadCurve;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1682092507:  // fixedCurve
          return fixedCurve;
        case 2130054972:  // spreadCurve
          return spreadCurve;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 1682092507:  // fixedCurve
          this.fixedCurve = (Curve) newValue;
          break;
        case 2130054972:  // spreadCurve
          this.spreadCurve = (Curve) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public AddFixedCurve build() {
      return new AddFixedCurve(
          fixedCurve,
          spreadCurve);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("AddFixedCurve.Builder{");
      buf.append("fixedCurve").append('=').append(JodaBeanUtils.toString(fixedCurve)).append(',').append(' ');
      buf.append("spreadCurve").append('=').append(JodaBeanUtils.toString(spreadCurve));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
