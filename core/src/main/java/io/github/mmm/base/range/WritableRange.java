/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

/// A mutable [Range] allowing to [set minimum][#setMin(Comparable)] and [maximum][#setMax(Comparable)].
///
/// @param <V> type of the contained values.
/// @since 1.0.0
public interface WritableRange<V extends Comparable<?>> extends Range<V> {

  /// @param min the new value of [#getMin()].
  void setMin(V min);

  /// @param max the new value of [#getMax()].
  void setMax(V max);

}
