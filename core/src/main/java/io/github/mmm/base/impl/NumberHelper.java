/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

/// Helper for [Number].
public final class NumberHelper {

  private NumberHelper() {

  }

  /// @param value the [Number].
  /// @return the given [Number] converted to [BigDecimal].
  public static BigDecimal toBigDecimal(Number value) {

    if (value instanceof BigDecimal) {
      return (BigDecimal) value;
    } else if (value instanceof BigInteger) {
      return new BigDecimal((BigInteger) value);
    } else if (isNonDecimalNumber(value)) {
      return BigDecimal.valueOf(value.longValue());
    } else {
      return BigDecimal.valueOf(value.doubleValue());
    }
  }

  private static boolean isNonDecimalNumber(Number value) {

    return (value instanceof Long) || (value instanceof Integer) || (value instanceof Short) || (value instanceof Byte);
  }

  /// @param value the [Number].
  /// @return the given [Number] converted to [BigInteger].
  public static BigInteger toBigInteger(Number value) {

    if (value instanceof BigInteger) {
      return (BigInteger) value;
    } else if (value instanceof BigDecimal) {
      return ((BigDecimal) value).toBigInteger();
    } else {
      return BigInteger.valueOf(value.longValue());
    }
  }

}
