/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.util.Objects;

/**
 * Implementation of {@link Range}.
 *
 * @param <V> type of the contained values.
 * @since 1.0.0
 */
public class GenericRange<V> implements Range<V> {

  private final V min;

  private final V max;

  /**
   * The constructor.
   *
   * @param min - see {@link #getMin()}. To create an open range use the minimum value.
   * @param max - see {@link #getMax()}. To create an open range use the maximum value.
   */
  public GenericRange(V min, V max) {

    super();
    if ((min != null) && (max != null)) {
      int delta = getComparator().compare(min, max);
      if (delta > 0) {
        throw new IllegalArgumentException(min + " !<" + max);
      }
    }
    this.min = min;
    this.max = max;
  }

  @Override
  public V getMin() {

    return this.min;
  }

  @Override
  public V getMax() {

    return this.max;
  }

  /**
   * @param minimum the new {@link #getMin() minimum}.
   * @return a new {@link GenericRange} where the {@link #getMin() minimum} is set to the given {@code minimum} value.
   */
  public GenericRange<V> withMin(V minimum) {

    if (Objects.equals(this.min, minimum)) {
      return this;
    }
    return new GenericRange<>(minimum, this.max);
  }

  /**
   * @param maximum the new {@link #getMax() maximum}.
   * @return a new {@link GenericRange} where the {@link #getMax() maximum} is set to the given {@code maximum} value.
   */
  public GenericRange<V> withMax(V maximum) {

    if (Objects.equals(this.max, maximum)) {
      return this;
    }
    return new GenericRange<>(this.min, maximum);
  }

  @Override
  public final boolean equals(Object obj) {

    if (obj == this) {
      return true;
    }
    if ((obj == null) || !(obj instanceof GenericRange)) {
      return false;
    }
    GenericRange<?> other = (GenericRange<?>) obj;
    return Objects.equals(this.min, other.min) && Objects.equals(this.max, other.max);
  }

  @Override
  public final int hashCode() {

    return Objects.hash(this.min, this.max);
  }

  @Override
  public String toString() {

    StringBuilder buffer = new StringBuilder();
    buffer.append('[');
    if (this.min == null) {
      buffer.append(MIN_UNBOUND);
    } else {
      buffer.append(this.min);
    }
    buffer.append(',');
    if (this.max == null) {
      buffer.append(MAX_UNBOUND);
    } else {
      buffer.append(this.max);
    }
    buffer.append(']');
    return buffer.toString();
  }
}
