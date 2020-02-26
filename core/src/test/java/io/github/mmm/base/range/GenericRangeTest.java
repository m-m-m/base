/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.time.Year;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link GenericRange}.
 */
public class GenericRangeTest extends Assertions {

  /** Test of {@link GenericRange}. */
  @Test
  public void test() {

    GenericRange<Year> yearRange = new GenericRange<>(Year.of(2000), Year.of(2020));
    assertThat(yearRange).hasToString("[2000，2020]");
    assertThat(yearRange.getMin()).isEqualTo(Year.of(2000));
    assertThat(yearRange.getMax()).isEqualTo(Year.of(2020));
    assertThat(yearRange.isContained(Year.of(1999))).isFalse();
    assertThat(yearRange.isContained(Year.of(2000))).isTrue();
    assertThat(yearRange.isContained(Year.of(2001))).isTrue();
    assertThat(yearRange.isContained(Year.of(2019))).isTrue();
    assertThat(yearRange.isContained(Year.of(2020))).isTrue();
    assertThat(yearRange.isContained(Year.of(2021))).isFalse();
  }

}
