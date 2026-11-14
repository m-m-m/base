/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.version;

/// Enum representing the type of interval regarding its boundaries.
public enum BoundaryType {

  /// Closed interval - includes the specified values at the boundaries.
  CLOSED,

  /// Open interval - excludes the specified values at the boundaries.
  OPEN,

  /// Left open interval - excludes the lower bound but includes the upper bound.
  LEFT_OPEN,

  /// Right open interval - includes the lower bound but excludes the upper bound.
  RIGHT_OPEN;

  static final String START_EXCLUDING_PREFIX = "(";

  static final String START_INCLUDING_PREFIX = "[";

  static final String END_EXCLUDING_SUFFIX = ")";

  static final String END_INCLUDING_SUFFIX = "]";

  /// @return `true` if left exclusive, `false` otherwise (left inclusive).
  public boolean isLeftExclusive() {

    return (this == LEFT_OPEN) || (this == OPEN);
  }

  /// @return `true` if right exclusive, `false` otherwise (right inclusive).
  public boolean isRightExclusive() {

    return (this == RIGHT_OPEN) || (this == OPEN);
  }

  /// @return the prefix (left parenthesis or bracket for [#isLeftExclusive()]).
  public String getPrefix() {

    return isLeftExclusive() ? START_EXCLUDING_PREFIX : START_INCLUDING_PREFIX;
  }

  /// @return the suffix (right parenthesis or bracket for [#isRightExclusive()]).
  public String getSuffix() {

    return isRightExclusive() ? END_EXCLUDING_SUFFIX : END_INCLUDING_SUFFIX;
  }

  /// @param leftExclusive the [left exclusive flag][#isLeftExclusive()].
  /// @param rightExclusive the [right exclusive flag][#isRightExclusive()].
  /// @return the [BoundaryType] with the specified values.
  public static BoundaryType of(boolean leftExclusive, boolean rightExclusive) {

    if (leftExclusive) {
      if (rightExclusive) {
        return OPEN;
      } else {
        return LEFT_OPEN;
      }
    } else {
      if (rightExclusive) {
        return RIGHT_OPEN;
      } else {
        return CLOSED;
      }
    }
  }
}
