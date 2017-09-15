/*
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.cms;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableValidator;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.joda.beans.impl.direct.DirectPrivateBeanBuilder;

import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.product.ResolvedProduct;
import com.opengamma.strata.product.swap.ResolvedSwapLeg;

/**
 * A constant maturity swap (CMS) or CMS cap/floor, resolved for pricing.
 * <p>
 * This is the resolved form of {@link Cms} and is an input to the pricers.
 * Applications will typically create a {@code ResolvedCms} from a {@code Cms}
 * using {@link Cms#resolve(ReferenceData)}.
 * <p>
 * A {@code ResolvedCms} is bound to data that changes over time, such as holiday calendars.
 * If the data changes, such as the addition of a new holiday, the resolved form will not be updated.
 * Care must be taken when placing the resolved form in a cache or persistence layer.
 */
@BeanDefinition(builderScope = "private")
public final class ResolvedCms
    implements ResolvedProduct, ImmutableBean, Serializable {

  /**
   * The CMS leg of the product.
   * <p>
   * This is associated with periodic payments based on swap rate.
   * The payments are CMS coupons, CMS caplets or CMS floors.
   */
  @PropertyDefinition(validate = "notNull")
  private final ResolvedCmsLeg cmsLeg;
  /**
   * The optional pay leg of the product.
   * <p>
   * Typically this is associated with periodic fixed or Ibor rate payments without compounding or notional exchange.
   * <p>
   * These periodic payments are not made for certain CMS products. Instead the premium is paid upfront.
   */
  @PropertyDefinition(get = "optional")
  private final ResolvedSwapLeg payLeg;

  //-------------------------------------------------------------------------
  /**
   * Obtains an instance from a CMS leg with no pay leg.
   * <p>
   * The pay leg is absent in the resulting CMS.
   * 
   * @param cmsLeg  the CMS leg
   * @return the CMS
   */
  public static ResolvedCms of(ResolvedCmsLeg cmsLeg) {
    return new ResolvedCms(cmsLeg, null);
  }

  /**
   * Obtains an instance from a CMS leg and a pay leg.
   * 
   * @param cmsLeg  the CMS leg
   * @param payLeg  the pay leg
   * @return the CMS
   */
  public static ResolvedCms of(ResolvedCmsLeg cmsLeg, ResolvedSwapLeg payLeg) {
    ArgChecker.notNull(cmsLeg, "cmsLeg");
    ArgChecker.notNull(payLeg, "payLeg");
    return new ResolvedCms(cmsLeg, payLeg);
  }

  //-------------------------------------------------------------------------
  @ImmutableValidator
  private void validate() {
    if (getPayLeg().isPresent()) {
      ArgChecker.isFalse(
          payLeg.getPayReceive().equals(cmsLeg.getPayReceive()),
          "Two legs should have different Pay/Receive flags");
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Returns the set of currencies referred to by the CMS.
   * <p>
   * This returns the complete set of payment currencies for the CMS.
   * This will typically return one currency, but could return two.
   * 
   * @return the set of payment currencies referred to by this swap
   */
  public ImmutableSet<Currency> allPaymentCurrencies() {
    if (payLeg == null) {
      return ImmutableSet.of(cmsLeg.getCurrency());
    }
    return ImmutableSet.of(cmsLeg.getCurrency(), payLeg.getCurrency());
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ResolvedCms}.
   * @return the meta-bean, not null
   */
  public static ResolvedCms.Meta meta() {
    return ResolvedCms.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ResolvedCms.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private ResolvedCms(
      ResolvedCmsLeg cmsLeg,
      ResolvedSwapLeg payLeg) {
    JodaBeanUtils.notNull(cmsLeg, "cmsLeg");
    this.cmsLeg = cmsLeg;
    this.payLeg = payLeg;
    validate();
  }

  @Override
  public ResolvedCms.Meta metaBean() {
    return ResolvedCms.Meta.INSTANCE;
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
   * Gets the CMS leg of the product.
   * <p>
   * This is associated with periodic payments based on swap rate.
   * The payments are CMS coupons, CMS caplets or CMS floors.
   * @return the value of the property, not null
   */
  public ResolvedCmsLeg getCmsLeg() {
    return cmsLeg;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the optional pay leg of the product.
   * <p>
   * Typically this is associated with periodic fixed or Ibor rate payments without compounding or notional exchange.
   * <p>
   * These periodic payments are not made for certain CMS products. Instead the premium is paid upfront.
   * @return the optional value of the property, not null
   */
  public Optional<ResolvedSwapLeg> getPayLeg() {
    return Optional.ofNullable(payLeg);
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ResolvedCms other = (ResolvedCms) obj;
      return JodaBeanUtils.equal(cmsLeg, other.cmsLeg) &&
          JodaBeanUtils.equal(payLeg, other.payLeg);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(cmsLeg);
    hash = hash * 31 + JodaBeanUtils.hashCode(payLeg);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("ResolvedCms{");
    buf.append("cmsLeg").append('=').append(cmsLeg).append(',').append(' ');
    buf.append("payLeg").append('=').append(JodaBeanUtils.toString(payLeg));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ResolvedCms}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code cmsLeg} property.
     */
    private final MetaProperty<ResolvedCmsLeg> cmsLeg = DirectMetaProperty.ofImmutable(
        this, "cmsLeg", ResolvedCms.class, ResolvedCmsLeg.class);
    /**
     * The meta-property for the {@code payLeg} property.
     */
    private final MetaProperty<ResolvedSwapLeg> payLeg = DirectMetaProperty.ofImmutable(
        this, "payLeg", ResolvedCms.class, ResolvedSwapLeg.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "cmsLeg",
        "payLeg");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1356515323:  // cmsLeg
          return cmsLeg;
        case -995239866:  // payLeg
          return payLeg;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends ResolvedCms> builder() {
      return new ResolvedCms.Builder();
    }

    @Override
    public Class<? extends ResolvedCms> beanType() {
      return ResolvedCms.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code cmsLeg} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ResolvedCmsLeg> cmsLeg() {
      return cmsLeg;
    }

    /**
     * The meta-property for the {@code payLeg} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ResolvedSwapLeg> payLeg() {
      return payLeg;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1356515323:  // cmsLeg
          return ((ResolvedCms) bean).getCmsLeg();
        case -995239866:  // payLeg
          return ((ResolvedCms) bean).payLeg;
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
   * The bean-builder for {@code ResolvedCms}.
   */
  private static final class Builder extends DirectPrivateBeanBuilder<ResolvedCms> {

    private ResolvedCmsLeg cmsLeg;
    private ResolvedSwapLeg payLeg;

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
        case -1356515323:  // cmsLeg
          return cmsLeg;
        case -995239866:  // payLeg
          return payLeg;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -1356515323:  // cmsLeg
          this.cmsLeg = (ResolvedCmsLeg) newValue;
          break;
        case -995239866:  // payLeg
          this.payLeg = (ResolvedSwapLeg) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public ResolvedCms build() {
      return new ResolvedCms(
          cmsLeg,
          payLeg);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("ResolvedCms.Builder{");
      buf.append("cmsLeg").append('=').append(JodaBeanUtils.toString(cmsLeg)).append(',').append(' ');
      buf.append("payLeg").append('=').append(JodaBeanUtils.toString(payLeg));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
