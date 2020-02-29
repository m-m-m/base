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
public interface Range<V> {

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

    Comparator<? super V> comparator = getComparator();
    int delta;
    V min = getMin();
    if (min != null) {
      delta = comparator.compare(value, min);
      if (delta < 0) {
        // value < min
        return false;
      }
    }
    V max = getMax();
    if (max != null) {
      delta = comparator.compare(value, max);
      if (delta > 0) {
        // value > max
        return false;
      }
    }
    return true;
  }

  /**
   * @param <T> type of the {@link #contains(Object) contained value}.
   * @return the unbounded {@link Range} instance (with {@link #getMin()} and {@link #getMax()} being {@code null}).
   */
  static <T> Range<T> unbounded() {

    return GenericRange.UNBOUNDED;
  }

}
