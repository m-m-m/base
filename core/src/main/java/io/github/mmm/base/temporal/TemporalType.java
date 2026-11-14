/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.temporal;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import io.github.mmm.base.lang.ValueType;

/// Extends [ValueType] for [Temporal] types.
///
/// @param <V> the [type type][#getType()].
/// @since 1.0.0
public abstract class TemporalType<V> extends ValueType<V> {

  /// [TemporalType] for [Instant].
  public static final TemporalType<Instant> INSTANT = new TemporalType<>(Instant.class) {
    @Override
    public Instant parse(String value) {

      if (value == null) {
        return null;
      }
      return Instant.parse(value);
    }
  };

  /// [TemporalType] for [LocalDate].
  public static final TemporalType<LocalDate> LOCAL_DATE = new TemporalType<>(LocalDate.class) {
    @Override
    public LocalDate parse(String value) {

      if (value == null) {
        return null;
      }
      return LocalDate.parse(value);
    }
  };

  /// [TemporalType] for [LocalTime].
  public static final TemporalType<LocalTime> LOCAL_TIME = new TemporalType<>(LocalTime.class) {
    @Override
    public LocalTime parse(String value) {

      if (value == null) {
        return null;
      }
      return LocalTime.parse(value);
    }
  };

  /// [TemporalType] for [LocalDateTime].
  public static final TemporalType<LocalDateTime> LOCAL_DATE_TIME = new TemporalType<>(LocalDateTime.class) {
    @Override
    public LocalDateTime parse(String value) {

      if (value == null) {
        return null;
      }
      return LocalDateTime.parse(value);
    }
  };

  /// [TemporalType] for [ZonedDateTime].
  public static final TemporalType<ZonedDateTime> ZONED_DATE_TIME = new TemporalType<>(ZonedDateTime.class) {
    @Override
    public ZonedDateTime parse(String value) {

      if (value == null) {
        return null;
      }
      return ZonedDateTime.parse(value);
    }
  };

  /// [TemporalType] for [OffsetDateTime].
  public static final TemporalType<OffsetDateTime> OFFSET_DATE_TIME = new TemporalType<>(OffsetDateTime.class) {
    @Override
    public OffsetDateTime parse(String value) {

      if (value == null) {
        return null;
      }
      return OffsetDateTime.parse(value);
    }
  };

  /// [TemporalType] for [Duration].
  public static final TemporalType<Duration> DURATION = new TemporalType<>(Duration.class) {
    @Override
    public Duration parse(String value) {

      if (value == null) {
        return null;
      }
      return Duration.parse(value);
    }
  };

  /// [TemporalType] for [Period].
  public static final TemporalType<Period> PERIOD = new TemporalType<>(Period.class) {
    @Override
    public Period parse(String value) {

      if (value == null) {
        return null;
      }
      return Period.parse(value);
    }
  };

  /// [TemporalType] for [MonthDay].
  public static final TemporalType<MonthDay> MONTH_DAY = new TemporalType<>(MonthDay.class) {
    @Override
    public MonthDay parse(String value) {

      if (value == null) {
        return null;
      }
      return MonthDay.parse(value);
    }
  };

  /// The constructor.
  ///
  /// @param type the [value type][#getType()].
  public TemporalType(Class<V> type) {

    super(type);
  }

}
