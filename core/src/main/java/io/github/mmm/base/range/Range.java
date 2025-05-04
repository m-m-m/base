/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.util.Comparator;

/**
 * This class represents a range from {@link #getMin() minimum} to {@link #getMax() maximum}. Implementations shall
 * validate at construction so a given {@link Range} should always be valid. <br>
 * <b>ATTENTION:</b><br>
 * The {@link #getMin() minimum} and {@link #getMax() maximum} may be {@code null} for {@link #unbounded() unbounded
 * ranges}. It is still recommended to use fixed bounds such as {@link Long#MAX_VALUE}. However, for types such as
 * {@link java.math.BigDecimal} this is not possible.
 *
 * @param <V> type of the contained values.
 * @since 1.0.0
 */
public interface Range<V extends Comparable<?>> {

  /** Char indicating start with inclusive minimum. */
  char BOUND_START_INCLUSIVE = '[';

  /** Char indicating start with exclusive minimum. */
  char BOUND_START_EXCLUSIVE = '(';

  /** Char indicating end with inclusive maximum. */
  char BOUND_END_INCLUSIVE = ']';

  /** Char indicating end with exclusive maximum. */
  char BOUND_END_EXCLUSIVE = ')';

  /**
   * Char to separate minimum and maximum. Mathematical convention would be to use a comma (','), but this causes
   * problems when parsing {@link Object#toString() string representations} as a comma may also occur in the minimum or
   * maximum value.
   */
  char BOUND_SEPARATOR = '\uFF0C';

  /** The unbound minimum. */
  String MIN_UNBOUND = "\u2212\u221E";

  /** The unbound maximum. */
  String MAX_UNBOUND = "+\u221E";

  /** Property name of {@link #getMin()}. */
  String PROPERTY_MIN = "min";

  /** Property name of {@link #getMax()}. */
  String PROPERTY_MAX = "max";

  /**
   * @return the lower bound of this range or {@code null} if no lower bound is defined. Has to be less than
   *         {@link #getMax() max} if both boundaries are not {@code null}.
   */
  V getMin();

  /**
   * @return the upper bound of this range or {@code null} if no upper bound is defined. Has to be greater than
   *         {@link #getMin() min} if both boundaries are not {@code null}.
   */
  V getMax();

  /**
   * @return the {@link Comparator} used to {@link Comparator#compare(Object, Object) compare} values of this
   *         {@link Range}. The default implementation assumes that the value type implements {@link Comparable}. If you
   *         want to use other value types you need to override this method.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  default Comparator<? super V> getComparator() {

    return (Comparator) Comparator.naturalOrder();
  }

  /**
   * This method determines if the given {@code value} is within this {@link Range} from {@link #getMin() minimum} to
   * {@link #getMax() maximum}.
   *
   * @param value is the vale to check.
   * @return {@code true} if contained ({@link #getMin() minimum} {@literal <=} {@code value} {@literal >=}
   *         {@link #getMax() maximum}), {@code false} otherwise. If the given value is {@code null}, {@code false} will
   *         be returned.
   */
  default boolean contains(V value) {

    if (value == null) {
      return false;
    }
    return value == clip(value);
  }

  /**
   * This method clips the given {@code value} so the result is {@link #contains(Comparable) contained} in this
   * {@link Range} unless the given {@code value} is {@code null}.
   *
   * @param value is the vale to clip. May be {@code null}.
   * @return the given {@code value} clipped to this range. If the given {@code value} is less than the {@link #getMin()
   *         minimum}, that {@link #getMin() minimum} will be returned. If the given {@code value} is greater than the
   *         {@link #getMax() maximum}, that {@link #getMax() maximum} will be returned. Otherwise the given
   *         {@code value} is returned.
   */
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

  /**
   * @param minimum the new {@link #getMin() minimum}.
   * @param maximum the new {@link #getMax() maximum}.
   * @return a new {@link Range} with the given boundaries.
   */
  Range<V> with(V minimum, V maximum);

  /**
   * @param minimum the new {@link #getMin() minimum}.
   * @return a new {@link Range} where the {@link #getMin() minimum} is set to the given {@code minimum} value.
   */
  default Range<V> withMin(V minimum) {

    return with(minimum, getMax());
  }

  /**
   * @param maximum the new {@link #getMax() maximum}.
   * @return a new {@link Range} where the {@link #getMax() maximum} is set to the given {@code maximum} value.
   */
  default Range<V> withMax(V maximum) {

    return with(getMin(), maximum);
  }

  /**
   * @param range the {@link Range} to build the intersection with.
   * @return the intersection of this {@link Range} with the given {@link Range} that is a {@link Range} with the
   *         highest {@link #getMin() minimum} and the lowest {@link #getMax() maximum} of the two ranges to intersect.
   *         In case the {@link Range}s the intersection of
   */
  Range<V> intersection(Range<V> range);

  /**
   * @return true if this {@link Range} is {@link #unbounded() unbounded}.
   */
  default boolean isUnbounded() {

    return ((getMin() == null) && (getMax() == null));
  }

  /**
   * @param <T> type of the {@link #contains(Comparable) contained value}.
   * @param min the {@link #getMin() minimum}.
   * @param max the {@link #getMax() maximum}.
   * @return the specified {@link Range}.
   */
  static <T extends Comparable<?>> Range<T> of(T min, T max) {

    return RangeType.of(min, max);
  }

  /**
   * @param <T> type of the {@link #contains(Comparable) contained value}.
   * @return the unbounded {@link Range} instance {@link #contains(Comparable) containing} all values (with
   *         {@link #getMin()} and {@link #getMax()} being {@code null}).
   */
  static <T extends Comparable<?>> Range<T> unbounded() {

    return RangeType.UNBOUNDED;
  }

  /**
   * @param <T> type of the {@link #contains(Comparable) contained value}.
   * @return the invalid {@link Range} instance {@link #contains(Comparable) containing} no values at all (with
   *         {@link #getMin()} and {@link #getMax()} being {@code null}).
   */
  static <T extends Comparable<?>> Range<T> invalid() {

    return RangeType.INVALID;
  }

}
