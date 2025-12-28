/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link NumberRangeType}.
 */
class NumberRangeTypeTest extends Assertions {

  /** Test of {@link NumberRangeType}. */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Test
  void test() {

    // arrange
    BigDecimal min = new BigDecimal("-0.000000001");
    Long max = Long.valueOf(1);

    // act
    NumberRangeType numberRange = new NumberRangeType(min, max);

    // assert
    assertThat(numberRange).hasToString("[-0.000000001，1]");
    assertThat(numberRange.getMin()).isEqualTo(min);
    assertThat(numberRange.getMax()).isEqualTo(max);
    assertThat(numberRange.contains(Double.valueOf(-0.000000001))).isTrue();
    assertThat(numberRange.contains(min)).isTrue();
    assertThat(numberRange.contains(new BigDecimal("0.99999999999999999999"))).isTrue();
    assertThat(numberRange.contains(max)).isTrue();
    assertThat(numberRange.contains(new BigDecimal("1.00000000000000000001"))).isFalse();
    assertThat(numberRange.clip(new BigDecimal("1.00000000000000000001"))).isEqualTo(max);
  }

}
