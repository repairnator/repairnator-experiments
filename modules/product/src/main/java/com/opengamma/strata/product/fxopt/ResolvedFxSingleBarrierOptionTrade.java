/*
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.fxopt;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableDefaults;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.currency.Payment;
import com.opengamma.strata.product.ResolvedTrade;
import com.opengamma.strata.product.TradeInfo;

/**
 * A trade in an FX single barrier option, resolved for pricing.
 * <p>
 * This is the resolved form of {@link FxSingleBarrierOptionTrade} and is the primary input to the pricers.
 * Applications will typically create a {@code ResolvedFxSingleBarrierOptionTrade} from a {@code FxSingleBarrierOptionTrade}
 * using {@link FxSingleBarrierOptionTrade#resolve(ReferenceData)}.
 * <p>
 * A {@code ResolvedFxSingleBarrierOptionTrade} is bound to data that changes over time, such as holiday calendars.
 * If the data changes, such as the addition of a new holiday, the resolved form will not be updated.
 * Care must be taken when placing the resolved form in a cache or persistence layer.
 */
@BeanDefinition
public final class ResolvedFxSingleBarrierOptionTrade
    implements ResolvedTrade, ImmutableBean, Serializable {

  /**
   * The additional trade information, defaulted to an empty instance.
   * <p>
   * This allows additional information to be attached to the trade.
   */
  @PropertyDefinition(overrideGet = true)
  private final TradeInfo info;
  /**
   * The resolved barrier FX option product.
   * <p>
   * The product captures the contracted financial details of the trade.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final ResolvedFxSingleBarrierOption product;
  /**
   * The premium of the FX option.
   * <p>
   * The premium sign should be compatible with the product Long/Short flag.
   * This means that the premium is negative for long and positive for short.
   */
  @PropertyDefinition(validate = "notNull")
  private final Payment premium;

  //-------------------------------------------------------------------------
  @ImmutableDefaults
  private static void applyDefaults(Builder builder) {
    builder.info = TradeInfo.empty();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ResolvedFxSingleBarrierOptionTrade}.
   * @return the meta-bean, not null
   */
  public static ResolvedFxSingleBarrierOptionTrade.Meta meta() {
    return ResolvedFxSingleBarrierOptionTrade.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ResolvedFxSingleBarrierOptionTrade.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static ResolvedFxSingleBarrierOptionTrade.Builder builder() {
    return new ResolvedFxSingleBarrierOptionTrade.Builder();
  }

  private ResolvedFxSingleBarrierOptionTrade(
      TradeInfo info,
      ResolvedFxSingleBarrierOption product,
      Payment premium) {
    JodaBeanUtils.notNull(product, "product");
    JodaBeanUtils.notNull(premium, "premium");
    this.info = info;
    this.product = product;
    this.premium = premium;
  }

  @Override
  public ResolvedFxSingleBarrierOptionTrade.Meta metaBean() {
    return ResolvedFxSingleBarrierOptionTrade.Meta.INSTANCE;
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
   * Gets the additional trade information, defaulted to an empty instance.
   * <p>
   * This allows additional information to be attached to the trade.
   * @return the value of the property
   */
  @Override
  public TradeInfo getInfo() {
    return info;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the resolved barrier FX option product.
   * <p>
   * The product captures the contracted financial details of the trade.
   * @return the value of the property, not null
   */
  @Override
  public ResolvedFxSingleBarrierOption getProduct() {
    return product;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the premium of the FX option.
   * <p>
   * The premium sign should be compatible with the product Long/Short flag.
   * This means that the premium is negative for long and positive for short.
   * @return the value of the property, not null
   */
  public Payment getPremium() {
    return premium;
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
      ResolvedFxSingleBarrierOptionTrade other = (ResolvedFxSingleBarrierOptionTrade) obj;
      return JodaBeanUtils.equal(info, other.info) &&
          JodaBeanUtils.equal(product, other.product) &&
          JodaBeanUtils.equal(premium, other.premium);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(info);
    hash = hash * 31 + JodaBeanUtils.hashCode(product);
    hash = hash * 31 + JodaBeanUtils.hashCode(premium);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("ResolvedFxSingleBarrierOptionTrade{");
    buf.append("info").append('=').append(info).append(',').append(' ');
    buf.append("product").append('=').append(product).append(',').append(' ');
    buf.append("premium").append('=').append(JodaBeanUtils.toString(premium));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ResolvedFxSingleBarrierOptionTrade}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code info} property.
     */
    private final MetaProperty<TradeInfo> info = DirectMetaProperty.ofImmutable(
        this, "info", ResolvedFxSingleBarrierOptionTrade.class, TradeInfo.class);
    /**
     * The meta-property for the {@code product} property.
     */
    private final MetaProperty<ResolvedFxSingleBarrierOption> product = DirectMetaProperty.ofImmutable(
        this, "product", ResolvedFxSingleBarrierOptionTrade.class, ResolvedFxSingleBarrierOption.class);
    /**
     * The meta-property for the {@code premium} property.
     */
    private final MetaProperty<Payment> premium = DirectMetaProperty.ofImmutable(
        this, "premium", ResolvedFxSingleBarrierOptionTrade.class, Payment.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "info",
        "product",
        "premium");

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
        case -309474065:  // product
          return product;
        case -318452137:  // premium
          return premium;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public ResolvedFxSingleBarrierOptionTrade.Builder builder() {
      return new ResolvedFxSingleBarrierOptionTrade.Builder();
    }

    @Override
    public Class<? extends ResolvedFxSingleBarrierOptionTrade> beanType() {
      return ResolvedFxSingleBarrierOptionTrade.class;
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
    public MetaProperty<TradeInfo> info() {
      return info;
    }

    /**
     * The meta-property for the {@code product} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ResolvedFxSingleBarrierOption> product() {
      return product;
    }

    /**
     * The meta-property for the {@code premium} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Payment> premium() {
      return premium;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return ((ResolvedFxSingleBarrierOptionTrade) bean).getInfo();
        case -309474065:  // product
          return ((ResolvedFxSingleBarrierOptionTrade) bean).getProduct();
        case -318452137:  // premium
          return ((ResolvedFxSingleBarrierOptionTrade) bean).getPremium();
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
   * The bean-builder for {@code ResolvedFxSingleBarrierOptionTrade}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<ResolvedFxSingleBarrierOptionTrade> {

    private TradeInfo info;
    private ResolvedFxSingleBarrierOption product;
    private Payment premium;

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
    private Builder(ResolvedFxSingleBarrierOptionTrade beanToCopy) {
      this.info = beanToCopy.getInfo();
      this.product = beanToCopy.getProduct();
      this.premium = beanToCopy.getPremium();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return info;
        case -309474065:  // product
          return product;
        case -318452137:  // premium
          return premium;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          this.info = (TradeInfo) newValue;
          break;
        case -309474065:  // product
          this.product = (ResolvedFxSingleBarrierOption) newValue;
          break;
        case -318452137:  // premium
          this.premium = (Payment) newValue;
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

    /**
     * @deprecated Use Joda-Convert in application code
     */
    @Override
    @Deprecated
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    /**
     * @deprecated Use Joda-Convert in application code
     */
    @Override
    @Deprecated
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    /**
     * @deprecated Loop in application code
     */
    @Override
    @Deprecated
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public ResolvedFxSingleBarrierOptionTrade build() {
      return new ResolvedFxSingleBarrierOptionTrade(
          info,
          product,
          premium);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the additional trade information, defaulted to an empty instance.
     * <p>
     * This allows additional information to be attached to the trade.
     * @param info  the new value
     * @return this, for chaining, not null
     */
    public Builder info(TradeInfo info) {
      this.info = info;
      return this;
    }

    /**
     * Sets the resolved barrier FX option product.
     * <p>
     * The product captures the contracted financial details of the trade.
     * @param product  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder product(ResolvedFxSingleBarrierOption product) {
      JodaBeanUtils.notNull(product, "product");
      this.product = product;
      return this;
    }

    /**
     * Sets the premium of the FX option.
     * <p>
     * The premium sign should be compatible with the product Long/Short flag.
     * This means that the premium is negative for long and positive for short.
     * @param premium  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder premium(Payment premium) {
      JodaBeanUtils.notNull(premium, "premium");
      this.premium = premium;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(128);
      buf.append("ResolvedFxSingleBarrierOptionTrade.Builder{");
      buf.append("info").append('=').append(JodaBeanUtils.toString(info)).append(',').append(' ');
      buf.append("product").append('=').append(JodaBeanUtils.toString(product)).append(',').append(' ');
      buf.append("premium").append('=').append(JodaBeanUtils.toString(premium));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
