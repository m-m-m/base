/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import io.github.mmm.base.number.NumberType;

/// Implementation of [NumberRange] as mutable `AbstractRangeBean bean`.
///
/// @param <V> type of the contained values.
/// @since 1.0.0
public class NumberRangeBean<V extends Number & Comparable<?>> extends AbstractRangeBean<V> implements NumberRange<V> {

  private NumberType<V> type;

  /// The constructor.
  ///
  /// @param type the [number type][#getType()].
  public NumberRangeBean(NumberType<V> type) {

    super();
    this.type = type;
  }

  /// The constructor.
  ///
  /// @param type the [number type][#getType()].
  /// @param min the [minimum][#getMin()].
  /// @param max the [maximum][#getMax()].
  public NumberRangeBean(NumberType<V> type, V min, V max) {

    super(min, max);
    this.type = type;
  }

  @Override
  public NumberType<V> getType() {

    if (this.type == null) {
      this.type = NumberRange.super.getType();
    }
    return this.type;
  }

}
