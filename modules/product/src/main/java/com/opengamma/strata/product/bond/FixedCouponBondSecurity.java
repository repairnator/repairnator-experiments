/*
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.bond;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.ImmutableDefaults;
import org.joda.beans.gen.ImmutableValidator;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.StandardId;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.date.DayCount;
import com.opengamma.strata.basics.date.DaysAdjustment;
import com.opengamma.strata.basics.schedule.PeriodicSchedule;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.product.SecurityId;
import com.opengamma.strata.product.SecurityInfo;
import com.opengamma.strata.product.TradeInfo;

/**
 * A security representing a fixed coupon bond.
 * <p>
 * A fixed coupon bond is a financial instrument that represents a stream of fixed payments.
 * 
 * <h4>Price</h4>
 * Strata uses <i>decimal prices</i> for bonds in the trade model, pricers and market data.
 * For example, a price of 99.32% is represented in Strata by 0.9932.
 */
@BeanDefinition
public final class FixedCouponBondSecurity
    implements LegalEntitySecurity, ImmutableBean, Serializable {

  /**
   * The standard security information.
   * <p>
   * This includes the security identifier.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final SecurityInfo info;
  /**
   * The currency that the bond is traded in.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final Currency currency;
  /**
   * The notional amount, must be positive.
   * <p>
   * The notional expressed here must be positive.
   * The currency of the notional is specified by {@code currency}.
   */
  @PropertyDefinition(validate = "ArgChecker.notNegativeOrZero")
  private final double notional;
  /**
   * The accrual schedule.
   * <p>
   * This is used to define the accrual periods.
   * These are used directly or indirectly to determine other dates in the product.
   */
  @PropertyDefinition(validate = "notNull")
  private final PeriodicSchedule accrualSchedule;
  /**
   * The fixed coupon rate.
   * <p>
   * The periodic payments are based on this fixed coupon rate.
   */
  @PropertyDefinition
  private final double fixedRate;
  /**
   * The day count convention applicable.
   * <p>
   * The conversion from dates to a numerical value is made based on this day count.
   * For the fixed bond, the day count convention is used to compute accrued interest.
   * <p>
   * Note that the year fraction of a coupon payment is computed based on the unadjusted
   * dates in the schedule.
   */
  @PropertyDefinition(validate = "notNull")
  private final DayCount dayCount;
  /**
   * Yield convention.
   * <p>
   * The convention defines how to convert from yield to price and inversely.
   */
  @PropertyDefinition(validate = "notNull")
  private final FixedCouponBondYieldConvention yieldConvention;
  /**
   * The legal entity identifier.
   * <p>
   * This identifier is used for the legal entity that issues the bond.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final StandardId legalEntityId;
  /**
   * The number of days between valuation date and settlement date.
   * <p>
   * This is used to compute clean price.
   * The clean price is the relative price to be paid at the standard settlement date in exchange for the bond.
   * <p>
   * It is usually one business day for US treasuries and UK Gilts and three days for Euroland government bonds.
   */
  @PropertyDefinition(validate = "notNull")
  private final DaysAdjustment settlementDateOffset;
  /**
   * Ex-coupon period.
   * <p>
   * Some bonds trade ex-coupons before the coupon payment. The coupon is paid not to the
   * owner of the bond on the payment date but to the owner of the bond on the detachment date.
   * The difference between the two is the ex-coupon period (measured in days).
   * <p>
   * Because the detachment date is not after the coupon date, the number of days
   * stored in this field should be zero or negative.
   */
  @PropertyDefinition(validate = "notNull")
  private final DaysAdjustment exCouponPeriod;

  //-------------------------------------------------------------------------
  @ImmutableDefaults
  private static void applyDefaults(Builder builder) {
    builder.exCouponPeriod = DaysAdjustment.NONE;
  }

  @ImmutableValidator
  private void validate() {
    ArgChecker.isTrue(settlementDateOffset.getDays() >= 0, "The settlement date offset must be non-negative");
    ArgChecker.isTrue(exCouponPeriod.getDays() <= 0,
        "The ex-coupon period is measured from the payment date, thus the days must be non-positive");
  }

  //-------------------------------------------------------------------------
  @Override
  public ImmutableSet<SecurityId> getUnderlyingIds() {
    return ImmutableSet.of();
  }

  //-------------------------------------------------------------------------
  @Override
  public FixedCouponBond createProduct(ReferenceData refData) {
    return new FixedCouponBond(
        getSecurityId(),
        currency,
        notional,
        accrualSchedule,
        fixedRate,
        dayCount,
        yieldConvention,
        legalEntityId,
        settlementDateOffset,
        exCouponPeriod);
  }

  @Override
  public FixedCouponBondTrade createTrade(TradeInfo info, double quantity, double tradePrice, ReferenceData refData) {
    return new FixedCouponBondTrade(info, createProduct(refData), quantity, tradePrice);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code FixedCouponBondSecurity}.
   * @return the meta-bean, not null
   */
  public static FixedCouponBondSecurity.Meta meta() {
    return FixedCouponBondSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(FixedCouponBondSecurity.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static FixedCouponBondSecurity.Builder builder() {
    return new FixedCouponBondSecurity.Builder();
  }

  private FixedCouponBondSecurity(
      SecurityInfo info,
      Currency currency,
      double notional,
      PeriodicSchedule accrualSchedule,
      double fixedRate,
      DayCount dayCount,
      FixedCouponBondYieldConvention yieldConvention,
      StandardId legalEntityId,
      DaysAdjustment settlementDateOffset,
      DaysAdjustment exCouponPeriod) {
    JodaBeanUtils.notNull(info, "info");
    JodaBeanUtils.notNull(currency, "currency");
    ArgChecker.notNegativeOrZero(notional, "notional");
    JodaBeanUtils.notNull(accrualSchedule, "accrualSchedule");
    JodaBeanUtils.notNull(dayCount, "dayCount");
    JodaBeanUtils.notNull(yieldConvention, "yieldConvention");
    JodaBeanUtils.notNull(legalEntityId, "legalEntityId");
    JodaBeanUtils.notNull(settlementDateOffset, "settlementDateOffset");
    JodaBeanUtils.notNull(exCouponPeriod, "exCouponPeriod");
    this.info = info;
    this.currency = currency;
    this.notional = notional;
    this.accrualSchedule = accrualSchedule;
    this.fixedRate = fixedRate;
    this.dayCount = dayCount;
    this.yieldConvention = yieldConvention;
    this.legalEntityId = legalEntityId;
    this.settlementDateOffset = settlementDateOffset;
    this.exCouponPeriod = exCouponPeriod;
    validate();
  }

  @Override
  public FixedCouponBondSecurity.Meta metaBean() {
    return FixedCouponBondSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the standard security information.
   * <p>
   * This includes the security identifier.
   * @return the value of the property, not null
   */
  @Override
  public SecurityInfo getInfo() {
    return info;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency that the bond is traded in.
   * @return the value of the property, not null
   */
  @Override
  public Currency getCurrency() {
    return currency;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the notional amount, must be positive.
   * <p>
   * The notional expressed here must be positive.
   * The currency of the notional is specified by {@code currency}.
   * @return the value of the property
   */
  public double getNotional() {
    return notional;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the accrual schedule.
   * <p>
   * This is used to define the accrual periods.
   * These are used directly or indirectly to determine other dates in the product.
   * @return the value of the property, not null
   */
  public PeriodicSchedule getAccrualSchedule() {
    return accrualSchedule;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the fixed coupon rate.
   * <p>
   * The periodic payments are based on this fixed coupon rate.
   * @return the value of the property
   */
  public double getFixedRate() {
    return fixedRate;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the day count convention applicable.
   * <p>
   * The conversion from dates to a numerical value is made based on this day count.
   * For the fixed bond, the day count convention is used to compute accrued interest.
   * <p>
   * Note that the year fraction of a coupon payment is computed based on the unadjusted
   * dates in the schedule.
   * @return the value of the property, not null
   */
  public DayCount getDayCount() {
    return dayCount;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets yield convention.
   * <p>
   * The convention defines how to convert from yield to price and inversely.
   * @return the value of the property, not null
   */
  public FixedCouponBondYieldConvention getYieldConvention() {
    return yieldConvention;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the legal entity identifier.
   * <p>
   * This identifier is used for the legal entity that issues the bond.
   * @return the value of the property, not null
   */
  @Override
  public StandardId getLegalEntityId() {
    return legalEntityId;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the number of days between valuation date and settlement date.
   * <p>
   * This is used to compute clean price.
   * The clean price is the relative price to be paid at the standard settlement date in exchange for the bond.
   * <p>
   * It is usually one business day for US treasuries and UK Gilts and three days for Euroland government bonds.
   * @return the value of the property, not null
   */
  public DaysAdjustment getSettlementDateOffset() {
    return settlementDateOffset;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets ex-coupon period.
   * <p>
   * Some bonds trade ex-coupons before the coupon payment. The coupon is paid not to the
   * owner of the bond on the payment date but to the owner of the bond on the detachment date.
   * The difference between the two is the ex-coupon period (measured in days).
   * <p>
   * Because the detachment date is not after the coupon date, the number of days
   * stored in this field should be zero or negative.
   * @return the value of the property, not null
   */
  public DaysAdjustment getExCouponPeriod() {
    return exCouponPeriod;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FixedCouponBondSecurity other = (FixedCouponBondSecurity) obj;
      return JodaBeanUtils.equal(info, other.info) &&
          JodaBeanUtils.equal(currency, other.currency) &&
          JodaBeanUtils.equal(notional, other.notional) &&
          JodaBeanUtils.equal(accrualSchedule, other.accrualSchedule) &&
          JodaBeanUtils.equal(fixedRate, other.fixedRate) &&
          JodaBeanUtils.equal(dayCount, other.dayCount) &&
          JodaBeanUtils.equal(yieldConvention, other.yieldConvention) &&
          JodaBeanUtils.equal(legalEntityId, other.legalEntityId) &&
          JodaBeanUtils.equal(settlementDateOffset, other.settlementDateOffset) &&
          JodaBeanUtils.equal(exCouponPeriod, other.exCouponPeriod);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(info);
    hash = hash * 31 + JodaBeanUtils.hashCode(currency);
    hash = hash * 31 + JodaBeanUtils.hashCode(notional);
    hash = hash * 31 + JodaBeanUtils.hashCode(accrualSchedule);
    hash = hash * 31 + JodaBeanUtils.hashCode(fixedRate);
    hash = hash * 31 + JodaBeanUtils.hashCode(dayCount);
    hash = hash * 31 + JodaBeanUtils.hashCode(yieldConvention);
    hash = hash * 31 + JodaBeanUtils.hashCode(legalEntityId);
    hash = hash * 31 + JodaBeanUtils.hashCode(settlementDateOffset);
    hash = hash * 31 + JodaBeanUtils.hashCode(exCouponPeriod);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(352);
    buf.append("FixedCouponBondSecurity{");
    buf.append("info").append('=').append(info).append(',').append(' ');
    buf.append("currency").append('=').append(currency).append(',').append(' ');
    buf.append("notional").append('=').append(notional).append(',').append(' ');
    buf.append("accrualSchedule").append('=').append(accrualSchedule).append(',').append(' ');
    buf.append("fixedRate").append('=').append(fixedRate).append(',').append(' ');
    buf.append("dayCount").append('=').append(dayCount).append(',').append(' ');
    buf.append("yieldConvention").append('=').append(yieldConvention).append(',').append(' ');
    buf.append("legalEntityId").append('=').append(legalEntityId).append(',').append(' ');
    buf.append("settlementDateOffset").append('=').append(settlementDateOffset).append(',').append(' ');
    buf.append("exCouponPeriod").append('=').append(JodaBeanUtils.toString(exCouponPeriod));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FixedCouponBondSecurity}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code info} property.
     */
    private final MetaProperty<SecurityInfo> info = DirectMetaProperty.ofImmutable(
        this, "info", FixedCouponBondSecurity.class, SecurityInfo.class);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> currency = DirectMetaProperty.ofImmutable(
        this, "currency", FixedCouponBondSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code notional} property.
     */
    private final MetaProperty<Double> notional = DirectMetaProperty.ofImmutable(
        this, "notional", FixedCouponBondSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code accrualSchedule} property.
     */
    private final MetaProperty<PeriodicSchedule> accrualSchedule = DirectMetaProperty.ofImmutable(
        this, "accrualSchedule", FixedCouponBondSecurity.class, PeriodicSchedule.class);
    /**
     * The meta-property for the {@code fixedRate} property.
     */
    private final MetaProperty<Double> fixedRate = DirectMetaProperty.ofImmutable(
        this, "fixedRate", FixedCouponBondSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code dayCount} property.
     */
    private final MetaProperty<DayCount> dayCount = DirectMetaProperty.ofImmutable(
        this, "dayCount", FixedCouponBondSecurity.class, DayCount.class);
    /**
     * The meta-property for the {@code yieldConvention} property.
     */
    private final MetaProperty<FixedCouponBondYieldConvention> yieldConvention = DirectMetaProperty.ofImmutable(
        this, "yieldConvention", FixedCouponBondSecurity.class, FixedCouponBondYieldConvention.class);
    /**
     * The meta-property for the {@code legalEntityId} property.
     */
    private final MetaProperty<StandardId> legalEntityId = DirectMetaProperty.ofImmutable(
        this, "legalEntityId", FixedCouponBondSecurity.class, StandardId.class);
    /**
     * The meta-property for the {@code settlementDateOffset} property.
     */
    private final MetaProperty<DaysAdjustment> settlementDateOffset = DirectMetaProperty.ofImmutable(
        this, "settlementDateOffset", FixedCouponBondSecurity.class, DaysAdjustment.class);
    /**
     * The meta-property for the {@code exCouponPeriod} property.
     */
    private final MetaProperty<DaysAdjustment> exCouponPeriod = DirectMetaProperty.ofImmutable(
        this, "exCouponPeriod", FixedCouponBondSecurity.class, DaysAdjustment.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "info",
        "currency",
        "notional",
        "accrualSchedule",
        "fixedRate",
        "dayCount",
        "yieldConvention",
        "legalEntityId",
        "settlementDateOffset",
        "exCouponPeriod");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return info;
        case 575402001:  // currency
          return currency;
        case 1585636160:  // notional
          return notional;
        case 304659814:  // accrualSchedule
          return accrualSchedule;
        case 747425396:  // fixedRate
          return fixedRate;
        case 1905311443:  // dayCount
          return dayCount;
        case -1895216418:  // yieldConvention
          return yieldConvention;
        case 866287159:  // legalEntityId
          return legalEntityId;
        case 135924714:  // settlementDateOffset
          return settlementDateOffset;
        case 1408037338:  // exCouponPeriod
          return exCouponPeriod;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public FixedCouponBondSecurity.Builder builder() {
      return new FixedCouponBondSecurity.Builder();
    }

    @Override
    public Class<? extends FixedCouponBondSecurity> beanType() {
      return FixedCouponBondSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code info} property.
     * @return the meta-property, not null
     */
    public MetaProperty<SecurityInfo> info() {
      return info;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Currency> currency() {
      return currency;
    }

    /**
     * The meta-property for the {@code notional} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> notional() {
      return notional;
    }

    /**
     * The meta-property for the {@code accrualSchedule} property.
     * @return the meta-property, not null
     */
    public MetaProperty<PeriodicSchedule> accrualSchedule() {
      return accrualSchedule;
    }

    /**
     * The meta-property for the {@code fixedRate} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> fixedRate() {
      return fixedRate;
    }

    /**
     * The meta-property for the {@code dayCount} property.
     * @return the meta-property, not null
     */
    public MetaProperty<DayCount> dayCount() {
      return dayCount;
    }

    /**
     * The meta-property for the {@code yieldConvention} property.
     * @return the meta-property, not null
     */
    public MetaProperty<FixedCouponBondYieldConvention> yieldConvention() {
      return yieldConvention;
    }

    /**
     * The meta-property for the {@code legalEntityId} property.
     * @return the meta-property, not null
     */
    public MetaProperty<StandardId> legalEntityId() {
      return legalEntityId;
    }

    /**
     * The meta-property for the {@code settlementDateOffset} property.
     * @return the meta-property, not null
     */
    public MetaProperty<DaysAdjustment> settlementDateOffset() {
      return settlementDateOffset;
    }

    /**
     * The meta-property for the {@code exCouponPeriod} property.
     * @return the meta-property, not null
     */
    public MetaProperty<DaysAdjustment> exCouponPeriod() {
      return exCouponPeriod;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return ((FixedCouponBondSecurity) bean).getInfo();
        case 575402001:  // currency
          return ((FixedCouponBondSecurity) bean).getCurrency();
        case 1585636160:  // notional
          return ((FixedCouponBondSecurity) bean).getNotional();
        case 304659814:  // accrualSchedule
          return ((FixedCouponBondSecurity) bean).getAccrualSchedule();
        case 747425396:  // fixedRate
          return ((FixedCouponBondSecurity) bean).getFixedRate();
        case 1905311443:  // dayCount
          return ((FixedCouponBondSecurity) bean).getDayCount();
        case -1895216418:  // yieldConvention
          return ((FixedCouponBondSecurity) bean).getYieldConvention();
        case 866287159:  // legalEntityId
          return ((FixedCouponBondSecurity) bean).getLegalEntityId();
        case 135924714:  // settlementDateOffset
          return ((FixedCouponBondSecurity) bean).getSettlementDateOffset();
        case 1408037338:  // exCouponPeriod
          return ((FixedCouponBondSecurity) bean).getExCouponPeriod();
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
   * The bean-builder for {@code FixedCouponBondSecurity}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<FixedCouponBondSecurity> {

    private SecurityInfo info;
    private Currency currency;
    private double notional;
    private PeriodicSchedule accrualSchedule;
    private double fixedRate;
    private DayCount dayCount;
    private FixedCouponBondYieldConvention yieldConvention;
    private StandardId legalEntityId;
    private DaysAdjustment settlementDateOffset;
    private DaysAdjustment exCouponPeriod;

    /**
     * Restricted constructor.
     */
    private Builder() {
      applyDefaults(this);
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(FixedCouponBondSecurity beanToCopy) {
      this.info = beanToCopy.getInfo();
      this.currency = beanToCopy.getCurrency();
      this.notional = beanToCopy.getNotional();
      this.accrualSchedule = beanToCopy.getAccrualSchedule();
      this.fixedRate = beanToCopy.getFixedRate();
      this.dayCount = beanToCopy.getDayCount();
      this.yieldConvention = beanToCopy.getYieldConvention();
      this.legalEntityId = beanToCopy.getLegalEntityId();
      this.settlementDateOffset = beanToCopy.getSettlementDateOffset();
      this.exCouponPeriod = beanToCopy.getExCouponPeriod();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return info;
        case 575402001:  // currency
          return currency;
        case 1585636160:  // notional
          return notional;
        case 304659814:  // accrualSchedule
          return accrualSchedule;
        case 747425396:  // fixedRate
          return fixedRate;
        case 1905311443:  // dayCount
          return dayCount;
        case -1895216418:  // yieldConvention
          return yieldConvention;
        case 866287159:  // legalEntityId
          return legalEntityId;
        case 135924714:  // settlementDateOffset
          return settlementDateOffset;
        case 1408037338:  // exCouponPeriod
          return exCouponPeriod;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          this.info = (SecurityInfo) newValue;
          break;
        case 575402001:  // currency
          this.currency = (Currency) newValue;
          break;
        case 1585636160:  // notional
          this.notional = (Double) newValue;
          break;
        case 304659814:  // accrualSchedule
          this.accrualSchedule = (PeriodicSchedule) newValue;
          break;
        case 747425396:  // fixedRate
          this.fixedRate = (Double) newValue;
          break;
        case 1905311443:  // dayCount
          this.dayCount = (DayCount) newValue;
          break;
        case -1895216418:  // yieldConvention
          this.yieldConvention = (FixedCouponBondYieldConvention) newValue;
          break;
        case 866287159:  // legalEntityId
          this.legalEntityId = (StandardId) newValue;
          break;
        case 135924714:  // settlementDateOffset
          this.settlementDateOffset = (DaysAdjustment) newValue;
          break;
        case 1408037338:  // exCouponPeriod
          this.exCouponPeriod = (DaysAdjustment) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public FixedCouponBondSecurity build() {
      return new FixedCouponBondSecurity(
          info,
          currency,
          notional,
          accrualSchedule,
          fixedRate,
          dayCount,
          yieldConvention,
          legalEntityId,
          settlementDateOffset,
          exCouponPeriod);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the standard security information.
     * <p>
     * This includes the security identifier.
     * @param info  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder info(SecurityInfo info) {
      JodaBeanUtils.notNull(info, "info");
      this.info = info;
      return this;
    }

    /**
     * Sets the currency that the bond is traded in.
     * @param currency  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder currency(Currency currency) {
      JodaBeanUtils.notNull(currency, "currency");
      this.currency = currency;
      return this;
    }

    /**
     * Sets the notional amount, must be positive.
     * <p>
     * The notional expressed here must be positive.
     * The currency of the notional is specified by {@code currency}.
     * @param notional  the new value
     * @return this, for chaining, not null
     */
    public Builder notional(double notional) {
      ArgChecker.notNegativeOrZero(notional, "notional");
      this.notional = notional;
      return this;
    }

    /**
     * Sets the accrual schedule.
     * <p>
     * This is used to define the accrual periods.
     * These are used directly or indirectly to determine other dates in the product.
     * @param accrualSchedule  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder accrualSchedule(PeriodicSchedule accrualSchedule) {
      JodaBeanUtils.notNull(accrualSchedule, "accrualSchedule");
      this.accrualSchedule = accrualSchedule;
      return this;
    }

    /**
     * Sets the fixed coupon rate.
     * <p>
     * The periodic payments are based on this fixed coupon rate.
     * @param fixedRate  the new value
     * @return this, for chaining, not null
     */
    public Builder fixedRate(double fixedRate) {
      this.fixedRate = fixedRate;
      return this;
    }

    /**
     * Sets the day count convention applicable.
     * <p>
     * The conversion from dates to a numerical value is made based on this day count.
     * For the fixed bond, the day count convention is used to compute accrued interest.
     * <p>
     * Note that the year fraction of a coupon payment is computed based on the unadjusted
     * dates in the schedule.
     * @param dayCount  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder dayCount(DayCount dayCount) {
      JodaBeanUtils.notNull(dayCount, "dayCount");
      this.dayCount = dayCount;
      return this;
    }

    /**
     * Sets yield convention.
     * <p>
     * The convention defines how to convert from yield to price and inversely.
     * @param yieldConvention  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder yieldConvention(FixedCouponBondYieldConvention yieldConvention) {
      JodaBeanUtils.notNull(yieldConvention, "yieldConvention");
      this.yieldConvention = yieldConvention;
      return this;
    }

    /**
     * Sets the legal entity identifier.
     * <p>
     * This identifier is used for the legal entity that issues the bond.
     * @param legalEntityId  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder legalEntityId(StandardId legalEntityId) {
      JodaBeanUtils.notNull(legalEntityId, "legalEntityId");
      this.legalEntityId = legalEntityId;
      return this;
    }

    /**
     * Sets the number of days between valuation date and settlement date.
     * <p>
     * This is used to compute clean price.
     * The clean price is the relative price to be paid at the standard settlement date in exchange for the bond.
     * <p>
     * It is usually one business day for US treasuries and UK Gilts and three days for Euroland government bonds.
     * @param settlementDateOffset  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder settlementDateOffset(DaysAdjustment settlementDateOffset) {
      JodaBeanUtils.notNull(settlementDateOffset, "settlementDateOffset");
      this.settlementDateOffset = settlementDateOffset;
      return this;
    }

    /**
     * Sets ex-coupon period.
     * <p>
     * Some bonds trade ex-coupons before the coupon payment. The coupon is paid not to the
     * owner of the bond on the payment date but to the owner of the bond on the detachment date.
     * The difference between the two is the ex-coupon period (measured in days).
     * <p>
     * Because the detachment date is not after the coupon date, the number of days
     * stored in this field should be zero or negative.
     * @param exCouponPeriod  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder exCouponPeriod(DaysAdjustment exCouponPeriod) {
      JodaBeanUtils.notNull(exCouponPeriod, "exCouponPeriod");
      this.exCouponPeriod = exCouponPeriod;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(352);
      buf.append("FixedCouponBondSecurity.Builder{");
      buf.append("info").append('=').append(JodaBeanUtils.toString(info)).append(',').append(' ');
      buf.append("currency").append('=').append(JodaBeanUtils.toString(currency)).append(',').append(' ');
      buf.append("notional").append('=').append(JodaBeanUtils.toString(notional)).append(',').append(' ');
      buf.append("accrualSchedule").append('=').append(JodaBeanUtils.toString(accrualSchedule)).append(',').append(' ');
      buf.append("fixedRate").append('=').append(JodaBeanUtils.toString(fixedRate)).append(',').append(' ');
      buf.append("dayCount").append('=').append(JodaBeanUtils.toString(dayCount)).append(',').append(' ');
      buf.append("yieldConvention").append('=').append(JodaBeanUtils.toString(yieldConvention)).append(',').append(' ');
      buf.append("legalEntityId").append('=').append(JodaBeanUtils.toString(legalEntityId)).append(',').append(' ');
      buf.append("settlementDateOffset").append('=').append(JodaBeanUtils.toString(settlementDateOffset)).append(',').append(' ');
      buf.append("exCouponPeriod").append('=').append(JodaBeanUtils.toString(exCouponPeriod));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
