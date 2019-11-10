/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.util.Comparator;
import java.util.Objects;

import io.github.mmm.base.compare.NumberComparator;

/**
 * This class extends {@link Range} to allow mixed types of {@link Number} to be used.
 *
 * @since 7.1.0
 */
public class NumberRange extends GenericRange<Number> {

  /**
   * The constructor.
   *
   * @param min - see {@link #getMin()}. To create an open range use the minimum value.
   * @param max - see {@link #getMax()}. To create an open range use the maximum value.
   */
  public NumberRange(Number min, Number max) {

    super(min, max);
  }

  @Override
  public Comparator<? super Number> getComparator() {

    return NumberComparator.INSTANCE;
  }

  @Override
  public NumberRange withMin(Number minimum) {

    if (Objects.equals(getMin(), minimum)) {
      return this;
    }
    return new NumberRange(minimum, getMax());
  }

  @Override
  public NumberRange withMax(Number maximum) {

    if (Objects.equals(getMax(), maximum)) {
      return this;
    }
    return new NumberRange(getMin(), maximum);
  }

}
