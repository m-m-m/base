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

/**
 * Simple utility class to convert to standard {@link java.time.temporal.Temporal} types.
 * 
 * @see #get()
 */
public class TemporalConverter {

  private static final TemporalConverter INSTANCE = new TemporalConverter();

  TemporalConverter() {

  }

  /**
   * @param value the {@link Object} to convert. Most likely a {@link java.time.temporal.Temporal} but may also be
   *        something else.
   * @return the converted {@link Instant} if conversion was possible. Otherwise {@code null}.
   */
  public Instant convertToInstant(Object value) {

    if (value instanceof Instant) {
      return (Instant) value;
    } else if (value instanceof LocalDateTime) {
      return ((LocalDateTime) value).toInstant(ZoneOffset.UTC);
    } else if (value instanceof LocalDate) {
      return ((LocalDate) value).atStartOfDay().toInstant(ZoneOffset.UTC);
    } else if (value instanceof OffsetDateTime) {
      return ((OffsetDateTime) value).toInstant();
    } else if (value instanceof OffsetDateTime) {
      return ((OffsetDateTime) value).toInstant();
    } else if (value instanceof ZonedDateTime) {
      return ((ZonedDateTime) value).toInstant();
    }
    return null;
  }

  /**
   * @param value the {@link Object} to convert. Most likely a {@link java.time.temporal.Temporal} but may also be
   *        something else.
   * @return the converted {@link LocalDateTime} if conversion was possible. Otherwise {@code null}.
   */
  public LocalDateTime convertToLocalDateTime(Object value) {

    if (value instanceof LocalDateTime) {
      return (LocalDateTime) value;
    } else if (value instanceof Instant) {
      return LocalDateTime.ofInstant((Instant) value, ZoneOffset.UTC);
    } else if (value instanceof LocalDate) {
      return ((LocalDate) value).atStartOfDay();
    } else if (value instanceof OffsetDateTime) {
      return ((OffsetDateTime) value).toLocalDateTime();
    } else if (value instanceof ZonedDateTime) {
      return ((ZonedDateTime) value).toLocalDateTime();
    }
    return null;
  }

  /**
   * @param value the {@link Object} to convert. Most likely a {@link java.time.temporal.Temporal} but may also be
   *        something else.
   * @return the converted {@link OffsetDateTime} if conversion was possible. Otherwise {@code null}.
   */
  public OffsetDateTime convertToOffsetDateTime(Object value) {

    if (value instanceof OffsetDateTime) {
      return (OffsetDateTime) value;
    } else if (value instanceof Instant) {
      ((Instant) value).atOffset(ZoneOffset.UTC);
    } else if (value instanceof LocalDateTime) {
      return ((LocalDateTime) value).atOffset(ZoneOffset.UTC); // not really accurate
    } else if (value instanceof LocalDate) {
      return ((LocalDate) value).atStartOfDay().atOffset(ZoneOffset.UTC); // even less accurate;
    } else if (value instanceof ZonedDateTime) {
      return ((ZonedDateTime) value).toOffsetDateTime();
    }
    return null;
  }

  /**
   * @param value the {@link Object} to convert. Most likely a {@link java.time.temporal.Temporal} but may also be
   *        something else.
   * @return the converted {@link ZonedDateTime} if conversion was possible. Otherwise {@code null}.
   */
  public ZonedDateTime convertToZonedDateTime(Object value) {

    if (value instanceof ZonedDateTime) {
      return (ZonedDateTime) value;
    } else if (value instanceof Instant) {
      return ((Instant) value).atZone(ZoneOffset.UTC);
    } else if (value instanceof OffsetDateTime) {
      return ((OffsetDateTime) value).toZonedDateTime();
    } else if (value instanceof LocalDateTime) {
      return ((LocalDateTime) value).atZone(ZoneOffset.UTC); // not really accurate
    } else if (value instanceof LocalDate) {
      return ((LocalDate) value).atStartOfDay().atZone(ZoneOffset.UTC); // even less accurate;
    }
    return null;
  }

  /**
   * @param value the {@link Object} to convert. Most likely a {@link java.time.temporal.Temporal} but may also be
   *        something else.
   * @return the converted {@link LocalDate} if conversion was possible. Otherwise {@code null}.
   */
  public LocalDate convertToLocalDate(Object value) {

    if (value instanceof LocalDate) {
      return (LocalDate) value;
    } else if (value instanceof Instant) {
      return LocalDate.ofInstant((Instant) value, ZoneOffset.UTC);
    } else if (value instanceof LocalDateTime) {
      return ((LocalDateTime) value).toLocalDate();
    } else if (value instanceof OffsetDateTime) {
      return ((OffsetDateTime) value).toLocalDate();
    } else if (value instanceof ZonedDateTime) {
      return ((ZonedDateTime) value).toLocalDate();
    }
    return null;
  }

  /**
   * @param value the {@link Object} to convert. Most likely a {@link java.time.temporal.Temporal} but may also be
   *        something else.
   * @return the converted {@link LocalTime} if conversion was possible. Otherwise {@code null}.
   */
  public LocalTime convertToLocalTime(Object value) {

    if (value instanceof LocalTime) {
      return (LocalTime) value;
    } else if (value instanceof OffsetTime) {
      return ((OffsetTime) value).toLocalTime();
    } else if (value instanceof LocalDateTime) {
      return ((LocalDateTime) value).toLocalTime();
    } else if (value instanceof OffsetDateTime) {
      return ((OffsetDateTime) value).toLocalTime();
    } else if (value instanceof ZonedDateTime) {
      return ((ZonedDateTime) value).toLocalTime();
    } else if (value instanceof Instant) {
      return ((Instant) value).atOffset(ZoneOffset.UTC).toLocalTime();
    }
    return null;
  }

  /**
   * @param value the {@link Object} to convert. Most likely a {@link java.time.temporal.Temporal} but may also be
   *        something else.
   * @return the converted {@link OffsetTime} if conversion was possible. Otherwise {@code null}.
   */
  public OffsetTime convertToOffsetTime(Object value) {

    if (value instanceof OffsetTime) {
      return (OffsetTime) value;
    } else if (value instanceof LocalTime) {
      return ((LocalTime) value).atOffset(ZoneOffset.UTC);
    } else if (value instanceof LocalDateTime) {
      return ((LocalDateTime) value).toLocalTime().atOffset(ZoneOffset.UTC);
    } else if (value instanceof OffsetDateTime) {
      return ((OffsetDateTime) value).toOffsetTime();
    } else if (value instanceof ZonedDateTime) {
      return ((ZonedDateTime) value).toOffsetDateTime().toOffsetTime();
    } else if (value instanceof Instant) {
      return ((Instant) value).atOffset(ZoneOffset.UTC).toOffsetTime();
    }
    return null;
  }

  /**
   * @return the singleton instance.
   */
  public static TemporalConverter get() {

    return INSTANCE;
  }

}
