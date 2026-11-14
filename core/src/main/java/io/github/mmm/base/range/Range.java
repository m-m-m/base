/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.util.Comparator;

/// This class represents a range from [minimum][#getMin()] to [maximum][#getMax()]. Implementations shall validate at
/// construction so a given [Range] should always be valid. **ATTENTION:** The [minimum][#getMin()] and
/// [maximum][#getMax()] may be `null` for [unbounded ranges][#unbounded()]. It is still recommended to use fixed bounds
/// such as [Long#MAX_VALUE]. However, for types such as [java.math.BigDecimal] this is not possible.
///
/// @param <V> type of the contained values.
/// @since 1.0.0
public interface Range<V extends Comparable<?>> {

  /// Char indicating start with inclusive minimum.
  char BOUND_START_INCLUSIVE = '[';

  /// Char indicating start with exclusive minimum.
  char BOUND_START_EXCLUSIVE = '(';

  /// Char indicating end with inclusive maximum.
  char BOUND_END_INCLUSIVE = ']';

  /// Char indicating end with exclusive maximum.
  char BOUND_END_EXCLUSIVE = ')';

  /// Char to separate minimum and maximum. Mathematical convention would be to use a comma (','), but this causes
  /// problems when parsing [string representations][Object#toString()] as a comma may also occur in the minimum or
  /// maximum value.
  char BOUND_SEPARATOR = '\uFF0C';

  /// The unbound minimum.
  String MIN_UNBOUND = "\u2212\u221E";

  /// The unbound maximum.
  String MAX_UNBOUND = "+\u221E";

  /// Property name of [#getMin()].
  String PROPERTY_MIN = "min";

  /// Property name of [#getMax()].
  String PROPERTY_MAX = "max";

  /// @return the lower bound of this range or `null` if no lower bound is defined. Has to be less than
  ///         [max][#getMax()] if both boundaries are not `null`.
  V getMin();

  /// @return the upper bound of this range or `null` if no upper bound is defined. Has to be greater than
  ///         [min][#getMin()] if both boundaries are not `null`.
  V getMax();

  /// @return the [Comparator] used to [compare][Comparator#compare(Object, Object)] values of this [Range]. The default
  ///         implementation assumes that the value type implements [Comparable]. If you want to use other value types
  ///         you need to override this method.
  @SuppressWarnings({ "unchecked", "rawtypes" })
  default Comparator<? super V> getComparator() {

    return (Comparator) Comparator.naturalOrder();
  }

  /// This method determines if the given `value` is within this [Range] from [minimum][#getMin()] to
  /// [maximum][#getMax()].
  ///
  /// @param value is the vale to check.
  /// @return `true` if contained ([minimum][#getMin()] `<=` `value` `>=` [maximum][#getMax()]), `false` otherwise. If
  ///         the given value is `null`, `false` will be returned.
  default boolean contains(V value) {

    if (value == null) {
      return false;
    }
    return value == clip(value);
  }

  /// This method clips the given `value` so the result is [contained][#contains(Comparable)] in this [Range] unless the
  /// given `value` is `null`.
  ///
  /// @param value is the vale to clip. May be `null`.
  /// @return the given `value` clipped to this range. If the given `value` is less than the [minimum][#getMin()], that
  ///         [minimum][#getMin()] will be returned. If the given `value` is greater than the [maximum][#getMax()], that
  ///         [maximum][#getMax()] will be returned. Otherwise the given `value` is returned.
  default V clip(V value) {

    if (value == null) {
      return null;
    }
    Comparator<? super V> comparator = getComparator();
    int delta;
    V min = getMin();
    if (min != null) {
      delta = comparator.compare(value, min);
      if (delta < 0) {
        // value < min
        return min;
      }
    }
    V max = getMax();
    if (max != null) {
      delta = comparator.compare(value, max);
      if (delta > 0) {
        // value > max
        return max;
      }
    }
    return value;
  }

  /// @param minimum the new [minimum][#getMin()].
  /// @param maximum the new [maximum][#getMax()].
  /// @return a new [Range] with the given boundaries.
  Range<V> with(V minimum, V maximum);

  /// @param minimum the new [minimum][#getMin()].
  /// @return a new [Range] where the [minimum][#getMin()] is set to the given `minimum` value.
  default Range<V> withMin(V minimum) {

    return with(minimum, getMax());
  }

  /// @param maximum the new [maximum][#getMax()].
  /// @return a new [Range] where the [maximum][#getMax()] is set to the given `maximum` value.
  default Range<V> withMax(V maximum) {

    return with(getMin(), maximum);
  }

  /// @param range the [Range] to build the intersection with.
  /// @return the intersection of this [Range] with the given [Range] that is a [Range] with the highest
  ///         [minimum][#getMin()] and the lowest [maximum][#getMax()] of the two ranges to intersect. In case the
  ///         [Range]s have no intersection [Range#invalid()] is returned.
  Range<V> intersection(Range<V> range);

  /// @return true if this [Range] is [unbounded][#unbounded()].
  default boolean isUnbounded() {

    return ((getMin() == null) && (getMax() == null));
  }

  /// @param <T> type of the [contained value][#contains(Comparable)].
  /// @param min the [minimum][#getMin()].
  /// @param max the [maximum][#getMax()].
  /// @return the specified [Range].
  static <T extends Comparable<?>> Range<T> of(T min, T max) {

    return RangeType.of(min, max);
  }

  /// @param <T> type of the [contained value][#contains(Comparable)].
  /// @return the unbounded [Range] instance [containing][#contains(Comparable)] all values (with [#getMin()] and
  ///         [#getMax()] being `null`).
  static <T extends Comparable<?>> Range<T> unbounded() {

    return RangeType.UNBOUNDED;
  }

  /// @param <T> type of the [contained value][#contains(Comparable)].
  /// @return the invalid [Range] instance [containing][#contains(Comparable)] no values at all (with [#getMin()] and
  ///         [#getMax()] being `null`).
  static <T extends Comparable<?>> Range<T> invalid() {

    return RangeType.INVALID;
  }

}
