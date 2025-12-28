/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.score;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link Score}.
 */
class ScoreTest extends Assertions {

  private static final Score[] SCORES = new Score[] { Score.MIN, Score.VERY_LOW, Score.LOWER, Score.LOW,
  Score.LOW_MEDIUM, Score.MEDIUM, Score.HIGH_MEDIUM, Score.HIGH, Score.HIGHER, Score.VERY_HIGH, Score.MAX };

  /** Test {@link Score#of(double)}. */
  @Test
  void testOf() {

    assertThat(Score.of(0.0)).isSameAs(Score.MIN);
    assertThat(Score.of(0.1)).isSameAs(Score.VERY_LOW);
    assertThat(Score.of(0.2)).isSameAs(Score.LOWER);
    assertThat(Score.of(0.3)).isSameAs(Score.LOW);
    assertThat(Score.of(0.4)).isSameAs(Score.LOW_MEDIUM);
    assertThat(Score.of(0.5)).isSameAs(Score.MEDIUM);
    assertThat(Score.of(0.6)).isSameAs(Score.HIGH_MEDIUM);
    assertThat(Score.of(0.7)).isSameAs(Score.HIGH);
    assertThat(Score.of(0.8)).isSameAs(Score.HIGHER);
    assertThat(Score.of(0.9)).isSameAs(Score.VERY_HIGH);
    assertThat(Score.of(1.0)).isSameAs(Score.MAX);
    assertThat(Score.of(0.25)).isNotSameAs(Score.of(0.25)).isEqualTo(Score.of(0.25));
  }

  /** Test {@link Score#getValue()}. */
  @Test
  void testGetValue() {

    assertThat(Score.MIN.getValue()).isEqualTo(0.0);
    assertThat(Score.VERY_LOW.getValue()).isEqualTo(0.1);
    assertThat(Score.LOWER.getValue()).isEqualTo(0.2);
    assertThat(Score.LOW.getValue()).isEqualTo(0.3);
    assertThat(Score.LOW_MEDIUM.getValue()).isEqualTo(0.4);
    assertThat(Score.MEDIUM.getValue()).isEqualTo(0.5);
    assertThat(Score.HIGH_MEDIUM.getValue()).isEqualTo(0.6);
    assertThat(Score.HIGH.getValue()).isEqualTo(0.7);
    assertThat(Score.HIGHER.getValue()).isEqualTo(0.8);
    assertThat(Score.VERY_HIGH.getValue()).isEqualTo(0.9);
    assertThat(Score.MAX.getValue()).isEqualTo(1.0);
    assertThat(Score.of(0.25).getValue()).isEqualTo(0.25);
  }

  /** Test {@link Score#multiply(Score)}. */
  @Test
  void testMultiply() {

    for (Score score : SCORES) {
      assertThat(Score.MIN.multiply(score)).isSameAs(Score.MIN);
      assertThat(Score.MAX.multiply(score)).isSameAs(score);
    }
    assertThat(Score.MEDIUM.multiply(Score.MEDIUM)).isEqualTo(Score.of(0.25));
  }

}
