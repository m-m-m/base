/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.sort;

/// This enum contains the possible values for the sort order.
///
/// @since 1.0.0
public enum SortOrder {

  /// Indicating that values are in increasing order (e.g. "1, 2, 3").
  ASC,

  /// Indicating that values are in decreasing order (e.g. "3, 2, 1").
  DESC;

  /// Adjusts the [Math#signum(double)] of a [compare to][Comparable#compareTo(Object)] result with this
  /// [SortOrder] to the semantic of [java.util.Collections#sort(java.util.List, java.util.Comparator)]. In
  /// other words *
  ///
  /// @param compareTo is the result of a regular [compare to][Comparable#compareTo(Object)] operation.
  /// @return the given value (`compareTo`) for [#ASC] and the negation ( `-compareTo`) otherwise (for
  ///         [#DESC]).
  public int adjustSignum(int compareTo) {

    if (this == DESC) {
      return -compareTo;
    }
    return compareTo;
  }

}
