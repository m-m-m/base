/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Helper for {@link Number}.
 */
public final class NumberHelper {

  private NumberHelper() {

  }

  /**
   * @param value the {@link Number}.
   * @return the given {@link Number} converted to {@link BigDecimal}.
   */
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

  /**
   * @param value the {@link Number}.
   * @return the given {@link Number} converted to {@link BigInteger}.
   */
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
