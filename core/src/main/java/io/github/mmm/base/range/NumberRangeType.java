/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.util.Comparator;

import io.github.mmm.base.compare.NumberComparator;
import io.github.mmm.base.number.NumberType;

/**
 * Implementation of {@link NumberRange} as immutable type.
 *
 * @param <N> type of the {@link Number} value. Unfortunately {@link Number} does not implement {@link Comparable}.
 * @since 1.0.0
 */
public class NumberRangeType<N extends Number & Comparable<?>> extends RangeType<N> implements NumberRange<N> {

  private NumberType<N> type;

  /**
   * The constructor.
   *
   * @param min - see {@link #getMin()}. To create an open range use the minimum value.
   * @param max - see {@link #getMax()}. To create an open range use the maximum value.
   */
  public NumberRangeType(N min, N max) {

    this(null, min, max);
  }

  /**
   * The constructor.
   *
   * @param type the {@link #getType() number type}.
   * @param min - see {@link #getMin()}. To create an open range use the minimum value.
   * @param max - see {@link #getMax()}. To create an open range use the maximum value.
   */
  public NumberRangeType(NumberType<N> type, N min, N max) {

    super(min, max);
    if (type == null) {
      getType();
    } else {
      this.type = type;
    }
  }

  @Override
  public NumberType<N> getType() {

    if (this.type == null) {
      this.type = NumberRange.super.getType();
    }
    return this.type;
  }

  @Override
  public Comparator<? super Number> getComparator() {

    return NumberComparator.INSTANCE;
  }

  @Override
  protected Range<N> create(N minimum, N maximum) {

    return new NumberRangeType<>(minimum, maximum);
  }

}
