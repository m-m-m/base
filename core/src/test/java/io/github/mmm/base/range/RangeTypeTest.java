/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.time.Year;
import java.util.function.Function;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link RangeType}.
 */
public class RangeTypeTest extends Assertions {

  /** Test of {@link RangeType}. */
  @Test
  public void test() {

    // given
    Year min = Year.of(2000);
    Year max = Year.of(2020);

    // when
    RangeType<Year> yearRange = new RangeType<>(min, max);

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

  /** Test of {@link RangeType#parse(String, java.util.function.Function)}. */
  @Test
  public void testParseYear() {

    // given
    Function<String, Year> yearParser = (s) -> Year.of(Integer.parseInt(s));

    // when + then
    assertThat(RangeType.parse(null, yearParser)).isEqualTo(Range.unbounded());
    assertThat(RangeType.parse("", yearParser)).isEqualTo(Range.unbounded());
    assertThat(RangeType.parse(Range.unbounded().toString(), yearParser)).isEqualTo(Range.unbounded());
    assertThat(RangeType.parse("[2000，2020]", yearParser)).isEqualTo(new RangeType<>(Year.of(2000), Year.of(2020)));
  }

  /** Test of {@link RangeType#parse(String, java.util.function.Function)}. */
  @Test
  public void testParseSpecial() {

    // given
    Range<Long> unbounded = Range.unbounded();
    Range<Long> invalid = Range.invalid();
    Function<String, Long> longParser = (s) -> Long.valueOf(s);

    // when + then
    assertThat(RangeType.parse(unbounded.toString(), longParser)).isSameAs(Range.unbounded());
    assertThat(RangeType.parse(invalid.toString(), longParser)).isSameAs(Range.invalid());
  }

  /**
   * Test of {@link Range#intersection(Range)}.
   */
  @Test
  public void testIntersection() {

    Range<Integer> range1 = Range.unbounded();
    Range<Integer> range2 = RangeType.of(null, 42);
    Range<Integer> intersection = range1.intersection(range2);
    assertThat(intersection).isSameAs(range2);
    Range<Integer> range3 = RangeType.of(null, 43);
    intersection = intersection.intersection(range3);
    assertThat(intersection).isSameAs(range2);
    Range<Integer> range4 = RangeType.of(null, 41);
    intersection = intersection.intersection(range4);
    assertThat(intersection).isSameAs(range4);
    Range<Integer> range5 = RangeType.of(-2, null);
    intersection = intersection.intersection(range5);
    assertThat(intersection.getMin()).isEqualTo(-2);
    assertThat(intersection.getMax()).isEqualTo(41);
    Range<Integer> range6 = RangeType.of(-4, 99);
    assertThat(intersection.intersection(range6)).isSameAs(intersection);
    Range<Integer> range7 = RangeType.of(0, 40);
    intersection = intersection.intersection(range7);
    assertThat(intersection).isSameAs(range7);
    Range<Integer> range8 = RangeType.of(100, 200);
    assertThat(intersection.intersection(range8)).isSameAs(Range.invalid());
  }
}
