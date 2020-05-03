/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.temporal;

import java.util.Calendar;
import java.util.Date;
import java.util.function.BiFunction;

/**
 * Extends {@link TemporalConverterDefault} with support for the legacy types {@link Date} and {@link Calendar}.
 *
 * @since 1.0.0
 */
public class TemporalConverterJavaUtil implements TemporalConverter {

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

    Object c1 = temporal1;
    Object c2 = temporal2;
    if (c1 instanceof Date) {
      c2 = convertToDate(c2);
    } else if (c2 instanceof Date) {
      c1 = convertToDate(c1);
    } else if (c1 instanceof Calendar) {
      c1 = convertToDate(c1);
    } else if (c2 instanceof Calendar) {
      c1 = convertToDate(c1);
    } else {
      return null;
    }
    return (T) ((BiFunction) function).apply(c1, c2);
  }

}
