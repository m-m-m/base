/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link Range}.
 */
public class RangeTest extends Assertions {

  /** Test of {@link Range#unbounded()}. */
  @Test
  public void testUnbounded() {

    Range<String> unbounded = Range.unbounded();

    assertThat(unbounded).isNotNull();
    assertThat(unbounded.getMin()).isNull();
    assertThat(unbounded.getMax()).isNull();
    assertThat(unbounded.toString()).isEqualTo("(−∞，+∞)");
    assertThat(unbounded.contains("value")).isTrue();
    assertThat(unbounded.contains(null)).isFalse();
  }

}
