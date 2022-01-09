/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Implementation of {@link Range}.
 *
 * @param <V> type of the contained values.
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractRange<V extends Comparable> implements Range<V> {

  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    }
    if ((obj == null) || !(obj instanceof AbstractRange)) {
      return false;
    }
    AbstractRange<?> other = (AbstractRange<?>) obj;
    return Objects.equals(getMin(), other.getMin()) && Objects.equals(getMax(), other.getMax());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getMin(), getMax());
  }

  private String toString(V value) {

    if (value instanceof BigDecimal) {
      return ((BigDecimal) value).stripTrailingZeros().toPlainString();
    }
    return value.toString();
  }

  @Override
  public String toString() {

    StringBuilder buffer = new StringBuilder();
    V min = getMin();
    if (min == null) {
      buffer.append(BOUND_START_EXCLUSIVE);
      buffer.append(MIN_UNBOUND);
    } else {
      buffer.append(BOUND_START_INCLUSIVE);
      buffer.append(toString(min));
    }
    V max = getMax();
    buffer.append(BOUND_SEPARATOR);
    if (max == null) {
      buffer.append(MAX_UNBOUND);
      buffer.append(BOUND_END_EXCLUSIVE);
    } else {
      buffer.append(toString(max));
      buffer.append(BOUND_END_INCLUSIVE);
    }
    return buffer.toString();
  }

}
