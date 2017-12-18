/*
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.calc.marketdata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableMap;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.collect.Messages;

/**
 * A container for market data configuration objects which all have the same type.
 * <p>
 * This class wraps a map where the keys are the names of the configuration objects and the values
 * are the configuration objects themselves.
 * <p>
 * This class only exists to enable serialization. Market data configuration objects are referenced by
 * name and type. The obvious implementation strategy is to use a pair of type and name to as keys
 * when storing them in a map. Unfortunately this won't serialize correctly using Joda Beans so it
 * is necessary to store them over two levels. {@code MarketDataConfig} contains a map of
 * {@code SingleTypeMarketDataConfig} instances keyed by the type of the config objects, and
 * the {@code SingleTypeMarketDataConfig} instances contains the configuration objects keyed by name.
 */
@BeanDefinition
final class SingleTypeMarketDataConfig implements ImmutableBean, Serializable {

  /** The type of the configuration objects. */
  @PropertyDefinition(validate = "notNull")
  private final Class<?> configType;

  /** The configuration objects, keyed by name. */
  @PropertyDefinition(validate = "notNull")
  private final ImmutableMap<String, Object> configObjects;

  /**
   * Returns the configuration object with the specified name if found or throws an exception if not.
   *
   * @param name  the name of the configuration object
   * @return the named object if available
   * @throws IllegalArgumentException if no configuration is found with the specified name
   */
  Object get(String name) {
    Object config = configObjects.get(name);

    if (config == null) {
      throw new IllegalArgumentException(
          Messages.format(
              "No configuration found with type {} and name {}",
              configType.getName(),
              name));
    }
    return config;
  }

  /**
   * Returns a copy of this set of configuration with the specified object added.
   * <p>
   * The configuration object must be the same type os this object's {@code configType}.
   * <p>
   * If an object is already present with the specified name it will be replaced.
   *
   * @param name  the name of the configuration object
   * @param config  the configuration object
   * @return a copy of this set of configuration with the specified object added
   * @throws IllegalArgumentException if the configuration object is not of the required type
   */
  SingleTypeMarketDataConfig withConfig(String name, Object config) {
    ArgChecker.notEmpty(name, "name");
    ArgChecker.notNull(config, "config");

    if (!configType.equals(config.getClass())) {
      throw new IllegalArgumentException(
          Messages.format(
              "Configuration object {} is not of the required type {}",
              config,
              configType.getName()));
    }
    // Use a hash map to allow values to be overwritten, preserving normal map semantics.
    // ImmutableMap builder throws an exception when there are duplicate keys.
    HashMap<String, Object> configCopy = new HashMap<>(configObjects);
    configCopy.put(name, config);
    return new SingleTypeMarketDataConfig(configType, configCopy);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code SingleTypeMarketDataConfig}.
   * @return the meta-bean, not null
   */
  public static SingleTypeMarketDataConfig.Meta meta() {
    return SingleTypeMarketDataConfig.Meta.INSTANCE;
  }

  static {
    MetaBean.register(SingleTypeMarketDataConfig.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  static SingleTypeMarketDataConfig.Builder builder() {
    return new SingleTypeMarketDataConfig.Builder();
  }

  private SingleTypeMarketDataConfig(
      Class<?> configType,
      Map<String, Object> configObjects) {
    JodaBeanUtils.notNull(configType, "configType");
    JodaBeanUtils.notNull(configObjects, "configObjects");
    this.configType = configType;
    this.configObjects = ImmutableMap.copyOf(configObjects);
  }

  @Override
  public SingleTypeMarketDataConfig.Meta metaBean() {
    return SingleTypeMarketDataConfig.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the type of the configuration objects.
   * @return the value of the property, not null
   */
  public Class<?> getConfigType() {
    return configType;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the configuration objects, keyed by name.
   * @return the value of the property, not null
   */
  public ImmutableMap<String, Object> getConfigObjects() {
    return configObjects;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SingleTypeMarketDataConfig other = (SingleTypeMarketDataConfig) obj;
      return JodaBeanUtils.equal(configType, other.configType) &&
          JodaBeanUtils.equal(configObjects, other.configObjects);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(configType);
    hash = hash * 31 + JodaBeanUtils.hashCode(configObjects);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("SingleTypeMarketDataConfig{");
    buf.append("configType").append('=').append(configType).append(',').append(' ');
    buf.append("configObjects").append('=').append(JodaBeanUtils.toString(configObjects));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SingleTypeMarketDataConfig}.
   */
  static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code configType} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Class<?>> configType = DirectMetaProperty.ofImmutable(
        this, "configType", SingleTypeMarketDataConfig.class, (Class) Class.class);
    /**
     * The meta-property for the {@code configObjects} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<ImmutableMap<String, Object>> configObjects = DirectMetaProperty.ofImmutable(
        this, "configObjects", SingleTypeMarketDataConfig.class, (Class) ImmutableMap.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "configType",
        "configObjects");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 831526300:  // configType
          return configType;
        case 2117143410:  // configObjects
          return configObjects;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public SingleTypeMarketDataConfig.Builder builder() {
      return new SingleTypeMarketDataConfig.Builder();
    }

    @Override
    public Class<? extends SingleTypeMarketDataConfig> beanType() {
      return SingleTypeMarketDataConfig.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code configType} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Class<?>> configType() {
      return configType;
    }

    /**
     * The meta-property for the {@code configObjects} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ImmutableMap<String, Object>> configObjects() {
      return configObjects;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 831526300:  // configType
          return ((SingleTypeMarketDataConfig) bean).getConfigType();
        case 2117143410:  // configObjects
          return ((SingleTypeMarketDataConfig) bean).getConfigObjects();
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
   * The bean-builder for {@code SingleTypeMarketDataConfig}.
   */
  static final class Builder extends DirectFieldsBeanBuilder<SingleTypeMarketDataConfig> {

    private Class<?> configType;
    private Map<String, Object> configObjects = ImmutableMap.of();

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(SingleTypeMarketDataConfig beanToCopy) {
      this.configType = beanToCopy.getConfigType();
      this.configObjects = beanToCopy.getConfigObjects();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 831526300:  // configType
          return configType;
        case 2117143410:  // configObjects
          return configObjects;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 831526300:  // configType
          this.configType = (Class<?>) newValue;
          break;
        case 2117143410:  // configObjects
          this.configObjects = (Map<String, Object>) newValue;
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
    public SingleTypeMarketDataConfig build() {
      return new SingleTypeMarketDataConfig(
          configType,
          configObjects);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the type of the configuration objects.
     * @param configType  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder configType(Class<?> configType) {
      JodaBeanUtils.notNull(configType, "configType");
      this.configType = configType;
      return this;
    }

    /**
     * Sets the configuration objects, keyed by name.
     * @param configObjects  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder configObjects(Map<String, Object> configObjects) {
      JodaBeanUtils.notNull(configObjects, "configObjects");
      this.configObjects = configObjects;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("SingleTypeMarketDataConfig.Builder{");
      buf.append("configType").append('=').append(JodaBeanUtils.toString(configType)).append(',').append(' ');
      buf.append("configObjects").append('=').append(JodaBeanUtils.toString(configObjects));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
