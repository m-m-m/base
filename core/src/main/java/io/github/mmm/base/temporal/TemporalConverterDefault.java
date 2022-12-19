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
import java.util.function.BiFunction;

/**
 * Simple utility class to convert to standard {@link java.time.temporal.Temporal} types.
 *
 * @see #get()
 */
public class TemporalConverterDefault implements TemporalConverter {

  private static TemporalConverterDefault INSTANCE;

  TemporalConverterDefault() {

    super();
    if (INSTANCE == null) {
      INSTANCE = this;
    }
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
      Instant instant = (Instant) value;
      return LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), ZoneOffset.UTC);
    } else if (value instanceof LocalDate) {
      return ((LocalDate) value).atStartOfDay();
    } else if (value instanceof OffsetDateTime) {
      return ((OffsetDateTime) value).withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
    } else if (value instanceof ZonedDateTime) {
      return ((ZonedDateTime) value).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
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
      Instant instant = (Instant) value;
      long localEpochDay = Math.floorDiv(instant.getEpochSecond(), 24 * 60 * 60);
      return LocalDate.ofEpochDay(localEpochDay);
    } else if (value instanceof LocalDateTime) {
      return ((LocalDateTime) value).toLocalDate();
    } else if (value instanceof OffsetDateTime) {
      return ((OffsetDateTime) value).withOffsetSameInstant(ZoneOffset.UTC).toLocalDate();
    } else if (value instanceof ZonedDateTime) {
      return ((ZonedDateTime) value).withZoneSameInstant(ZoneOffset.UTC).toLocalDate();
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
      return ((OffsetDateTime) value).withOffsetSameInstant(ZoneOffset.UTC).toLocalTime();
    } else if (value instanceof ZonedDateTime) {
      return ((ZonedDateTime) value).withZoneSameInstant(ZoneOffset.UTC).toLocalTime();
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
   * @param <T> type of the result.
   * @param temporal1 the first temporal value (with date and/or time).
   * @param temporal2 the second temporal value (with date and/or time).
   * @param function the function to apply on the two given temporal values after assuring they are of same type. This
   *        method will therefore convert on of the given temporal values so it has the same type as the other one.
   * @return the result of the given {@link BiFunction} with the given temporal values applied or {@code null} if the a
   *         temporal value has an unknown or unsupported type.
   */
  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public <T> T convertAndEvaluate(Object temporal1, Object temporal2, BiFunction<?, ?, T> function) {

    Class<?> t1 = temporal1.getClass();
    Class<?> t2 = temporal2.getClass();
    Object c1 = temporal1;
    Object c2 = temporal2;
    if (!t1.equals(t2)) {
      if (c1 instanceof LocalTime) {
        c2 = convertToLocalTime(c2);
      } else if (c2 instanceof LocalTime) {
        c1 = convertToLocalTime(c1);
      } else if (c1 instanceof OffsetTime) {
        c2 = convertToOffsetTime(c2);
      } else if (c2 instanceof OffsetTime) {
        c1 = convertToOffsetTime(c1);
      } else if (c1 instanceof LocalDate) {
        c2 = convertToLocalDate(c2);
      } else if (c2 instanceof LocalDate) {
        c1 = convertToLocalDate(c1);
      } else if (c1 instanceof Instant) {
        c2 = convertToInstant(c2);
      } else if (c2 instanceof Instant) {
        c1 = convertToInstant(c1);
      } else if (c1 instanceof LocalDateTime) {
        c2 = convertToLocalDateTime(c2);
      } else if (c2 instanceof LocalDateTime) {
        c1 = convertToLocalDateTime(c1);
      } else if (c1 instanceof OffsetDateTime) {
        c2 = convertToOffsetDateTime(c2);
      } else if (c2 instanceof OffsetDateTime) {
        c1 = convertToOffsetDateTime(c1);
      } else if (c1 instanceof ZonedDateTime) {
        c2 = convertToZonedDateTime(c2);
      } else if (c2 instanceof ZonedDateTime) {
        c1 = convertToZonedDateTime(c1);
      } else {
        return null;
      }
    }
    return (T) ((BiFunction) function).apply(c1, c2);
  }

  /**
   * @return the singleton instance.
   */
  public static TemporalConverterDefault get() {

    if (INSTANCE == null) {
      return new TemporalConverterDefault();
    }
    return INSTANCE;
  }

}
