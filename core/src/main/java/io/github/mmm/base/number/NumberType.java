/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.number;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.github.mmm.base.lang.ValueType;

/**
 * A {@link NumberType} represents a specific {@link Number} {@link Class}. It allows to check attributes like
 * {@link #isDecimal()} or {@link #getExactness()}. <br>
 * Further it acts as factory to create according numbers {@link #parse(String) from string} or {@link #valueOf(Number)
 * from number}. <br>
 * This is a class and NOT an {@link Enum} to be extensible.
 *
 * @param <N> type of the {@link #getType() represented number-class}.
 *
 * @since 1.0.0
 */
public abstract class NumberType<N extends Number> extends ValueType<N> {

  /** The {@link NumberType} for {@link Byte}. */
  public static final NumberType<Byte> BYTE = new NumberType<>(Byte.class, byte.class, 1, Byte.valueOf(Byte.MIN_VALUE),
      Byte.valueOf(Byte.MAX_VALUE)) {

    @Override
    protected Byte convert(Number number, boolean exact) {

      byte b = number.byteValue();
      if (exact) {
        if (b != number.doubleValue()) {
          return null;
        }
      }
      return Byte.valueOf(b);
    }

    @Override
    public Byte parse(String number, int radix) {

      return Byte.valueOf(number, radix);
    }

  };

  /** The {@link NumberType} for {@link Short}. */
  public static final NumberType<Short> SHORT = new NumberType<>(Short.class, short.class, 2,
      Short.valueOf(Short.MIN_VALUE), Short.valueOf(Short.MAX_VALUE)) {

    @Override
    protected Short convert(Number number, boolean exact) {

      short s = number.shortValue();
      if (exact) {
        if (s != number.doubleValue()) {
          return null;
        }
      }
      return Short.valueOf(s);
    }

    @Override
    public Short parse(String number, int radix) {

      return Short.valueOf(number, radix);
    }

  };

  /** The {@link NumberType} for {@link Integer}. */
  public static final NumberType<Integer> INTEGER = new NumberType<>(Integer.class, int.class, 3,
      Integer.valueOf(Integer.MIN_VALUE), Integer.valueOf(Integer.MAX_VALUE)) {

    @Override
    protected Integer convert(Number number, boolean exact) {

      int i = number.intValue();
      if (exact) {
        if (i != number.doubleValue()) {
          return null;
        }
      }
      return Integer.valueOf(i);
    }

    @Override
    public Integer parse(String number, int radix) {

      return Integer.valueOf(number, radix);
    }

  };

  /** The {@link NumberType} for {@link Long}. */
  public static final NumberType<Long> LONG = new NumberType<>(Long.class, long.class, 4, Long.valueOf(Long.MIN_VALUE),
      Long.valueOf(Long.MAX_VALUE)) {

    @Override
    protected Long convert(Number number, boolean exact) {

      long l = number.longValue();
      if (exact) {
        if (l != number.doubleValue()) {
          return null;
        }
      }
      return Long.valueOf(l);
    }

    @Override
    public Long parse(String number, int radix) {

      return Long.valueOf(number, radix);
    }

    public String format(Long number, int radix) {

      if (number == null) {
        return null;
      }
      return Long.toString(number.longValue(), radix);
    }

  };

  /** The {@link NumberType} for {@link Float}. */
  public static final NumberType<Float> FLOAT = new NumberType<>(Float.class, float.class, 5,
      Float.valueOf(-Float.MAX_VALUE), Float.valueOf(Float.MAX_VALUE), Float.valueOf(Float.NaN),
      Float.valueOf(Float.POSITIVE_INFINITY), Float.valueOf(Float.NEGATIVE_INFINITY)) {

    @Override
    protected Float convert(Number number, boolean exact) {

      float f = number.floatValue();
      if (exact) {
        if (Float.isNaN(f) || Float.isInfinite(f) || (f != number.doubleValue())) {
          return null;
        }
      }
      return Float.valueOf(f);
    }

    @Override
    public Float parse(String number, int radix) {

      if (radix == 16) {
        number = "0x" + number;
      } else if (radix != 10) {
        throw illegalRadixException(radix);
      }
      return Float.valueOf(number);
    }

    public String format(Float number, int radix) {

      if (number == null) {
        return null;
      }
      if (radix == 10) {
        return number.toString();
      } else if (radix == 16) {
        return Float.toHexString(number.floatValue());
      } else {
        throw illegalRadixException(radix);
      }
    }

  };

  /** The {@link NumberType} for {@link Double}. */
  public static final NumberType<Double> DOUBLE = new NumberType<>(Double.class, double.class, 6,
      Double.valueOf(-Double.MAX_VALUE), Double.valueOf(Double.MAX_VALUE), Double.valueOf(Double.NaN),
      Double.valueOf(Double.POSITIVE_INFINITY), Double.valueOf(Double.NEGATIVE_INFINITY)) {

    @Override
    protected Double convert(Number number, boolean exact) {

      BigDecimal bd = null;
      if (exact) {
        if (number instanceof BigDecimal) {
          bd = (BigDecimal) number;
          int precision = bd.precision();
          if (precision >= 19) {
            return null;
          }
        } else if (number instanceof BigInteger) {
          int bits = ((BigInteger) number).bitCount();
          if (bits >= 54) {
            return null;
          }
        }
      }
      double d = number.doubleValue();
      if (exact) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
          return null;
        } else if ((bd != null) && BigDecimal.valueOf(d).compareTo(bd) != 0) {
          return null;
        }
      }
      return Double.valueOf(d);
    }

    @Override
    public Double parse(String number, int radix) {

      if (radix == 16) {
        number = "0x" + number;
      } else if (radix != 10) {
        throw illegalRadixException(radix);
      }
      return Double.valueOf(number);
    }

    public String format(Double number, int radix) {

      if (number == null) {
        return null;
      }
      if (radix == 10) {
        return number.toString();
      } else if (radix == 16) {
        return Double.toHexString(number.doubleValue());
      } else {
        throw illegalRadixException(radix);
      }
    }

  };

  /** The {@link NumberType} for {@link BigInteger}. */
  public static final NumberType<BigInteger> BIG_INTEGER = new NumberType<>(BigInteger.class, null, 7, null, null) {

    @Override
    protected BigInteger convert(Number number, boolean exact) {

      if (number instanceof BigDecimal) {
        BigDecimal bd = (BigDecimal) number;
        if (exact && bd.stripTrailingZeros().scale() > 0) {
          return null;
        }
        return bd.toBigInteger();
      }
      return BigInteger.valueOf(number.longValue());
    }

    @Override
    public BigInteger parse(String number, int radix) {

      return new BigInteger(number, radix);
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

    public String format(BigInteger number, int radix) {

      if (number == null) {
        return null;
      }
      return number.toString(radix);
    }

  };

  /** The {@link NumberType} for {@link BigDecimal}. */
  public static final NumberType<BigDecimal> BIG_DECIMAL = new NumberType<>(BigDecimal.class, null, 8, null, null) {

    @Override
    protected BigDecimal convert(Number number, boolean exact) {

      return toBigDecimal(number);
    }

    @Override
    public BigDecimal parse(String number, int radix) {

      if (radix != 10) {
        throw illegalRadixException(radix);
      }
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

    public String format(BigDecimal number, int radix) {

      if (number == null) {
        return null;
      }
      if (radix != 10) {
        throw illegalRadixException(radix);
      }
      return number.toString();
    }
  };

  private static final NumberType<?>[] TYPES = { null, BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, BIG_INTEGER,
  BIG_DECIMAL };

  final int exactness;

  private final N min;

  private final N max;

  private final N zero;

  private final N one;

  private final N nan;

  private final N positiveInfinity;

  private final N negativeInfinity;

  /**
   * The constructor.
   *
   * @param type the {@link #getType() type}.
   * @param primitiveType the {@link #getPrimitiveType() primitive type}.
   * @param exactness the {@link #getExactness() exactness}.
   * @param min the {@link #getMin() minimum value}.
   * @param max the {@link #getMax() maximum value}.
   */
  protected NumberType(Class<N> type, Class<N> primitiveType, int exactness, N min, N max) {

    this(type, primitiveType, exactness, min, max, null, null, null);
  }

  /**
   * The constructor.
   *
   * @param type the {@link #getType() type}.
   * @param primitiveType the {@link #getPrimitiveType() primitive type}.
   * @param exactness the {@link #getExactness() exactness}.
   * @param min the {@link #getMin() minimum value}.
   * @param max the {@link #getMax() maximum value}.
   * @param nan the {@link #getNaN() not-a-number value}.
   * @param positiveInfinity the {@link #getPositiveInfinity() positive infinity value}.
   * @param negativeInfinity the {@link #getNegativeInfinity() negative infinity value}.
   */
  protected NumberType(Class<N> type, Class<N> primitiveType, int exactness, N min, N max, N nan, N positiveInfinity,
      N negativeInfinity) {

    super(type, primitiveType);
    this.exactness = exactness;
    this.min = min;
    this.max = max;
    this.zero = valueOf(Byte.valueOf((byte) 0));
    this.one = valueOf(Byte.valueOf((byte) 1));
    this.nan = nan;
    this.positiveInfinity = positiveInfinity;
    this.negativeInfinity = negativeInfinity;
  }

  /**
   * @param number is the number to convert.
   * @param exact {@code true} if {@code null} shall be returned in case the conversion looses precision, {@code false}
   *        otherwise (to always return the converted value).
   * @return the converted number.
   */
  protected abstract N convert(Number number, boolean exact);

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
   * This method determines if the represented {@link #getType() Number} is an imprecise decimal value. <br>
   * E.g. {@link Double}, {@link Float}, or {@link java.math.BigDecimal} represent decimal values while {@link Integer}
   * or {@link Long} are NOT decimal.
   *
   * @return {@code true} if the {@link #getType() number type} represents a decimal value.
   */
  public final boolean isImpreciseDecimal() {

    if (this.type == Double.class) {
      return true;
    } else if (this.type == Float.class) {
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
  public N valueOf(Number number) {

    return valueOf(number, false);
  }

  /**
   * @param number is the {@link Number} to convert.
   * @param exact {@code true} if {@code null} shall be returned in case the conversion looses precision, {@code false}
   *        otherwise (to always return the converted value).
   * @return the given {@link Number} converted to this {@link #getType() type}. This will be the same instance as
   *         {@code number} if the type already matches. Otherwise conversion may loose precision. See
   *         {@link #getExactness() exactness} to predict.
   */
  @SuppressWarnings("unchecked")
  public N valueOf(Number number, boolean exact) {

    if (number == null) {
      return null;
    }
    if (this.type.equals(number.getClass())) {
      return (N) number;
    }
    return convert(number, exact);
  }

  /**
   * @param number is the {@link Object#toString() string representation} of the {@link Number} to be parsed.
   * @return the parsed number of the according {@link #getType() type}.
   * @throws NumberFormatException if the given {@link String} has an invalid format for this {@link #getType() type}.
   */
  @Override
  public N parse(String number) throws NumberFormatException {

    return parse(number, 10);
  }

  /**
   * @param number is the {@link Object#toString() string representation} of the {@link Number} to be parsed.
   * @param radix the radix. Typically {@code 10} for decimal format, may be {@code 16} for hex, etc.
   * @return the parsed number of the according {@link #getType() type}.
   * @throws NumberFormatException if the given {@link String} has an invalid format for this {@link #getType() type}.
   * @throws IllegalArgumentException if the given {@code radix} is not supported. The radix {@code 10} is always
   *         supported, and {@code 16} is supported for types other than {@link #BIG_DECIMAL}.
   */
  public abstract N parse(String number, int radix) throws NumberFormatException;

  /**
   * @param number the {@link Number} to format a {@link String}.
   * @param radix the radix. Typically {@code 10} for decimal format, may be {@code 16} for hex, etc.
   * @return the given {@link Number} as {@link String}.
   * @throws IllegalArgumentException if the given {@code radix} is not supported. The radix {@code 10} is always
   *         supported, and {@code 16} is supported for types other than {@link #BIG_DECIMAL}.
   * @see Long#toString(long, int)
   * @see Integer#toString(int, int)
   * @see Double#toHexString(double)
   * @see BigInteger#toString(int)
   */
  public String format(N number, int radix) throws IllegalArgumentException {

    if (number == null) {
      return null;
    }
    // overridden for Long, BigInteger and decimal types
    return Integer.toString(number.intValue(), radix);
  }

  /**
   * <b>ATTENTION</b>:<br>
   * We define the minimum value as the smallest actual number that can be represented by this {@link NumberType}.
   * However, this will never be {@link Double#MIN_VALUE} or {@link Float#MIN_VALUE} as those are the smallest
   * <b>positive</b> nonzero values.
   *
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
   * @return the value NaN (not a number) or {@code null} if no such value exists for this {@link NumberType}.
   */
  public N getNaN() {

    return this.nan;
  }

  /**
   * @return the positive infinity value or {@code null} if no such value exists for this {@link NumberType}.
   * @see Double#POSITIVE_INFINITY
   */
  public N getPositiveInfinity() {

    return this.positiveInfinity;
  }

  /**
   * @return the negative infinity value or {@code null} if no such value exists for this {@link NumberType}.
   * @see Double#NEGATIVE_INFINITY
   */
  public N getNegativeInfinity() {

    return this.negativeInfinity;
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

  /**
   * Wraps a value within a range from {@code min} to {@code max} using a simplified approach. The following table give
   * you an easy overview of what this method does by examples:
   * <table border="1">
   * <tr>
   * <th>value</th>
   * <th>min</th>
   * <th>max</th>
   * <th>{@code wrap(value, min, max)}</th>
   * </tr>
   * <tr>
   * <td>5</td>
   * <td>0</td>
   * <td>10</td>
   * <td>5</td>
   * </tr>
   * <tr>
   * <td>11</td>
   * <td>0</td>
   * <td>10</td>
   * <td>0</td>
   * </tr>
   * <tr>
   * <td>12</td>
   * <td>0</td>
   * <td>10</td>
   * <td>1</td>
   * </tr>
   * <tr>
   * <td>15</td>
   * <td>0</td>
   * <td>10</td>
   * <td>4</td>
   * </tr>
   * <tr>
   * <td>20</td>
   * <td>0</td>
   * <td>10</td>
   * <td>9</td>
   * </tr>
   * <tr>
   * <td>21</td>
   * <td>0</td>
   * <td>10</td>
   * <td>10</td>
   * </tr>
   * <tr>
   * <td>22</td>
   * <td>0</td>
   * <td>10</td>
   * <td>10</td>
   * </tr>
   * <tr>
   * <td>5</td>
   * <td>-10</td>
   * <td>10</td>
   * <td>5</td>
   * </tr>
   * <tr>
   * <td>-11</td>
   * <td>-10</td>
   * <td>10</td>
   * <td>10</td>
   * </tr>
   * <tr>
   * <td>-12</td>
   * <td>-10</td>
   * <td>10</td>
   * <td>9</td>
   * </tr>
   * <tr>
   * <td>-21</td>
   * <td>-10</td>
   * <td>10</td>
   * <td>0</td>
   * </tr>
   * <tr>
   * <td>-30</td>
   * <td>-10</td>
   * <td>10</td>
   * <td>-9</td>
   * </tr>
   * <tr>
   * <td>-31</td>
   * <td>-10</td>
   * <td>10</td>
   * <td>-10</td>
   * </tr>
   * <tr>
   * <td>-32</td>
   * <td>-10</td>
   * <td>10</td>
   * <td>-10</td>
   * </tr>
   * <tr>
   * <td>{@code null}</td>
   * <td>*</td>
   * <td>*</td>
   * <td>{@code null}</td>
   * </tr>
   * <tr>
   * <td>&lt;«min»</td>
   * <td>«min»</td>
   * <td>{@code null}</td>
   * <td>«min»</td>
   * </tr>
   * <tr>
   * <td>&gt;«max»</td>
   * <td>{@code null}</td>
   * <td>«max»</td>
   * <td>«max»</td>
   * </tr>
   * <tr>
   * <td>«value»</td>
   * <td>{@code null}</td>
   * <td>{@code null}</td>
   * <td>«value»</td>
   * </tr>
   * </table>
   *
   * @param value the value to wrap.
   * @param minimum the minimum allowed value or {@code null} for no lower bound.
   * @param maximum the maximum allowed value or {@code null} for no upper bound.
   * @return the given {@code value} if within the given bounds. Otherwise a wrapped value within the given bounds. If
   *         the value lies further out of the bounds than the entire range, it will be set to {@code min} or
   *         {@code max} accordingly.
   */
  public N wrap(N value, Number minimum, Number maximum) {

    if (value == null) {
      return null;
    }
    if (minimum != null) {
      N delta = doSubtract(value, minimum);
      if (isNegative(delta)) { // value < min?
        N minValue = valueOf(minimum);
        if (maximum != null) {
          N result = add(maximum, add(delta, getOne()));
          if (isGreaterEqual(result, minValue) && isLessEqual(result, valueOf(maximum))) {
            return result;
          }
        }
        return minValue;
      }
    }
    if (maximum != null) {
      N delta = doSubtract(value, maximum);
      if (isPositive(delta)) { // value > max?
        N maxValue = valueOf(maximum);
        if (minimum != null) {
          N result = add(minimum, subtract(delta, getOne()));
          if (isGreaterEqual(result, valueOf(minimum)) && isLessEqual(result, maxValue)) {
            return result;
          }
        }
        return maxValue;
      }
    }
    return value;
  }

  /**
   * @param n1 the first {@link Number} to compare.
   * @param n2 the second {@link Number} to compare.
   * @return {@code true} if the first {@link Number} is greater than the second.
   */
  public boolean isGreater(N n1, N n2) {

    if ((n1 == null) || (n2 == null)) {
      return false;
    }
    return (compareTo(n1, n2) > 0);
  }

  /**
   * @param n1 the first {@link Number} to compare.
   * @param n2 the second {@link Number} to compare.
   * @return {@code true} if the first {@link Number} is greater than or equal to the second.
   */
  public boolean isGreaterEqual(N n1, N n2) {

    if ((n1 == null) || (n2 == null)) {
      return false;
    }
    return (compareTo(n1, n2) >= 0);
  }

  /**
   * @param n1 the first {@link Number} to compare.
   * @param n2 the second {@link Number} to compare.
   * @return {@code true} if the first {@link Number} is less than the second.
   */
  public boolean isLess(N n1, N n2) {

    if ((n1 == null) || (n2 == null)) {
      return false;
    }
    return (compareTo(n1, n2) < 0);
  }

  /**
   * @param n1 the first {@link Number} to compare.
   * @param n2 the second {@link Number} to compare.
   * @return {@code true} if the first {@link Number} is less than or equal to the second.
   */
  public boolean isLessEqual(N n1, N n2) {

    if ((n1 == null) || (n2 == null)) {
      return false;
    }
    return (compareTo(n1, n2) <= 0);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private int compareTo(N n1, N n2) {

    return ((Comparable) n1).compareTo(n2);
  }

  /**
   * @param number the {@link Number} to check.
   * @return {@code true} if the given {@link Number} is negative, {@code false} otherwise.
   */
  public boolean isNegative(N number) {

    return isLess(number, getZero());
  }

  /**
   * @param number the {@link Number} to check.
   * @return {@code true} if the given {@link Number} is positive (greater than zero), {@code false} otherwise.
   */
  public boolean isPositive(N number) {

    return isGreater(number, getZero());
  }

  /**
   * @param number the {@link Number} to check.
   * @return {@code true} if the given {@link Number} is positive (greater than zero), {@code false} otherwise.
   */
  public boolean isPositiveOrZero(N number) {

    return isGreaterEqual(number, getZero());
  }

  private static IllegalArgumentException illegalRadixException(int radix) {

    throw new IllegalArgumentException("Radix " + radix + " is not supported.");
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
    } else if ((number instanceof Long) || (number instanceof Integer) || (number instanceof Short)
        || (number instanceof Byte)) {
      return BigDecimal.valueOf(number.longValue());
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

  /**
   * @param exactness the {@link #getExactness() exactness}.
   * @return the {@link NumberType} with the given {@link #getExactness() exactness} or {@code null} if no such
   *         {@link NumberType} exists.
   */
  public static NumberType<?> ofExactness(int exactness) {

    if ((exactness >= 0) && (exactness < TYPES.length)) {
      return TYPES[exactness];
    }
    return null;
  }

  /**
   * @param value the {@link Number} to simplify.
   * @return the simplest {@link Number} with the exact same value. E.g. {@link BigDecimal#ONE} will be simplified to
   *         {@link Byte} with value {@code 1}.
   */
  public static Number simplify(Number value) {

    return simplify(value, BYTE);
  }

  /**
   * @param value the {@link Number} to simplify.
   * @param min the minimum {@link NumberType} to simplify to. In case the given {@link Number} is simpler than the
   *        given {@link NumberType} it will be {@link NumberType#valueOf(Number) converted} and is actually
   *        unsimplified. E.g. use {@link #INTEGER} to ensure int exactness and avoid getting a {@link Byte} or
   *        {@link Short} value as result.
   * @return the simplest {@link Number} with the exact same value but with at least the
   *         {@link NumberType#getExactness() exactness} of the given minimum {@link NumberType}.
   */
  public static Number simplify(Number value, NumberType<?> min) {

    if (value == null) {
      return null;
    }
    NumberType<?> type = of(value.getClass());
    if (type.exactness == min.exactness) {
      return value;
    } else if (type.exactness < min.exactness) {
      return min.convert(value, false);
    }
    for (int i = min.exactness; i < type.exactness; i++) {
      Number n = TYPES[i].convert(value, true);
      if (n != null) {
        return n;
      }
    }
    return value;
  }

}
