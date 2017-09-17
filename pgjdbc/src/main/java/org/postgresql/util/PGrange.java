/*
 * Copyright (c) 2017, PostgreSQL Global Development Group
 * See the LICENSE file in the project root for more information.
 */

package org.postgresql.util;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Abstract base class for all range types.
 *
 * @param <T> the type of element in the range
 */
public abstract class PGrange<T extends Serializable> extends PGobject implements Serializable, Cloneable {

  /**
   * Both the lower and the upper bound are set.
   */
  static final int EMPTY = 1;

  /**
   * Both the lower and the upper bound are set.
   */
  static final int CLOSED = 2;

  /**
   * The lower bound is not set but the upper bound is set.
   */
  static final int LOWER_OPEN = 8;

  /**
   * The lower bound is set but the upper bound is not set.
   */
  static final int UPPER_OPEN = 18;

  /**
   * Neither to lower nor the upper bound are set.
   */
  static final int BOTH_OPEN = 24;

  protected boolean lowerInclusive;
  protected boolean upperInclusive;
  protected T lowerBound;
  protected T upperBound;

  PGrange(T lowerBound, boolean lowerInclusive, T upperBound, boolean upperInclusive) {
    this.lowerBound = lowerBound;
    this.lowerInclusive = lowerInclusive;
    this.upperBound = upperBound;
    this.upperInclusive = upperInclusive;
  }

  PGrange(T lowerBound, T upperBound) {
    this(lowerBound, true, upperBound, false);
  }

  PGrange() {
    this(null, null);
  }

  /**
   * Initialize a range with a given range string representation.
   *
   * @param value String represented range (e.g. '[1,3)')
   * @throws SQLException Is thrown if the string representation has an unknown format
   * @see #setValue(String)
   */
  PGrange(String value) throws SQLException {
    setValue(value);
  }

  /**
   * Checks if the lower bound is inclusive.
   *
   * @return {@code true} if the lower bound is inclusive,
   *         {@code false} if the lower bound is exclusive
   */
  public boolean isLowerInclusive() {
    return this.lowerInclusive;
  }

  /**
   * Sets the lower bound inclusive.
   *
   * @param lowerInclusive {@code true} to make the lower bound is inclusive,
   *                       {@code false} to make the lower bound is exclusive
   */
  public void setLowerInclusive(boolean lowerInclusive) {
    this.lowerInclusive = lowerInclusive;
  }

  /**
   * Checks if the upper bound is inclusive.
   *
   * @return {@code true} if the upper bound is inclusive,
   *         {@code false} if the upper bound is exclusive
   */
  public boolean isUpperInclusive() {
    return this.upperInclusive;
  }

  /**
   * Sets the upper bound inclusive.
   *
   * @param upperInclusive {@code true} to make the upper bound is inclusive,
   *                       {@code false} to make the upper bound is exclusive
   */
  public void setUpperInclusive(boolean upperInclusive) {
    this.upperInclusive = upperInclusive;
  }

  /**
   * Return the lower bound.
   *
   * @return the lower bound, {@code null} if the lower bound is infinite
   * @see #isLowerInfinite()
   */
  public T getLowerBound() {
    return this.lowerBound;
  }

  /**
   * Sets the lower bound.
   *
   * @return the lower bound to set,
   *         {@code null} to set the lower bound to infinite
   */
  public void setLowerBound(T lowerBound) {
    this.lowerBound = lowerBound;
  }

  /**
   * Return the upper bound.
   *
   * @return the upper bound, {@code null} if the upper bound is infinite
   * @see #isUpperInfinite()
   */
  public T getUpperBound() {
    return this.upperBound;
  }

  /**
   * Sets the upper bound.
   *
   * @return the upper bound to set,
   *         {@code null} to set the upper bound to infinite
   */
  public void setUpperBound(T upperBound) {
    this.upperBound = upperBound;
  }

  /**
   * Checks if the lower bound is infinite.
   *
   * @return {@code true} if the lower bound is infinite,
   *         {@code false} if the lower bound is set
   */
  public boolean isLowerInfinite() {
    return this.lowerBound == null;
  }

  /**
   * Checks if the upper bound is infinite.
   *
   * @return {@code true} if the upper bound is infinite,
   *         {@code false} if the upper bound is set
   */
  public boolean isUpperInfinite() {
    return this.upperBound == null;
  }

  @Override
  public String getValue() {
    return (this.lowerInclusive ? '[' : '(')
            + (this.lowerBound == null ? "" : this.serializeBound(this.lowerBound))
            + ','
            + (this.upperBound == null ? "" : this.serializeBound(this.upperBound))
            + (this.upperInclusive ? ']' : ')');
  }

  /**
   * Returns a string representation in the text protocol of a value in the range.
   *
   * @param value the value in the range, not {@code null}
   * @return the string representation of {@value value}
   */
  protected abstract String serializeBound(T value);

  @Override
  public void setValue(String value) throws SQLException {
    super.setValue(value);
    if (value.length() < 2) {
      throw new PSQLException(
              GT.tr("Conversion to type {0} failed: {1}.", type, value),
              PSQLState.DATA_TYPE_MISMATCH);
    }
    if ("empty".equals(value)) {
      this.lowerInclusive = false;
      this.upperInclusive = false;
      this.lowerBound = null;
      this.upperBound = null;
      return;
    }


    if (value.charAt(0) == '[') {
      this.lowerInclusive = true;
    } else if (value.charAt(0) == '(') {
      this.lowerInclusive = false;
    } else {
      throw new PSQLException(
              GT.tr("Conversion to type {0} failed: {1}.", type, value),
              PSQLState.DATA_TYPE_MISMATCH);
    }

    if (value.charAt(value.length() - 1) == ']') {
      this.upperInclusive = true;
    } else if (value.charAt(value.length() - 1) == ')') {
      this.upperInclusive = false;
    } else {
      throw new PSQLException(
              GT.tr("Conversion to type {0} failed: {1}.", type, value),
              PSQLState.DATA_TYPE_MISMATCH);
    }

    PGtokenizer t = new PGtokenizer(value.substring(1, value.length() - 1), ',');
    if (t.getSize() == 0 || t.getSize() > 2) {
      throw new PSQLException(
          GT.tr("Conversion to type {0} failed: {1}.", type, value),
          PSQLState.DATA_TYPE_MISMATCH);
    }

    try {
      String token = t.getToken(0);
      if (token.isEmpty()) {
        this.lowerBound = null;
      } else {
        this.lowerBound = parseToken(token);
      }
      if (t.getSize() > 1) {
        this.upperBound = parseToken(t.getToken(1));
      } else {
        this.upperBound = null;
      }
    } catch (RuntimeException e) {
      throw new PSQLException(
              GT.tr("Conversion to type {0} failed: {1}.", type, value),
              PSQLState.DATA_TYPE_MISMATCH);
    }
  }

  protected abstract T parseToken(String token);

  /**
   * Returns the range type tag used at the start of the binary representation.
   *
   * @return the range type tag
   */
  protected int getTag() {
    if (this.isEmpty()) {
      return EMPTY;
    }
    if (this.isLowerInfinite()) {
      return this.isUpperInfinite() ? BOTH_OPEN : LOWER_OPEN;
    } else {
      return this.isUpperInfinite() ? UPPER_OPEN : CLOSED;
    }
  }

  boolean isEmpty() {
    return nullSafeEquals(this.upperBound, this.lowerBound)
            && (!this.lowerInclusive || !this.upperInclusive);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (obj.getClass() != this.getClass()) {
      return true;
    }
    PGrange<T> other = (PGrange<T>) obj;
    return this.lowerInclusive == other.lowerInclusive
            && this.upperInclusive == other.upperInclusive
            && nullSafeEquals(this.lowerBound, other.lowerBound)
            && nullSafeEquals(this.upperBound, other.upperBound);
  }

  protected boolean nullSafeEquals(T o1, T o2) {
    if (o1 == null) {
      return o2 == null;
    } else {
      return o1.equals(o2);
    }
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.lowerInclusive ? 1 : 0);
    result = 31 * result + (this.upperInclusive ? 1 : 0);
    result = 31 * result + nullSafeHashCode(this.lowerBound);
    result = 31 * result + nullSafeHashCode(this.upperBound);
    return result;
  }

  private static int nullSafeHashCode(Object o) {
    return o == null ? 0 : o.hashCode();
  }

}
