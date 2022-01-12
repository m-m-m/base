/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.util.Objects;
import java.util.function.Function;

/**
 * Implementation of {@link Range}.
 *
 * @param <V> type of the contained values.
 * @since 1.0.0
 */
public class RangeType<V extends Comparable<?>> extends AbstractRange<V> {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  static final RangeType UNBOUNDED = new RangeType(null, null) {
    @Override
    public Range intersection(Range range) {

      return range;
    }
  };

  static final String INVALID_STRING = BOUND_START_INCLUSIVE + MAX_UNBOUND + BOUND_SEPARATOR + MIN_UNBOUND
      + BOUND_END_INCLUSIVE;

  @SuppressWarnings({ "unchecked", "rawtypes" })
  static final RangeType INVALID = new RangeType(null, null) {

    @Override
    public Range intersection(Range range) {

      return this;
    }

    @Override
    public boolean contains(Comparable value) {

      return false;
    }

    @Override
    public String toString() {

      return INVALID_STRING;
    }
  };

  private final V min;

  private final V max;

  /**
   * The constructor.
   *
   * @param min - see {@link #getMin()}. To create an open range use the minimum value.
   * @param max - see {@link #getMax()}. To create an open range use the maximum value.
   */
  protected RangeType(V min, V max) {

    super();
    if ((min != null) && (max != null)) {
      int delta = getComparator().compare(min, max);
      if (delta > 0) {
        throw new IllegalArgumentException(min + " !<" + max);
      }
    }
    this.min = min;
    this.max = max;
  }

  @Override
  public V getMin() {

    return this.min;
  }

  @Override
  public V getMax() {

    return this.max;
  }

  /**
   * @param minimum the new {@link #getMin() minimum}.
   * @return a new {@link RangeType} where the {@link #getMin() minimum} is set to the given {@code minimum} value.
   */
  @Override
  public RangeType<V> withMin(V minimum) {

    if (Objects.equals(this.min, minimum)) {
      return this;
    }
    return new RangeType<>(minimum, this.max);
  }

  /**
   * @param maximum the new {@link #getMax() maximum}.
   * @return a new {@link RangeType} where the {@link #getMax() maximum} is set to the given {@code maximum} value.
   */
  @Override
  public RangeType<V> withMax(V maximum) {

    if (Objects.equals(this.max, maximum)) {
      return this;
    }
    return new RangeType<>(this.min, maximum);
  }

  /**
   * @param <T> type of the {@link #contains(Comparable) contained value}.
   * @param min the {@link #getMin() minimum}.
   * @param max the {@link #getMax() maximum}.
   * @return the specified {@link RangeType}.
   */
  public static <T extends Comparable<?>> RangeType<T> of(T min, T max) {

    if ((min == null) && (max == null)) {
      return UNBOUNDED;
    }
    return new RangeType<>(min, max);
  }

  /**
   * @param <T> type of the {@link #contains(Comparable) contained value}.
   * @param range the {@link Object#toString() string representation} of the {@link Range} to parse.
   * @param boundParser the {@link Function} capable to parse the individual bound values (min and max).
   * @return the parsed {@link RangeType}.
   */
  public static <T extends Comparable<?>> RangeType<T> parse(String range, Function<String, T> boundParser) {

    if ((range == null) || range.isEmpty()) {
      return UNBOUNDED;
    }
    if (INVALID_STRING.equals(range)) {
      return INVALID;
    }
    int seperatorIndex = range.indexOf(BOUND_SEPARATOR);
    if (seperatorIndex > 0) {
      int length = range.length();
      int end = length - 1;
      Boolean startInclusive = parseStartBound(range.charAt(0), range);
      Boolean endInclusive = parseEndBound(range.charAt(end), range);
      if ((startInclusive != null) && (endInclusive != null)) {
        String minString = range.substring(1, seperatorIndex);
        T min;
        if (minString.equals(MIN_UNBOUND)) {
          min = null;
        } else {
          min = boundParser.apply(minString);
        }
        String maxString = range.substring(seperatorIndex + 1, end);
        T max;
        if (maxString.equals(MAX_UNBOUND)) {
          max = null;
        } else {
          max = boundParser.apply(maxString);
        }
        if (((min != null) == startInclusive.booleanValue()) && (max != null) == endInclusive.booleanValue()) {
          return RangeType.of(min, max);
        }
      }
    }
    throw new IllegalArgumentException(range);
  }

  private static Boolean parseStartBound(char c, String range) {

    if (c == BOUND_START_INCLUSIVE) {
      return Boolean.TRUE;
    } else if (c == BOUND_START_EXCLUSIVE) {
      return Boolean.FALSE;
    } else {
      return null;
    }
  }

  private static Boolean parseEndBound(char c, String range) {

    if (c == BOUND_END_INCLUSIVE) {
      return Boolean.TRUE;
    } else if (c == BOUND_END_EXCLUSIVE) {
      return Boolean.FALSE;
    } else {
      return null;
    }
  }
}
