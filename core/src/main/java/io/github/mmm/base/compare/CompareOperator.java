/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.compare;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;

import io.github.mmm.base.impl.NumberHelper;
import io.github.mmm.base.temporal.TemporalConverter;

/**
 * A {@link CompareOperator} represents an operator able to {@link #evalComparable(Comparable, Comparable) compare} two
 * given values.
 *
 * @see #evalObject(Object, Object)
 * @see #evalComparable(Comparable, Comparable)
 * @see #evalNumber(Number, Number)
 * @see #evalDouble(double, double)
 * @see #evalDelta(int)
 * @see #negate()
 */
enum CompareOperator2 {

  /** {@link CompareOperator} to check if some value is greater than another. */
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

  /** {@link CompareOperator} to check if some value is greater or equal to another. */
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

  /** {@link CompareOperator} to check if some value is less than another. */
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

  /** {@link CompareOperator} to check if some value is less or equal than another. */
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

  /**
   * {@link CompareOperator} to check if objects are {@link Object#equals(Object) equal}.
   */
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

  /**
   * {@link CompareOperator} to check if objects are NOT {@link Object#equals(Object) equal}.
   */
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

  /**
   * The constructor.
   *
   * @param value is the {@link #getSymbol() raw value} (symbol).
   * @param title is the {@link #toString() string representation}.
   * @param evalTrueIfEquals - {@code true} if {@link CompareOperator} {@link #evalObject(Object, Object) evaluates} to
   *        {@code true} if arguments are equal, {@code false} otherwise.
   * @param less - {@link Boolean#TRUE} if {@link CompareOperator} {@link #evalObject(Object, Object) evaluates} to
   *        {@code true} if first argument is less than second, {@link Boolean#FALSE} on greater, {@code null}
   *        otherwise.
   */
  private CompareOperator(String value, String title) {

    this.symbol = value;
    this.title = title;
  }

  /**
   * @return the symbol of this {@link CompareOperator} ("==", "{@literal >}", "{@literal >=}", "{@literal <}", or
   *         "{@literal <=}")
   */
  public String getSymbol() {

    return this.symbol;
  }

  /**
   * @return the negation of this {@link CompareOperator} that {@link #evalDouble(double, double) evaluates} to the
   *         negated result.
   */
  public abstract CompareOperator negate();

  /**
   * @param delta the signum of {@link Comparable#compareTo(Object)} or {@link Integer#MIN_VALUE} if the arguments are
   *        incompatible (e.g. exactly one is {@code null} or {@link String} compared with {@link Boolean}).
   * @return the result of the {@link CompareOperator} for the given {@code delta}.
   */
  public abstract boolean evalDelta(int delta);

  /**
   * This method evaluates this {@link CompareOperator} for the given arguments.
   *
   * @param arg1 is the first argument.
   * @param arg2 is the second argument.
   * @return the result of the {@link CompareOperator} applied to the given arguments.
   */
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

  /**
   * This method evaluates this {@link CompareOperator} for the given {@link Comparable} arguments.
   *
   * @param <T> type of the {@link Comparable} objects.
   * @param arg1 is the first argument.
   * @param arg2 is the second argument.
   * @return the result of the {@link CompareOperator} applied to the given arguments.
   * @see #evalObject(Object, Object)
   */
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

  /**
   * Generic variant of {@link #evalComparable(Comparable, Comparable)} that tries to convert incompatible arguments:
   * <ul>
   * <li>If both arguments are same it will return {@link #evalDelta(int) evalDelta(0)}.</li>
   * <li>If only exactly one of the arguments is {@code null} it will return {@link #evalDelta(int)
   * evalDelta}({@link Integer#MIN_VALUE}).</li>
   * <li>If both arguments are {@link Number}s it delegates to {@link #evalNumber(Number, Number)}</li>
   * <li>If both arguments are {@link Comparable}s:
   * <ul>
   * <li>If both {@link Comparable}s have different types but each of them is a standard
   * {@link java.time.temporal.Temporal} or {@link java.util.Date}/{@link java.util.Calendar}, then one of them is
   * converted to the other. Please note that some combinations are still invalid so e.g. {@link LocalDate} and
   * {@link LocalTime} are not comparable.</li>
   * <li>If we now have two {@link Comparable}s of the same type, we delegate to
   * {@link #evalComparable(Comparable, Comparable)}.</li>
   * </ul>
   * </li>
   * </ul>
   *
   * @param arg1 is the first argument.
   * @param arg2 is the second argument.
   * @return the result of the {@link CompareOperator} applied to the given arguments.
   */
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

  /**
   * This method evaluates this {@link CompareOperator} for the given {@link Number} arguments.
   *
   * @param arg1 is the first argument.
   * @param arg2 is the second argument.
   * @return the result of the {@link CompareOperator} applied to the given arguments.
   * @see #evalObject(Object, Object)
   */
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

  /**
   * This method gets the {@link CompareOperator} for the given {@code symbol}.
   *
   * @param value is the {@link #getSymbol() symbol} of the requested {@link CompareOperator}.
   * @return the requested {@link CompareOperator} or {@code null} if no such {@link CompareOperator} exists.
   */
  public static CompareOperator ofSymbol(String value) {

    for (CompareOperator comparator : values()) {
      if (comparator.symbol.equals(value)) {
        return comparator;
      }
    }
    return null;
  }
}
