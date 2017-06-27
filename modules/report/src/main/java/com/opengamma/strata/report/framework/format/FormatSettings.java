/*
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.report.framework.format;

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

/**
 * Contains formatting settings for a specific type.
 *
 * @param <T> the type of value to which the settings apply
 */
@BeanDefinition(builderScope = "private")
public final class FormatSettings<T>
    implements ImmutableBean {

  /**
   * The category of this type.
   */
  @PropertyDefinition(validate = "notNull")
  private final FormatCategory category;
  /**
   * The formatter to use to convert this type into a string.
   */
  @PropertyDefinition(validate = "notNull")
  private final ValueFormatter<T> formatter;

  /**
   * Obtains settings from category and formatter.
   * 
   * @param <T>  the type of the value
   * @param category  the category of the type
   * @param formatter  the formatter the use for the type
   * @return the format settings
   */
  public static <T> FormatSettings<T> of(FormatCategory category, ValueFormatter<T> formatter) {
    return new FormatSettings<T>(category, formatter);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code FormatSettings}.
   * @return the meta-bean, not null
   */
  @SuppressWarnings("rawtypes")
  public static FormatSettings.Meta meta() {
    return FormatSettings.Meta.INSTANCE;
  }

  /**
   * The meta-bean for {@code FormatSettings}.
   * @param <R>  the bean's generic type
   * @param cls  the bean's generic type
   * @return the meta-bean, not null
   */
  @SuppressWarnings("unchecked")
  public static <R> FormatSettings.Meta<R> metaFormatSettings(Class<R> cls) {
    return FormatSettings.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(FormatSettings.Meta.INSTANCE);
  }

  private FormatSettings(
      FormatCategory category,
      ValueFormatter<T> formatter) {
    JodaBeanUtils.notNull(category, "category");
    JodaBeanUtils.notNull(formatter, "formatter");
    this.category = category;
    this.formatter = formatter;
  }

  @SuppressWarnings("unchecked")
  @Override
  public FormatSettings.Meta<T> metaBean() {
    return FormatSettings.Meta.INSTANCE;
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
   * Gets the category of this type.
   * @return the value of the property, not null
   */
  public FormatCategory getCategory() {
    return category;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the formatter to use to convert this type into a string.
   * @return the value of the property, not null
   */
  public ValueFormatter<T> getFormatter() {
    return formatter;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FormatSettings<?> other = (FormatSettings<?>) obj;
      return JodaBeanUtils.equal(category, other.category) &&
          JodaBeanUtils.equal(formatter, other.formatter);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(category);
    hash = hash * 31 + JodaBeanUtils.hashCode(formatter);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("FormatSettings{");
    buf.append("category").append('=').append(category).append(',').append(' ');
    buf.append("formatter").append('=').append(JodaBeanUtils.toString(formatter));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FormatSettings}.
   * @param <T>  the type
   */
  public static final class Meta<T> extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    @SuppressWarnings("rawtypes")
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code category} property.
     */
    private final MetaProperty<FormatCategory> category = DirectMetaProperty.ofImmutable(
        this, "category", FormatSettings.class, FormatCategory.class);
    /**
     * The meta-property for the {@code formatter} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<ValueFormatter<T>> formatter = DirectMetaProperty.ofImmutable(
        this, "formatter", FormatSettings.class, (Class) ValueFormatter.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "category",
        "formatter");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 50511102:  // category
          return category;
        case 1811591370:  // formatter
          return formatter;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends FormatSettings<T>> builder() {
      return new FormatSettings.Builder<T>();
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public Class<? extends FormatSettings<T>> beanType() {
      return (Class) FormatSettings.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code category} property.
     * @return the meta-property, not null
     */
    public MetaProperty<FormatCategory> category() {
      return category;
    }

    /**
     * The meta-property for the {@code formatter} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ValueFormatter<T>> formatter() {
      return formatter;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 50511102:  // category
          return ((FormatSettings<?>) bean).getCategory();
        case 1811591370:  // formatter
          return ((FormatSettings<?>) bean).getFormatter();
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
   * The bean-builder for {@code FormatSettings}.
   * @param <T>  the type
   */
  private static final class Builder<T> extends DirectPrivateBeanBuilder<FormatSettings<T>> {

    private FormatCategory category;
    private ValueFormatter<T> formatter;

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
        case 50511102:  // category
          return category;
        case 1811591370:  // formatter
          return formatter;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder<T> set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 50511102:  // category
          this.category = (FormatCategory) newValue;
          break;
        case 1811591370:  // formatter
          this.formatter = (ValueFormatter<T>) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public FormatSettings<T> build() {
      return new FormatSettings<T>(
          category,
          formatter);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("FormatSettings.Builder{");
      buf.append("category").append('=').append(JodaBeanUtils.toString(category)).append(',').append(' ');
      buf.append("formatter").append('=').append(JodaBeanUtils.toString(formatter));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
