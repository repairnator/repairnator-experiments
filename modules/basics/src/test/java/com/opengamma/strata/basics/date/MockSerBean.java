/*
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.basics.date;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.joda.beans.Bean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.TypedMetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.BasicBeanBuilder;
import org.joda.beans.impl.direct.MinimalMetaBean;

/**
 * Mock for serialization testing.
 */
@BeanDefinition(style = "minimal")
public final class MockSerBean implements Bean {

  @PropertyDefinition
  private BusinessDayConvention bdConvention;

  @PropertyDefinition
  private HolidayCalendar holidayCalendar;

  @PropertyDefinition
  private DayCount dayCount;

  @PropertyDefinition
  private List<Object> objects = new ArrayList<>();

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code MockSerBean}.
   */
  @SuppressWarnings("unchecked")
  private static final TypedMetaBean<MockSerBean> META_BEAN =
      MinimalMetaBean.of(
          MockSerBean.class,
          new String[] {
              "bdConvention",
              "holidayCalendar",
              "dayCount",
              "objects"},
          () -> new BasicBeanBuilder<>(new MockSerBean()),
          Arrays.<Function<MockSerBean, Object>>asList(
              b -> b.getBdConvention(),
              b -> b.getHolidayCalendar(),
              b -> b.getDayCount(),
              b -> b.getObjects()),
          Arrays.<BiConsumer<MockSerBean, Object>>asList(
              (b, v) -> b.setBdConvention((BusinessDayConvention) v),
              (b, v) -> b.setHolidayCalendar((HolidayCalendar) v),
              (b, v) -> b.setDayCount((DayCount) v),
              (b, v) -> b.setObjects((List<Object>) v)));

  /**
   * The meta-bean for {@code MockSerBean}.
   * @return the meta-bean, not null
   */
  public static TypedMetaBean<MockSerBean> meta() {
    return META_BEAN;
  }

  static {
    MetaBean.register(META_BEAN);
  }

  @Override
  public TypedMetaBean<MockSerBean> metaBean() {
    return META_BEAN;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the bdConvention.
   * @return the value of the property
   */
  public BusinessDayConvention getBdConvention() {
    return bdConvention;
  }

  /**
   * Sets the bdConvention.
   * @param bdConvention  the new value of the property
   */
  public void setBdConvention(BusinessDayConvention bdConvention) {
    this.bdConvention = bdConvention;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the holidayCalendar.
   * @return the value of the property
   */
  public HolidayCalendar getHolidayCalendar() {
    return holidayCalendar;
  }

  /**
   * Sets the holidayCalendar.
   * @param holidayCalendar  the new value of the property
   */
  public void setHolidayCalendar(HolidayCalendar holidayCalendar) {
    this.holidayCalendar = holidayCalendar;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the dayCount.
   * @return the value of the property
   */
  public DayCount getDayCount() {
    return dayCount;
  }

  /**
   * Sets the dayCount.
   * @param dayCount  the new value of the property
   */
  public void setDayCount(DayCount dayCount) {
    this.dayCount = dayCount;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the objects.
   * @return the value of the property
   */
  public List<Object> getObjects() {
    return objects;
  }

  /**
   * Sets the objects.
   * @param objects  the new value of the property
   */
  public void setObjects(List<Object> objects) {
    this.objects = objects;
  }

  //-----------------------------------------------------------------------
  @Override
  public MockSerBean clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      MockSerBean other = (MockSerBean) obj;
      return JodaBeanUtils.equal(getBdConvention(), other.getBdConvention()) &&
          JodaBeanUtils.equal(getHolidayCalendar(), other.getHolidayCalendar()) &&
          JodaBeanUtils.equal(getDayCount(), other.getDayCount()) &&
          JodaBeanUtils.equal(getObjects(), other.getObjects());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getBdConvention());
    hash = hash * 31 + JodaBeanUtils.hashCode(getHolidayCalendar());
    hash = hash * 31 + JodaBeanUtils.hashCode(getDayCount());
    hash = hash * 31 + JodaBeanUtils.hashCode(getObjects());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("MockSerBean{");
    buf.append("bdConvention").append('=').append(getBdConvention()).append(',').append(' ');
    buf.append("holidayCalendar").append('=').append(getHolidayCalendar()).append(',').append(' ');
    buf.append("dayCount").append('=').append(getDayCount()).append(',').append(' ');
    buf.append("objects").append('=').append(JodaBeanUtils.toString(getObjects()));
    buf.append('}');
    return buf.toString();
  }

  //-------------------------- AUTOGENERATED END --------------------------
}
