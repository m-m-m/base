/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.compare;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;

import io.github.mmm.base.impl.NumberHelper;
import io.github.mmm.base.temporal.TemporalConverter;

/// A [CompareOperator] represents an operator able to [compare][#evalComparable(Comparable, Comparable)] two
/// given values.
///
/// @see #evalObject(Object, Object)
/// @see #evalComparable(Comparable, Comparable)
/// @see #evalNumber(Number, Number)
/// @see #evalDouble(double, double)
/// @see #evalDelta(int)
/// @see #negate()
/// @since 1.0.0
public enum CompareOperator {

  /// [CompareOperator] to check if some value is greater than another.
  GREATER_THAN(">", "greater than") {

    @Override
    public boolean evalDelta(int delta) {

      return delta > 0;
    }

    @Override
    public CompareOperator negate() {

      return LESS_OR_EQUAL;
    }
  },

  /// [CompareOperator] to check if some value is greater or equal to another.
  GREATER_OR_EQUAL(">=", "greater or equal to") {

    @Override
    public boolean evalDelta(int delta) {

      return delta >= 0;
    }

    @Override
    public CompareOperator negate() {

      return LESS_THAN;
    }
  },

  /// [CompareOperator] to check if some value is less than another.
  LESS_THAN("<", "less than") {

    @Override
    public boolean evalDelta(int delta) {

      if (delta == Integer.MIN_VALUE) {
        return false;
      }
      return delta < 0;
    }

    @Override
    public CompareOperator negate() {

      return GREATER_OR_EQUAL;
    }
  },

  /// [CompareOperator] to check if some value is less or equal than another.
  LESS_OR_EQUAL("<=", "less or equal to") {

    @Override
    public boolean evalDelta(int delta) {

      if (delta == Integer.MIN_VALUE) {
        return false;
      }
      return delta <= 0;
    }

    @Override
    public CompareOperator negate() {

      return GREATER_THAN;
    }
  },

  /// [CompareOperator] to check if objects are [equal][Object#equals(Object)].
  EQUAL("==", "equal to") {

    @Override
    public boolean evalDelta(int delta) {

      return delta == 0;
    }

    @Override
    public CompareOperator negate() {

      return NOT_EQUAL;
    }
  },

  /// [CompareOperator] to check if objects are NOT [equal][Object#equals(Object)].
  NOT_EQUAL("!=", "not equal to") {

    @Override
    public boolean evalDelta(int delta) {

      return delta != 0;
    }

    @Override
    public CompareOperator negate() {

      return EQUAL;
    }
  };

  private final String symbol;

  private final String title;

  /// The constructor.
  ///
  /// @param value is the [raw value][#getSymbol()] (symbol).
  /// @param title is the [string representation][#toString()].
  /// @param evalTrueIfEquals - `true` if [CompareOperator] [evaluates][#evalObject(Object, Object)] to
  ///        `true` if arguments are equal, `false` otherwise.
  /// @param less - [Boolean#TRUE] if [CompareOperator] [evaluates][#evalObject(Object, Object)] to
  ///        `true` if first argument is less than second, [Boolean#FALSE] on greater, `null`
  ///        otherwise.
  private CompareOperator(String value, String title) {

    this.symbol = value;
    this.title = title;
  }

  /// @return the symbol of this [CompareOperator] ("==", "`>`", "`>=`", "`<`", or
  ///         "`<=`")
  public String getSymbol() {

    return this.symbol;
  }

  /// @return the negation of this [CompareOperator] that [evaluates][#evalDouble(double, double)] to the
  ///         negated result.
  public abstract CompareOperator negate();

  /// @param delta the signum of [Comparable#compareTo(Object)] or [Integer#MIN_VALUE] if the arguments are
  ///        incompatible (e.g. exactly one is `null` or [String] compared with [Boolean]).
  /// @return the result of the [CompareOperator] for the given `delta`.
  public abstract boolean evalDelta(int delta);

  /// This method evaluates this [CompareOperator] for the given arguments.
  ///
  /// @param arg1 is the first argument.
  /// @param arg2 is the second argument.
  /// @return the result of the [CompareOperator] applied to the given arguments.
  public boolean evalDouble(double arg1, double arg2) {

    double delta = arg1 - arg2;
    if (delta < 0) {
      return evalDelta(-1);
    } else if (delta > 0) {
      return evalDelta(1);
    } else {
      return evalDelta(0);
    }
  }

  /// This method evaluates this [CompareOperator] for the given [Comparable] arguments.
  ///
  /// @param <T> type of the [Comparable] objects.
  /// @param arg1 is the first argument.
  /// @param arg2 is the second argument.
  /// @return the result of the [CompareOperator] applied to the given arguments.
  /// @see #evalObject(Object, Object)
  public <T extends Comparable<T>> boolean evalComparable(T arg1, T arg2) {

    int delta;
    if (arg1 == arg2) {
      delta = 0;
    } else if ((arg1 == null) || (arg2 == null)) {
      delta = Integer.MIN_VALUE; // incompatible arguments
    } else {
      delta = arg1.compareTo(arg2);
    }
    return evalDelta(delta);
  }

  /// Generic variant of [#evalComparable(Comparable, Comparable)] that tries to convert incompatible arguments:
  ///
  /// - If both arguments are same it will return [evalDelta(0)][#evalDelta(int)].
  /// - If only exactly one of the arguments is `null` it will return [evalDelta][#evalDelta(int)]([Integer#MIN_VALUE]).
  /// - If both arguments are [Number]s it delegates to [#evalNumber(Number, Number)]
  /// - If both arguments are [Comparable]s:
  ///
  /// - If both [Comparable]s have different types but each of them is a standard
  /// [java.time.temporal.Temporal] or [java.util.Date]/[java.util.Calendar], then one of them is
  /// converted to the other. Please note that some combinations are still invalid so e.g. [LocalDate] and
  /// [LocalTime] are not comparable.
  /// - If we now have two [Comparable]s of the same type, we delegate to
  /// [#evalComparable(Comparable, Comparable)].
  ///
  /// @param arg1 is the first argument.
  /// @param arg2 is the second argument.
  /// @return the result of the [CompareOperator] applied to the given arguments.
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public boolean evalObject(Object arg1, Object arg2) {

    int delta = Integer.MIN_VALUE;
    if (arg1 == arg2) {
      delta = 0;
    } else if ((arg1 != null) && (arg2 != null)) {
      if ((arg1 instanceof Number) && (arg2 instanceof Number)) {
        return evalNumber((Number) arg1, (Number) arg2);
      } else if ((arg1 instanceof Comparable) && (arg2 instanceof Comparable)) {
        try {
          Boolean result = TemporalConverter.get().convertAndEvaluate(arg1, arg2,
              (x1, x2) -> Boolean.valueOf(evalComparable((Comparable) x1, (Comparable) x2)));
          if (result != null) {
            return result.booleanValue();
          }
          Comparable c1 = (Comparable) arg1;
          Comparable c2 = (Comparable) arg2;
          return evalComparable(c1, c2);
        } catch (ClassCastException e) {
        }
      }
    }
    return evalDelta(delta);
  }

  /// This method evaluates this [CompareOperator] for the given [Number] arguments.
  ///
  /// @param arg1 is the first argument.
  /// @param arg2 is the second argument.
  /// @return the result of the [CompareOperator] applied to the given arguments.
  /// @see #evalObject(Object, Object)
  public boolean evalNumber(Number arg1, Number arg2) {

    if (arg1 == arg2) {
      return evalDelta(0);
    } else if ((arg1 == null) || (arg2 == null)) {
      return !evalDelta(0); // incompatible arguments
    } else if (arg1 instanceof BigDecimal) {
      return evalComparable((BigDecimal) arg1, NumberHelper.toBigDecimal(arg2));
    } else if (arg1 instanceof BigInteger) {
      if (arg2 instanceof BigDecimal) {
        return evalComparable(new BigDecimal((BigInteger) arg1), (BigDecimal) arg2);
      } else {
        return evalComparable((BigInteger) arg1, NumberHelper.toBigInteger(arg2));
      }
    } else {
      return evalDouble(arg1.doubleValue(), arg2.doubleValue());
    }
  }

  @Override
  public String toString() {

    return this.title;
  }

  /// This method gets the [CompareOperator] for the given `symbol`.
  ///
  /// @param value is the [symbol][#getSymbol()] of the requested [CompareOperator].
  /// @return the requested [CompareOperator] or `null` if no such [CompareOperator] exists.
  public static CompareOperator ofSymbol(String value) {

    for (CompareOperator comparator : values()) {
      if (comparator.symbol.equals(value)) {
        return comparator;
      }
    }
    return null;
  }
}
