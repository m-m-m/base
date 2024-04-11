/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.number;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link NumberType}.
 */
@SuppressWarnings("all")
public class NumberTypeTest extends Assertions {

  private static final BigInteger BIG_INTEGER = new BigInteger("123456789012345678901234567890");

  private static final BigDecimal BIG_DECIMAL = new BigDecimal("123456789.12345678901234567890");

  protected <N extends Number> void check(NumberType<N> type, N value) {

    assertThat(type.getType()).isEqualTo(value.getClass());
    assertThat(type).isSameAs(NumberType.ofExactness(type.getExactness()));
    assertThat(type.parse(value.toString())).isEqualTo(value);
    if (type.getExactness() < 7) {
      Double d = Double.valueOf(value.doubleValue());
      assertThat(type.valueOf(d)).isEqualTo(value);
    }
    assertThat(type.valueOf(value)).isSameAs(value);
    try {
      type.parse("illegal number");
      failBecauseExceptionWasNotThrown(NumberFormatException.class);
    } catch (NumberFormatException e) {
    }
  }

  protected <N extends Number> void checkNonDecimal(NumberType<N> type, N value) {

    assertThat(type.isDecimal()).isFalse();
    check(type, value);
    Double d = Double.valueOf(42.00001);
    N converted = type.valueOf(d);
    assertThat(converted.longValue()).isEqualTo(42);
  }

  protected <N extends Number> void checkDecimal(NumberType<N> type, N value) {

    assertThat(type.isDecimal()).isTrue();
    check(type, value);
    Float f = Float.valueOf(42.00001F);
    N converted = type.valueOf(f);
    assertThat(converted.floatValue()).isEqualTo(f);
  }

  @Test
  public void testByte() {

    // given
    byte b = 123;
    // when
    NumberType<Byte> type = NumberType.BYTE;
    // then
    checkNonDecimal(type, Byte.valueOf(b));
    assertThat(type.getMin()).isEqualTo(Byte.MIN_VALUE);
    assertThat(type.getMax()).isEqualTo(Byte.MAX_VALUE);
  }

  @Test
  public void testShort() {

    // given
    short s = 12345;
    // when
    NumberType<Short> type = NumberType.SHORT;
    // then
    checkNonDecimal(type, Short.valueOf(s));
    assertThat(type.getMin()).isEqualTo(Short.MIN_VALUE);
    assertThat(type.getMax()).isEqualTo(Short.MAX_VALUE);
  }

  @Test
  public void testInteger() {

    // given
    int i = 1234567890;
    // when
    NumberType<Integer> type = NumberType.INTEGER;
    // then
    checkNonDecimal(type, Integer.valueOf(i));
    assertThat(type.getMin()).isEqualTo(Integer.MIN_VALUE);
    assertThat(type.getMax()).isEqualTo(Integer.MAX_VALUE);
  }

  @Test
  public void testLong() {

    // given
    long l = 1234567890123456L;
    // when
    NumberType<Long> type = NumberType.LONG;
    // then
    checkNonDecimal(type, Long.valueOf(l));
    assertThat(type.getMin()).isEqualTo(Long.MIN_VALUE);
    assertThat(type.getMax()).isEqualTo(Long.MAX_VALUE);
  }

  @Test
  public void testBigInteger() {

    // given
    BigInteger bi = BIG_INTEGER;
    // when
    NumberType<BigInteger> type = NumberType.BIG_INTEGER;
    // then
    checkNonDecimal(type, bi);
    assertThat(type.getMin()).isNull();
    assertThat(type.getMax()).isNull();
    assertThat(type.valueOf(BIG_DECIMAL)).isEqualTo(BIG_DECIMAL.toBigInteger());
  }

  @Test
  public void testFloat() {

    // given
    float f = 123456789.123456789F;
    // when
    NumberType<Float> type = NumberType.FLOAT;
    // then
    checkDecimal(type, Float.valueOf(f));
    assertThat(type.getMin()).isEqualTo(-Float.MAX_VALUE);
    assertThat(type.getMax()).isEqualTo(Float.MAX_VALUE);
  }

  @Test
  public void testDouble() {

    // given
    double d = 123456789.123456789;
    // when
    NumberType<Double> type = NumberType.DOUBLE;
    // then
    checkDecimal(type, Double.valueOf(d));
    assertThat(type.getMin()).isEqualTo(-Double.MAX_VALUE);
    assertThat(type.getMax()).isEqualTo(Double.MAX_VALUE);
  }

  @Test
  public void testBigDecimal() {

    // given
    BigDecimal bd = BIG_DECIMAL;
    // when
    NumberType<BigDecimal> type = NumberType.BIG_DECIMAL;
    // then
    checkDecimal(type, bd);
    assertThat(type.getMin()).isNull();
    assertThat(type.getMax()).isNull();
    assertThat(type.valueOf(BIG_INTEGER)).isEqualTo(new BigDecimal(BIG_INTEGER));
  }

  @Test
  public void testSimplify() {

    Byte one = NumberType.BYTE.getOne();
    for (int exactness = NumberType.BYTE.getExactness(); exactness <= NumberType.BIG_DECIMAL
        .getExactness(); exactness++) {
      NumberType t = NumberType.ofExactness(exactness);
      assertThat(NumberType.simplify(t.getOne())).isEqualTo(one);
      Number max = t.getMax();
      BigDecimal bd = NumberType.BIG_DECIMAL.valueOf(max);
      assertThat(NumberType.simplify(bd)).isEqualTo(max);
      Number min = t.getMin();
      bd = NumberType.BIG_DECIMAL.valueOf(min);
      assertThat(NumberType.simplify(bd)).isEqualTo(min);
    }
    assertThat(NumberType.simplify(BigDecimal.valueOf(4.2))).isEqualTo(Double.valueOf(4.2));
    assertThat(NumberType.simplify(BigDecimal.valueOf(4.2f))).isEqualTo(Float.valueOf(4.2f));
    assertThat(NumberType.simplify(new BigInteger("12345678901"))).isEqualTo(Long.valueOf(12345678901L));
    assertThat(NumberType.simplify(BIG_INTEGER)).isSameAs(BIG_INTEGER);
    assertThat(NumberType.simplify(BIG_DECIMAL)).isSameAs(BIG_DECIMAL);
    BigDecimal bd = new BigDecimal("0.12345678901234567");
    assertThat(NumberType.simplify(bd)).isSameAs(bd);
    checkSimplify(new BigDecimal("0.1234567890123456789"));
  }

  private void checkSimplify(Number n) {

    Number simplified = NumberType.simplify(n);
    if (simplified != n) {
      assertThat(simplified).hasToString(n.toString());
    }
  }

  @Test
  public void testSimplifyMin() {

    NumberType<Integer> integer = NumberType.INTEGER;
    Integer one = integer.getOne();
    for (int exactness = NumberType.BYTE.getExactness(); exactness <= NumberType.BIG_DECIMAL
        .getExactness(); exactness++) {
      NumberType t = NumberType.ofExactness(exactness);
      assertThat(NumberType.simplify(t.getOne(), integer)).isEqualTo(one);
    }
  }

  @Test
  public void testWrap() {

    // already within bounds
    checkWrapInt(5, 0, 10, 5);
    checkWrapInt(10, 0, 10, 10);
    checkWrapInt(-10, -10, 10, -10);
    // wrap above max
    checkWrapInt(11, 0, 10, 0);
    checkWrapInt(12, 0, 10, 1);
    checkWrapInt(15, 0, 10, 4);
    checkWrapInt(19, 0, 10, 8);
    checkWrapInt(20, 0, 10, 9);
    checkWrapInt(21, 0, 10, 10);
    checkWrapInt(22, 0, 10, 10);
    // wrap below min
    checkWrapInt(-11, -10, 10, 10);
    checkWrapInt(-12, -10, 10, 9);
    checkWrapInt(-19, -10, 10, 2);
    checkWrapInt(-20, -10, 10, 1);
    checkWrapInt(-21, -10, 10, 0);
    checkWrapInt(-22, -10, 10, -1);
    checkWrapInt(-30, -10, 10, -9);
    checkWrapInt(-31, -10, 10, -10);
    checkWrapInt(-32, -10, 10, -10);

  }

  private void checkWrapInt(int value, int min, int max, int expected) {

    Integer actual = NumberType.INTEGER.wrap(value, min, max);
    assertThat(actual).isEqualTo(expected);
  }
}
