/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.number;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A {@link NumberType} represents a specific {@link Number} {@link Class}. It allows to check attributes like
 * {@link #isDecimal()} or {@link #getExactness()}. <br>
 * Further it acts as factory to create according numbers {@link #valueOf(String) from string} or
 * {@link #valueOf(Number) from number}. <br>
 * This is a class and NOT an {@link Enum} to be extensible.
 *
 * @param <N> type of the {@link #getType() represented number-class}.
 *
 * @since 1.0.0
 */
public abstract class NumberType<N extends Number> {

  /** The {@link NumberType} for {@link Byte}. */
  public static final NumberType<Byte> BYTE = new NumberType<>(Byte.class, 1, Byte.valueOf(Byte.MIN_VALUE),
      Byte.valueOf(Byte.MAX_VALUE)) {

    @Override
    protected Byte convert(Number number) {

      return Byte.valueOf(number.byteValue());
    }

    @Override
    public Byte valueOf(String number) {

      return Byte.valueOf(number);
    }

  };

  /** The {@link NumberType} for {@link Short}. */
  public static final NumberType<Short> SHORT = new NumberType<>(Short.class, 2, Short.valueOf(Short.MIN_VALUE),
      Short.valueOf(Short.MAX_VALUE)) {

    @Override
    protected Short convert(Number number) {

      return Short.valueOf(number.shortValue());
    }

    @Override
    public Short valueOf(String number) {

      return Short.valueOf(number);
    }

  };

  /** The {@link NumberType} for {@link Integer}. */
  public static final NumberType<Integer> INTEGER = new NumberType<>(Integer.class, 3,
      Integer.valueOf(Integer.MIN_VALUE), Integer.valueOf(Integer.MAX_VALUE)) {

    @Override
    protected Integer convert(Number number) {

      return Integer.valueOf(number.intValue());
    }

    @Override
    public Integer valueOf(String number) {

      return Integer.valueOf(number);
    }

  };

  /** The {@link NumberType} for {@link Long}. */
  public static final NumberType<Long> LONG = new NumberType<>(Long.class, 4, Long.valueOf(Long.MIN_VALUE),
      Long.valueOf(Long.MAX_VALUE)) {

    @Override
    protected Long convert(Number number) {

      return Long.valueOf(number.longValue());
    }

    @Override
    public Long valueOf(String number) {

      return Long.valueOf(number);
    }

  };

  /** The {@link NumberType} for {@link Float}. */
  public static final NumberType<Float> FLOAT = new NumberType<>(Float.class, 5, Float.valueOf(Float.MIN_VALUE),
      Float.valueOf(Float.MAX_VALUE)) {

    @Override
    protected Float convert(Number number) {

      return Float.valueOf(number.floatValue());
    }

    @Override
    public Float valueOf(String number) {

      return Float.valueOf(number);
    }

  };

  /** The {@link NumberType} for {@link Double}. */
  public static final NumberType<Double> DOUBLE = new NumberType<>(Double.class, 6, Double.valueOf(Double.MIN_VALUE),
      Double.valueOf(Double.MAX_VALUE)) {

    @Override
    protected Double convert(Number number) {

      return Double.valueOf(number.doubleValue());
    }

    @Override
    public Double valueOf(String number) {

      return Double.valueOf(number);
    }

  };

  /** The {@link NumberType} for {@link BigInteger}. */
  public static final NumberType<BigInteger> BIG_INTEGER = new NumberType<>(BigInteger.class, 7, null, null) {

    @Override
    protected BigInteger convert(Number number) {

      if (number instanceof BigDecimal) {
        return ((BigDecimal) number).toBigInteger();
      }
      return BigInteger.valueOf(number.longValue());
    }

    @Override
    public BigInteger valueOf(String number) {

      return new BigInteger(number);
    }

    BigInteger doAdd(Number summand1, Number summand2) {

      return toBigDecimal(summand1).add(toBigDecimal(summand2)).toBigInteger();
    }

    BigInteger doSubtract(Number minuend, Number subtrahend) {

      return toBigDecimal(minuend).subtract(toBigDecimal(subtrahend)).toBigInteger();
    }

    BigInteger doMultiply(Number multiplier, Number multiplicand) {

      return toBigDecimal(multiplier).multiply(toBigDecimal(multiplicand)).toBigInteger();
    }

    BigInteger doDivide(Number dividend, Number divisor) {

      return toBigDecimal(dividend).divide(toBigDecimal(divisor)).toBigInteger();
    }

  };

  /** The {@link NumberType} for {@link BigDecimal}. */
  public static final NumberType<BigDecimal> BIG_DECIMAL = new NumberType<>(BigDecimal.class, 8, null, null) {

    @Override
    protected BigDecimal convert(Number number) {

      if (number instanceof BigInteger) {
        return new BigDecimal((BigInteger) number);
      }
      return BigDecimal.valueOf(number.doubleValue());
    }

    @Override
    public BigDecimal valueOf(String number) {

      return new BigDecimal(number);
    }

    BigDecimal doAdd(Number summand1, Number summand2) {

      return toBigDecimal(summand1).add(toBigDecimal(summand2));
    }

    BigDecimal doSubtract(Number minuend, Number subtrahend) {

      return toBigDecimal(minuend).subtract(toBigDecimal(subtrahend));
    }

    BigDecimal doMultiply(Number multiplier, Number multiplicand) {

      return toBigDecimal(multiplier).multiply(toBigDecimal(multiplicand));
    }

    BigDecimal doDivide(Number dividend, Number divisor) {

      return toBigDecimal(dividend).divide(toBigDecimal(divisor));
    }
  };

  private final Class<N> type;

  private final int exactness;

  private final N min;

  private final N max;

  private final N zero;

  private final N one;

  /**
   * The constructor.
   *
   * @param type the {@link #getType() type}.
   * @param exactness the {@link #getExactness() exactness}.
   * @param min the {@link #getMin() minimum value}.
   * @param max the {@link #getMax() maximum value}.
   */
  protected NumberType(Class<N> type, int exactness, N min, N max) {

    super();
    this.type = type;
    this.exactness = exactness;
    this.min = min;
    this.max = max;
    this.zero = valueOf(Byte.valueOf((byte) 0));
    this.one = valueOf(Byte.valueOf((byte) 1));
  }

  /**
   * @return the {@link Class} reflecting the {@link Number} represented by this {@link NumberType}.
   */
  public Class<N> getType() {

    return this.type;
  }

  /**
   * @param number is the number to convert.
   * @return the converted number.
   */
  protected abstract N convert(Number number);

  /**
   * This method determines if the represented {@link #getType() Number} is a decimal value. <br>
   * E.g. {@link Double}, {@link Float}, or {@link java.math.BigDecimal} represent decimal values while {@link Integer}
   * or {@link Long} are NOT decimal.
   *
   * @return {@code true} if the {@link #getType() number type} represents a decimal value.
   */
  public final boolean isDecimal() {

    if (this.type == Double.class) {
      return true;
    } else if (this.type == Float.class) {
      return true;
    } else if (this.type == BigDecimal.class) {
      return true;
    }
    return false;
  }

  /**
   * This method gets the and the given {@code otherType}. <br>
   * <b>ATTENTION:</b><br>
   * Some types such as {@link Double} and {@link java.math.BigInteger} are NOT really comparable so the exactness
   * difference might only make sense if the compared {@link NumberType types} are both {@link #isDecimal() decimal} or
   * both {@link #isDecimal() non-decimal} (mathematical integers). However the order of typical types is:<br>
   *
   * @return the exactness of this {@link NumberType}, which is 1 for {@link Byte}, 2 for {@link Short}, 3 for
   *         {@link Integer}, 4 for {@link Long}, 5 for {@link Float}, 6 for {@link Double}, 7 for {@link BigInteger},
   *         and 8 for {@link BigDecimal}.
   */
  public int getExactness() {

    return this.exactness;
  }

  /**
   * @param number is the {@link Number} to convert.
   * @return the given {@link Number} converted to this {@link #getType() type}. This will be the same instance as
   *         {@code number} if the type already matches. Otherwise conversion may loose precision. See
   *         {@link #getExactness() exactness} to predict.
   */
  @SuppressWarnings("unchecked")
  public N valueOf(Number number) {

    if (number == null) {
      return null;
    }
    Class<? extends Number> givenNumberClass = number.getClass();
    if (this.type.equals(givenNumberClass)) {
      return (N) number;
    }
    return convert(number);
  }

  /**
   * @param number is the {@link Object#toString() string representation} of the {@link Number} to be parsed.
   * @return the parsed number of the according {@link #getType() type}.
   * @throws NumberFormatException if the given {@link String} has an invalid format for this {@link #getType() type}.
   */
  public abstract N valueOf(String number);

  /**
   * @return the minimum allowed value. Will return {@code null} in case of an unbounded type such as
   *         {@link java.math.BigInteger}.
   */
  public N getMin() {

    return this.min;
  }

  /**
   * @return the maximum allowed value. Will return {@code null} in case of an unbounded type such as
   *         {@link java.math.BigInteger}.
   */
  public N getMax() {

    return this.max;
  }

  /**
   * @return the value zero ({@code 0}).
   */
  public N getZero() {

    return this.zero;
  }

  /**
   * @return the value one ({@code 1}).
   */
  public N getOne() {

    return this.one;
  }

  /**
   * @param summand1 the first summand.
   * @param summand2 the second summand.
   * @return the sum of all given summands. If both arguments are {@code null} the result will be {@code null}.
   *         Otherwise {@code null} behaves like the neutral element.
   */
  public N add(Number summand1, Number summand2) {

    if (summand1 == null) {
      return valueOf(summand2);
    } else if (summand2 == null) {
      return valueOf(summand1);
    }
    return doAdd(summand1, summand2);
  }

  N doAdd(Number summand1, Number summand2) {

    return valueOf(Double.valueOf(summand1.doubleValue() + summand2.doubleValue()));
  }

  /**
   * @param minuend the value to be subtracted.
   * @param subtrahend the value to subtract.
   * @return the difference of the {@code minuend} and the {@code subtrahend}. If both arguments are {@code null} the
   *         result will be {@code null}. Otherwise {@code null} behaves like the neutral element.
   */
  public N subtract(Number minuend, Number subtrahend) {

    if (subtrahend == null) {
      return valueOf(minuend);
    } else if (minuend == null) {
      minuend = this.zero;
    }
    return doSubtract(minuend, subtrahend);
  }

  N doSubtract(Number minuend, Number subtrahend) {

    return valueOf(Double.valueOf(minuend.doubleValue() - subtrahend.doubleValue()));
  }

  /**
   * @param multiplier the first factor.
   * @param multiplicand the second factor.
   * @return the product of the given factors. If both arguments are {@code null} the result will be {@code null}.
   *         Otherwise {@code null} behaves like the neutral element.
   */
  public N multiply(Number multiplier, Number multiplicand) {

    if (multiplier == null) {
      return valueOf(multiplicand);
    } else if (multiplicand == null) {
      return valueOf(multiplier);
    }
    return doMultiply(multiplier, multiplicand);
  }

  N doMultiply(Number multiplier, Number multiplicand) {

    return valueOf(Double.valueOf(multiplier.doubleValue() * multiplicand.doubleValue()));
  }

  /**
   * @param dividend the value to be divided.
   * @param divisor the value to divide by.
   * @return the quotient of the given values. If both arguments are {@code null} the result will be {@code null}.
   *         Otherwise {@code null} behaves like the neutral element.
   */
  public N divide(Number dividend, Number divisor) {

    if (divisor == null) {
      return valueOf(dividend);
    } else if (dividend == null) {
      dividend = this.one;
    }
    return doDivide(dividend, divisor);
  }

  N doDivide(Number dividend, Number divisor) {

    return valueOf(Double.valueOf(dividend.doubleValue() / divisor.doubleValue()));
  }

  @Override
  public String toString() {

    return this.type.getSimpleName();
  }

  private static BigDecimal toBigDecimal(Number number) {

    if (number instanceof BigDecimal) {
      return (BigDecimal) number;
    } else if (number instanceof BigInteger) {
      return new BigDecimal((BigInteger) number);
    } else {
      return BigDecimal.valueOf(number.doubleValue());
    }
  }

  /**
   * @param <N> type of the {@link Number}.
   * @param type the {@link Class} reflecting the {@link Number}.
   * @return the {@link NumberType} for the given {@link Class} of {@code null} if not a standard Java {@link Number}.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static <N extends Number> NumberType<N> of(Class<N> type) {

    NumberType result = null;
    if ((type == int.class) || (type == Integer.class)) {
      result = INTEGER;
    } else if ((type == long.class) || (type == Long.class)) {
      result = LONG;
    } else if ((type == double.class) || (type == Double.class)) {
      result = DOUBLE;
    } else if (type == BigDecimal.class) {
      result = BIG_DECIMAL;
    } else if (type == BigInteger.class) {
      result = BIG_INTEGER;
    } else if ((type == float.class) || (type == Float.class)) {
      result = FLOAT;
    } else if ((type == short.class) || (type == Short.class)) {
      result = SHORT;
    } else if ((type == byte.class) || (type == Byte.class)) {
      result = BYTE;
    }
    return result;
  }

}
