/*
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.market.option;

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

import com.opengamma.strata.collect.ArgChecker;

/**
 * A strike based on moneyness.
 * <p>
 * The moneyness is defined as {@code strike/forward}.
 * The strike should be nonnegative and the forward should be strictly positive.
 */
@BeanDefinition(builderScope = "private")
public final class MoneynessStrike
    implements Strike, ImmutableBean, Serializable {

  /**
   * The value of moneyness.
   */
  @PropertyDefinition(validate = "ArgChecker.notNegative", overrideGet = true)
  private final double value;

  /**
   * Obtains an instance of {@code Moneyness} with the value of moneyness.
   * 
   * @param moneyness  the value of moneyness, not negative
   * @return the instance
   */
  public static MoneynessStrike of(double moneyness) {
    return new MoneynessStrike(moneyness);
  }

  /**
   * Obtains an instance of {@code Moneyness} from the strike and forward.
   * <p>
   * The moneyness is defined as {@code strike/forward}.
   * 
   * @param strike  the strike, not negative
   * @param forward  the forward, not negative
   * @return the instance
   */
  public static MoneynessStrike ofStrikeAndForward(double strike, double forward) {
    return of(ArgChecker.notNegativeOrZero(strike, "strike") / ArgChecker.notNegative(forward, "forward"));
  }

  //-------------------------------------------------------------------------
  @Override
  public StrikeType getType() {
    return StrikeType.MONEYNESS;
  }

  @Override
  public MoneynessStrike withValue(double value) {
    return new MoneynessStrike(value);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code MoneynessStrike}.
   * @return the meta-bean, not null
   */
  public static MoneynessStrike.Meta meta() {
    return MoneynessStrike.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(MoneynessStrike.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private MoneynessStrike(
      double value) {
    ArgChecker.notNegative(value, "value");
    this.value = value;
  }

  @Override
  public MoneynessStrike.Meta metaBean() {
    return MoneynessStrike.Meta.INSTANCE;
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
   * Gets the value of moneyness.
   * @return the value of the property
   */
  @Override
  public double getValue() {
    return value;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      MoneynessStrike other = (MoneynessStrike) obj;
      return JodaBeanUtils.equal(value, other.value);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(value);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("MoneynessStrike{");
    buf.append("value").append('=').append(JodaBeanUtils.toString(value));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MoneynessStrike}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code value} property.
     */
    private final MetaProperty<Double> value = DirectMetaProperty.ofImmutable(
        this, "value", MoneynessStrike.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "value");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 111972721:  // value
          return value;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends MoneynessStrike> builder() {
      return new MoneynessStrike.Builder();
    }

    @Override
    public Class<? extends MoneynessStrike> beanType() {
      return MoneynessStrike.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code value} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> value() {
      return value;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 111972721:  // value
          return ((MoneynessStrike) bean).getValue();
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
   * The bean-builder for {@code MoneynessStrike}.
   */
  private static final class Builder extends DirectPrivateBeanBuilder<MoneynessStrike> {

    private double value;

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
        case 111972721:  // value
          return value;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 111972721:  // value
          this.value = (Double) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public MoneynessStrike build() {
      return new MoneynessStrike(
          value);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(64);
      buf.append("MoneynessStrike.Builder{");
      buf.append("value").append('=').append(JodaBeanUtils.toString(value));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
