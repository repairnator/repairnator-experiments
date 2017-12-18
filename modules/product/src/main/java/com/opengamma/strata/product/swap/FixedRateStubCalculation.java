/*
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.swap;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.ImmutableValidator;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.joda.beans.impl.direct.DirectPrivateBeanBuilder;

import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.product.rate.FixedRateComputation;
import com.opengamma.strata.product.rate.RateComputation;

/**
 * Defines the rate applicable in the initial or final stub of a fixed swap leg.
 * <p>
 * A standard swap leg consists of a regular periodic schedule and one or two stub periods at each end.
 * This class defines what fixed rate to use during a stub.
 * <p>
 * The calculation may be specified in two ways.
 * <ul>
 * <li>A fixed rate, applicable for the whole stub
 * <li>A fixed amount for the whole stub
 * </ul>
 */
@BeanDefinition(builderScope = "private")
public final class FixedRateStubCalculation
    implements ImmutableBean, Serializable {

  /**
   * An instance that has no special rate handling.
   */
  public static final FixedRateStubCalculation NONE = new FixedRateStubCalculation(null, null);

  /**
   * The fixed rate to use in the stub.
   * A 5% rate will be expressed as 0.05.
   * <p>
   * If the fixed rate is present, then {@code knownAmount} must not be present.
   */
  @PropertyDefinition(get = "optional")
  private final Double fixedRate;
  /**
   * The known amount to pay/receive for the stub.
   * <p>
   * If the known amount is present, then {@code fixedRate} must not be present.
   */
  @PropertyDefinition(get = "optional")
  private final CurrencyAmount knownAmount;

  //-------------------------------------------------------------------------
  /**
   * Obtains an instance with a single fixed rate.
   * 
   * @param fixedRate  the fixed rate for the stub
   * @return the stub
   */
  public static FixedRateStubCalculation ofFixedRate(double fixedRate) {
    return new FixedRateStubCalculation(fixedRate, null);
  }

  /**
   * Obtains an instance with a known amount of interest.
   * 
   * @param knownAmount  the known amount of interest
   * @return the stub
   */
  public static FixedRateStubCalculation ofKnownAmount(CurrencyAmount knownAmount) {
    ArgChecker.notNull(knownAmount, "knownAmount");
    return new FixedRateStubCalculation(null, knownAmount);
  }

  //-------------------------------------------------------------------------
  @ImmutableValidator
  private void validate() {
    if (fixedRate != null && knownAmount != null) {
      throw new IllegalArgumentException("Either rate or amount may be specified, not both");
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Creates the {@code RateComputation} for the stub.
   * 
   * @param defaultRate  the default fixed rate
   * @return the rate computation
   */
  RateComputation createRateComputation(double defaultRate) {
    if (isFixedRate()) {
      return FixedRateComputation.of(fixedRate);
    } else if (isKnownAmount()) {
      return KnownAmountRateComputation.of(knownAmount);
    } else {
      return FixedRateComputation.of(defaultRate);
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Checks if the stub has a fixed rate.
   * 
   * @return true if a fixed rate applies
   */
  public boolean isFixedRate() {
    return fixedRate != null;
  }

  /**
   * Checks if the stub has a known amount.
   * 
   * @return true if the stub has a known amount
   */
  public boolean isKnownAmount() {
    return knownAmount != null;
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code FixedRateStubCalculation}.
   * @return the meta-bean, not null
   */
  public static FixedRateStubCalculation.Meta meta() {
    return FixedRateStubCalculation.Meta.INSTANCE;
  }

  static {
    MetaBean.register(FixedRateStubCalculation.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private FixedRateStubCalculation(
      Double fixedRate,
      CurrencyAmount knownAmount) {
    this.fixedRate = fixedRate;
    this.knownAmount = knownAmount;
    validate();
  }

  @Override
  public FixedRateStubCalculation.Meta metaBean() {
    return FixedRateStubCalculation.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the fixed rate to use in the stub.
   * A 5% rate will be expressed as 0.05.
   * <p>
   * If the fixed rate is present, then {@code knownAmount} must not be present.
   * @return the optional value of the property, not null
   */
  public OptionalDouble getFixedRate() {
    return fixedRate != null ? OptionalDouble.of(fixedRate) : OptionalDouble.empty();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the known amount to pay/receive for the stub.
   * <p>
   * If the known amount is present, then {@code fixedRate} must not be present.
   * @return the optional value of the property, not null
   */
  public Optional<CurrencyAmount> getKnownAmount() {
    return Optional.ofNullable(knownAmount);
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FixedRateStubCalculation other = (FixedRateStubCalculation) obj;
      return JodaBeanUtils.equal(fixedRate, other.fixedRate) &&
          JodaBeanUtils.equal(knownAmount, other.knownAmount);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(fixedRate);
    hash = hash * 31 + JodaBeanUtils.hashCode(knownAmount);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("FixedRateStubCalculation{");
    buf.append("fixedRate").append('=').append(fixedRate).append(',').append(' ');
    buf.append("knownAmount").append('=').append(JodaBeanUtils.toString(knownAmount));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FixedRateStubCalculation}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code fixedRate} property.
     */
    private final MetaProperty<Double> fixedRate = DirectMetaProperty.ofImmutable(
        this, "fixedRate", FixedRateStubCalculation.class, Double.class);
    /**
     * The meta-property for the {@code knownAmount} property.
     */
    private final MetaProperty<CurrencyAmount> knownAmount = DirectMetaProperty.ofImmutable(
        this, "knownAmount", FixedRateStubCalculation.class, CurrencyAmount.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "fixedRate",
        "knownAmount");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 747425396:  // fixedRate
          return fixedRate;
        case -158727813:  // knownAmount
          return knownAmount;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends FixedRateStubCalculation> builder() {
      return new FixedRateStubCalculation.Builder();
    }

    @Override
    public Class<? extends FixedRateStubCalculation> beanType() {
      return FixedRateStubCalculation.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code fixedRate} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> fixedRate() {
      return fixedRate;
    }

    /**
     * The meta-property for the {@code knownAmount} property.
     * @return the meta-property, not null
     */
    public MetaProperty<CurrencyAmount> knownAmount() {
      return knownAmount;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 747425396:  // fixedRate
          return ((FixedRateStubCalculation) bean).fixedRate;
        case -158727813:  // knownAmount
          return ((FixedRateStubCalculation) bean).knownAmount;
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
   * The bean-builder for {@code FixedRateStubCalculation}.
   */
  private static final class Builder extends DirectPrivateBeanBuilder<FixedRateStubCalculation> {

    private Double fixedRate;
    private CurrencyAmount knownAmount;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 747425396:  // fixedRate
          return fixedRate;
        case -158727813:  // knownAmount
          return knownAmount;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 747425396:  // fixedRate
          this.fixedRate = (Double) newValue;
          break;
        case -158727813:  // knownAmount
          this.knownAmount = (CurrencyAmount) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public FixedRateStubCalculation build() {
      return new FixedRateStubCalculation(
          fixedRate,
          knownAmount);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("FixedRateStubCalculation.Builder{");
      buf.append("fixedRate").append('=').append(JodaBeanUtils.toString(fixedRate)).append(',').append(' ');
      buf.append("knownAmount").append('=').append(JodaBeanUtils.toString(knownAmount));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
