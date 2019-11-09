/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.compare;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;

import io.github.mmm.base.temporal.TemporalConverterLegacy;

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
public enum CompareOperator {

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
   * @return the symbol of this {@link CompareOperator} ("==", ">", ">=", "<", or "<=")
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
        Comparable c1 = (Comparable) arg1;
        Comparable c2 = (Comparable) arg2;
        Class<?> t1 = arg1.getClass();
        Class<?> t2 = arg2.getClass();
        if (!t1.equals(t2)) {
          TemporalConverterLegacy c = TemporalConverterLegacy.get();
          if (c1 instanceof LocalTime) {
            c2 = c.convertToLocalTime(c2);
          } else if (c2 instanceof LocalTime) {
            c1 = c.convertToLocalTime(c1);
          } else if (c1 instanceof OffsetTime) {
            c2 = c.convertToOffsetTime(c2);
          } else if (c2 instanceof OffsetTime) {
            c1 = c.convertToOffsetTime(c1);
          } else if (c1 instanceof LocalDate) {
            c2 = c.convertToLocalDate(c2);
          } else if (c2 instanceof LocalDate) {
            c1 = c.convertToLocalDate(c1);
          } else if (c1 instanceof Instant) {
            c2 = c.convertToInstant(c2);
          } else if (c2 instanceof Instant) {
            c1 = c.convertToInstant(c1);
          } else if (c1 instanceof LocalDateTime) {
            c2 = c.convertToLocalDateTime(c2);
          } else if (c2 instanceof LocalDateTime) {
            c1 = c.convertToLocalDateTime(c1);
          } else if (c1 instanceof OffsetDateTime) {
            c2 = c.convertToOffsetDateTime(c2);
          } else if (c2 instanceof OffsetDateTime) {
            c1 = c.convertToOffsetDateTime(c1);
          } else if (c1 instanceof ZonedDateTime) {
            c2 = c.convertToZonedDateTime(c2);
          } else if (c2 instanceof ZonedDateTime) {
            c1 = c.convertToZonedDateTime(c1);
          } else if (c1 instanceof Date) {
            c2 = c.convertToDate(c2);
          } else if (c2 instanceof Date) {
            c1 = c.convertToDate(c1);
          }
        }
        try {
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
      return evalComparable((BigDecimal) arg1, toBigDecimal(arg2));
    } else if (arg1 instanceof BigInteger) {
      if (arg2 instanceof BigDecimal) {
        return evalComparable(new BigDecimal((BigInteger) arg1), (BigDecimal) arg2);
      } else {
        return evalComparable((BigInteger) arg1, toBigInteger(arg2));
      }
    } else {
      return evalDouble(arg1.doubleValue(), arg2.doubleValue());
    }
  }

  private static BigDecimal toBigDecimal(Number value) {

    if (value instanceof BigDecimal) {
      return (BigDecimal) value;
    } else if (value instanceof BigInteger) {
      return new BigDecimal((BigInteger) value);
    } else if (isNonDecimalNumber(value)) {
      return BigDecimal.valueOf(value.longValue());
    } else {
      return BigDecimal.valueOf(value.doubleValue());
    }
  }

  private static boolean isNonDecimalNumber(Number value) {

    return (value instanceof Long) || (value instanceof Integer) || (value instanceof Short) || (value instanceof Byte);
  }

  private static BigInteger toBigInteger(Number value) {

    if (value instanceof BigInteger) {
      return (BigInteger) value;
    } else {
      return BigInteger.valueOf(value.longValue());
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
  public static CompareOperator fromValue(String value) {

    for (CompareOperator comparator : values()) {
      if (comparator.symbol.equals(value)) {
        return comparator;
      }
    }
    return null;
  }
}
