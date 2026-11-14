/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.version;

/// Result of [VersionObject#compareVersion(Object)].
public enum VersionComparisonResult {

  /// The first version is smaller than the second one.
  LESS,

  /// [Less][#LESS] but [unsafe][#isUnsafe()].
  LESS_UNSAFE,

  /// Both versions are equal.
  EQUAL,

  /// [#EQUAL] but [unsafe][#isUnsafe()].
  EQUAL_UNSAFE,

  /// The second version is larger than the second one.
  GREATER,

  /// [Greater][#GREATER] but [unsafe][#isUnsafe()].
  GREATER_UNSAFE;

  /// @return `true` if the versions are not strictly comparable (e.g. "apple" and "banana", also for "1.0" and
  ///         "1-0"), `false` otherwise.
  /// @see #LESS_UNSAFE
  /// @see #GREATER_UNSAFE
  public boolean isUnsafe() {

    return (this == LESS_UNSAFE) || (this == GREATER_UNSAFE);
  }

  /// @return `true` if the first version was smaller than the second one, `false` otherwise.
  public boolean isLess() {

    return (this == LESS) || (this == LESS_UNSAFE);
  }

  /// @return `true` if both versions were equal, `false` otherwise.
  public boolean isEqual() {

    return (this == EQUAL) || (this == EQUAL_UNSAFE);
  }

  /// @return `true` if the first version was larger than the second one, `false` otherwise.
  public boolean isGreater() {

    return (this == GREATER) || (this == GREATER_UNSAFE);
  }

  /// @return an integer value equivalent with the [VersionComparisonResult] and compliant with
  ///         [VersionObject#compareTo(Object)].
  public int asValue() {

    return switch (this) {
      case LESS, LESS_UNSAFE -> -1;
      case EQUAL, EQUAL_UNSAFE -> 0;
      case GREATER, GREATER_UNSAFE -> 1;
      default -> throw new IllegalStateException(toString());
    };
  }

  /// @param unsafe the new value of [#isUnsafe()].
  /// @return an equivalent [VersionComparisonResult] with the given [unsafe][#isUnsafe()] value.
  public VersionComparisonResult withUnsafe(boolean unsafe) {

    if (unsafe) {
      return withUnsafe();
    } else {
      return withSafe();
    }
  }

  /// @return an equivalent [VersionComparisonResult] with [unsafe][#isUnsafe()] being `true`.
  public VersionComparisonResult withUnsafe() {

    if (isGreater()) {
      return GREATER_UNSAFE;
    } else if (isLess()) {
      return LESS_UNSAFE;
    } else {
      return EQUAL_UNSAFE;
    }
  }

  /// @return an equivalent [VersionComparisonResult] with [unsafe][#isUnsafe()] being `false`.
  public VersionComparisonResult withSafe() {

    if (isGreater()) {
      return GREATER;
    } else if (isLess()) {
      return LESS;
    } else {
      return EQUAL;
    }
  }

}
