/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.temporal;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.function.BiFunction;

/**
 * Extends {@link TemporalConverterDefault} with support for the legacy types {@link Date} and {@link Calendar}. We
 * consider {@link java.util.Date} and {@link java.util.Calendar} to be dead and therefore do not support them out of
 * the box. In case you need support for them, see {@link TemporalConverter#get()} how to change the default.
 *
 * @see #get()
 */
public class TemporalConverterLegacy extends TemporalConverterDefault {

  private static TemporalConverterLegacy INSTANCE;

  TemporalConverterLegacy() {

    super();
    if (INSTANCE == null) {
      INSTANCE = this;
    }
  }

  @Override
  public Instant convertToInstant(Object value) {

    if (value instanceof Date) {
      return ((Date) value).toInstant();
    } else if (value instanceof Calendar) {
      return ((Calendar) value).toInstant();
    }
    return super.convertToInstant(value);
  }

  @Override
  public LocalDateTime convertToLocalDateTime(Object value) {

    if (value instanceof Date) {
      return LocalDateTime.ofInstant(((Date) value).toInstant(), ZoneOffset.UTC);
    } else if (value instanceof Calendar) {
      return LocalDateTime.ofInstant(((Calendar) value).toInstant(), ZoneOffset.UTC);
    }
    return super.convertToLocalDateTime(value);
  }

  @Override
  public OffsetDateTime convertToOffsetDateTime(Object value) {

    if (value instanceof Date) {
      return ((Date) value).toInstant().atOffset(ZoneOffset.UTC);
    } else if (value instanceof Calendar) {
      return ((Calendar) value).toInstant().atOffset(ZoneOffset.UTC);
    }
    return super.convertToOffsetDateTime(value);
  }

  @Override
  public ZonedDateTime convertToZonedDateTime(Object value) {

    if (value instanceof Date) {
      return ((Date) value).toInstant().atZone(ZoneOffset.UTC);
    } else if (value instanceof Calendar) {
      return ((Calendar) value).toInstant().atZone(ZoneOffset.UTC);
    }
    return super.convertToZonedDateTime(value);
  }

  @Override
  public LocalDate convertToLocalDate(Object value) {

    if (value instanceof Date) {
      return LocalDate.ofInstant(((Date) value).toInstant(), ZoneOffset.UTC);
    } else if (value instanceof Calendar) {
      return LocalDate.ofInstant(((Calendar) value).toInstant(), ZoneOffset.UTC);
    }
    return super.convertToLocalDate(value);
  }

  @Override
  public LocalTime convertToLocalTime(Object value) {

    if (value instanceof Date) {
      return ((Date) value).toInstant().atOffset(ZoneOffset.UTC).toLocalTime();
    } else if (value instanceof Calendar) {
      return ((Calendar) value).toInstant().atOffset(ZoneOffset.UTC).toLocalTime();
    }
    return super.convertToLocalTime(value);
  }

  @Override
  public OffsetTime convertToOffsetTime(Object value) {

    if (value instanceof Date) {
      return ((Date) value).toInstant().atOffset(ZoneOffset.UTC).toOffsetTime();
    } else if (value instanceof Calendar) {
      return ((Calendar) value).toInstant().atOffset(ZoneOffset.UTC).toOffsetTime();
    }
    return super.convertToOffsetTime(value);
  }

  /**
   * @param value the {@link Object} to convert. Most likely a {@link java.time.temporal.Temporal}, {@link Date} or
   *        {@link Calendar} but may also be something else.
   * @return the converted {@link Date} if conversion was possible. Otherwise {@code null}.
   */
  public Date convertToDate(Object value) {

    if (value instanceof Date) {
      return (Date) value;
    } else if (value instanceof Calendar) {
      return ((Calendar) value).getTime();
    } else if (value instanceof Instant) {
      return Date.from((Instant) value);
    } else if (value instanceof LocalDateTime) {
      return Date.from(((LocalDateTime) value).toInstant(ZoneOffset.UTC));
    } else if (value instanceof LocalDate) {
      return Date.from(((LocalDate) value).atStartOfDay().toInstant(ZoneOffset.UTC));
    } else if (value instanceof ZonedDateTime) {
      return Date.from(((ZonedDateTime) value).toInstant());
    } else if (value instanceof OffsetDateTime) {
      return Date.from(((OffsetDateTime) value).toInstant());
    }
    return null;
  }

  /**
   * @param value the {@link Object} to convert. Most likely a {@link java.time.temporal.Temporal}, {@link Date} or
   *        {@link Calendar} but may also be something else.
   * @return the converted {@link Calendar} if conversion was possible. Otherwise {@code null}.
   */
  public Calendar convertToCalendar(Object value) {

    if (value instanceof Calendar) {
      return (Calendar) value;
    } else {
      Date date = convertToDate(value);
      if (date != null) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
      }
    }
    return null;
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public <T> T convertAndEvaluate(Object temporal1, Object temporal2, BiFunction<?, ?, T> function) {

    T result = super.convertAndEvaluate(temporal1, temporal2, function);
    if (result == null) {
      Object c1 = temporal1;
      Object c2 = temporal2;
      if (c1 instanceof Date) {
        c2 = convertToDate(c2);
      } else if (c2 instanceof Date) {
        c1 = convertToDate(c1);
      }
      return (T) ((BiFunction) function).apply(c1, c2);
    }
    return result;
  }

  /**
   * @return the singleton instance.
   */
  public static TemporalConverterLegacy get() {

    if (INSTANCE == null) {
      return new TemporalConverterLegacy();
    }
    return INSTANCE;
  }

}
