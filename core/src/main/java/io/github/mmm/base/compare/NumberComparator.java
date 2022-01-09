/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.compare;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;

import io.github.mmm.base.impl.NumberHelper;

/**
 * Implementation of {@link Comparator} for {@link Number}s capable to compare different types.
 *
 * @since 1.0.0
 */
public class NumberComparator implements Comparator<Number> {

  /** The singleton instance. */
  public static final NumberComparator INSTANCE = new NumberComparator();

  /**
   * The constructor.
   */
  public NumberComparator() {

  }

  @Override
  public int compare(Number arg1, Number arg2) {

    if (arg1 == arg2) {
      return 0;
    } else if (arg1 == null) {
      return -1;
    } else if (arg2 == null) {
      return 1;
    } else if (arg1 instanceof BigDecimal) {
      return ((BigDecimal) arg1).compareTo(NumberHelper.toBigDecimal(arg2));
    } else if (arg1 instanceof BigInteger) {
      if (arg2 instanceof BigDecimal) {
        return (new BigDecimal((BigInteger) arg1).compareTo((BigDecimal) arg2));
      } else {
        return ((BigInteger) arg1).compareTo(NumberHelper.toBigInteger(arg2));
      }
    } else {
      double delta = arg1.doubleValue() - arg2.doubleValue();
      if (delta < 0) {
        return -1;
      } else if (delta > 0) {
        return 1;
      } else {
        return 0;
      }
    }
  }

}
