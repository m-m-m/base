/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;

/**
 * Implementation of {@link Range}.
 *
 * @param <V> type of the contained values.
 * @since 1.0.0
 */
public abstract class AbstractRange<V extends Comparable<?>> implements Range<V> {

  @Override
  public Range<V> intersection(Range<V> range) {

    if ((range == null) || (range == RangeType.UNBOUNDED)) {
      return this;
    } else if (range == RangeType.INVALID) {
      return range;
    }
    Comparator<? super V> comparator = getComparator();
    V min1 = getMin();
    V min2 = range.getMin();
    V min = min1;
    if (min == null) {
      min = min2;
    } else if (min2 != null) {
      int delta = comparator.compare(min, min2);
      if (delta <= 0) { // min > min2?
        min = min2;
      }
    }
    V max1 = getMax();
    V max2 = range.getMax();
    V max = max1;
    if (max == null) {
      max = max2;
    } else if (max2 != null) {
      int delta = comparator.compare(max, max2);
      if (delta >= 0) { // max < max2?
        max = max2;
      }
    }
    if (((min != min1) || (max != max1)) && (min != null) && (max != null)) {
      int delta = comparator.compare(min, max);
      if (delta > 0) {
        return Range.invalid();
      }
    }
    if ((min == min2) && (max == max2)) {
      return range;
    } else if ((min == min1) && (max == max1)) {
      return this;
    }
    return with(min, max);
  }

  @Override
  public Range<V> with(V minimum, V maximum) {

    if (Objects.equals(minimum, getMin()) && Objects.equals(maximum, getMax())) {
      return this;
    }
    return create(minimum, maximum);
  }

  /**
   * @param minimum the new {@link #getMin() minimum}.
   * @param maximum the new {@link #getMax() maximum}.
   * @return a new {@link Range} with the given boundaries.
   * @see #with(Comparable, Comparable)
   */
  protected abstract Range<V> create(V minimum, V maximum);

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
