/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.score;

/**
 * Data-type for a score as a double value in the range from {@code 0} to {@code 1}.
 *
 * @since 1.0.0
 */
public final class Score {

  private final double value;

  /** The minimum score (0.0). */
  public static final Score MIN = new Score(0);

  /** The very low score (0.1). */
  public static final Score VERY_LOW = new Score(0.1);

  /** The lower score (0.2). */
  public static final Score LOWER = new Score(0.2);

  /** The low score (0.3). */
  public static final Score LOW = new Score(0.3);

  /** The low medium score (0.4). */
  public static final Score LOW_MEDIUM = new Score(0.4);

  /** The medium score (0.5). */
  public static final Score MEDIUM = new Score(0.5);

  /** The high medium score (0.6). */
  public static final Score HIGH_MEDIUM = new Score(0.6);

  /** The high score (0.7). */
  public static final Score HIGH = new Score(0.7);

  /** The higher score (0.8). */
  public static final Score HIGHER = new Score(0.8);

  /** The very high score (0.9). */
  public static final Score VERY_HIGH = new Score(0.9);

  /** The maximum score (1.0). */
  public static final Score MAX = new Score(1);

  private static final Score[] CONST = new Score[] { MIN, VERY_LOW, LOWER, LOW, LOW_MEDIUM, MEDIUM, HIGH_MEDIUM, HIGH,
  HIGHER, VERY_HIGH, MAX };

  private Score(double value) {

    super();
    this.value = value;
  }

  /**
   * @return the score as double value in the range from {@code 0} to {@code 1}.
   */
  public double getValue() {

    return this.value;
  }

  /**
   * @param score the factor to multiply by.
   * @return the product of this {@link Score} multiplied with the given {@link Score}.
   */
  public Score multiply(Score score) {

    if ((score == MIN) || (this == MIN)) {
      return MIN;
    } else if (score == MAX) {
      return this;
    } else if (this == MAX) {
      return score;
    }
    double newValue = this.value * score.value;
    return of(newValue);
  }

  @Override
  public int hashCode() {

    return (int) this.value * 100;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    } else if (obj instanceof Score score) {
      return this.value == score.value;
    } else {
      return false;
    }
  }

  @Override
  public String toString() {

    return Double.toString(this.value);
  }

  /**
   * @param value the {@link #getValue() score value}.
   * @return the {@link Score} for the given {@code value}.
   */
  public static final Score of(double value) {

    if ((value < 0) || (value > 1)) {
      throw new IllegalArgumentException("Invalid score: " + value);
    }
    for (Score constant : CONST) {
      if (constant.value == value) {
        return constant;
      }
    }
    return new Score(value);
  }

}
