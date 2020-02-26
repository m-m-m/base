/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.time.Year;
import java.util.function.Function;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link GenericRange}.
 */
public class GenericRangeTest extends Assertions {

  /** Test of {@link GenericRange}. */
  @Test
  public void test() {

    // given
    Year min = Year.of(2000);
    Year max = Year.of(2020);

    // when
    GenericRange<Year> yearRange = new GenericRange<>(min, max);

    // then
    assertThat(yearRange).hasToString("[2000，2020]");
    assertThat(yearRange.getMin()).isEqualTo(min);
    assertThat(yearRange.getMax()).isEqualTo(max);
    assertThat(yearRange.contains(Year.of(1999))).isFalse();
    assertThat(yearRange.contains(min)).isTrue();
    assertThat(yearRange.contains(Year.of(2001))).isTrue();
    assertThat(yearRange.contains(Year.of(2019))).isTrue();
    assertThat(yearRange.contains(max)).isTrue();
    assertThat(yearRange.contains(Year.of(2021))).isFalse();
  }

  /** Test of {@link GenericRange#parse(String, java.util.function.Function)}. */
  @Test
  public void testParse() {

    // given
    Function<String, Year> yearParser = (s) -> Year.of(Integer.parseInt(s));

    // when + then
    assertThat(GenericRange.parse(null, yearParser)).isEqualTo(Range.unbounded());
    assertThat(GenericRange.parse("", yearParser)).isEqualTo(Range.unbounded());
    assertThat(GenericRange.parse(Range.unbounded().toString(), yearParser)).isEqualTo(Range.unbounded());
    assertThat(GenericRange.parse("[2000，2020]", yearParser))
        .isEqualTo(new GenericRange<>(Year.of(2000), Year.of(2020)));
  }

}
