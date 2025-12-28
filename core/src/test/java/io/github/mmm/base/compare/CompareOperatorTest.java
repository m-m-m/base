/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.compare;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.base.temporal.TemporalConverterLegacy;

/**
 * Test of {@link CompareOperator}.
 */
public class CompareOperatorTest extends Assertions {

  /** Test of {@link CompareOperator#EQUAL}. */
  @Test
  public void testEqual() {

    // arrange
    CompareOperator op = CompareOperator.EQUAL;

    // act + assert
    assertThat(op.getSymbol()).isEqualTo("==");
    assertThat(op.toString()).isEqualTo("equal to");

    verifyOperator(op, false, false, true);
  }

  /** Test of {@link CompareOperator#NOT_EQUAL}. */
  @Test
  public void testNotEqual() {

    // arrange
    CompareOperator op = CompareOperator.NOT_EQUAL;

    // act + assert
    assertThat(op.getSymbol()).isEqualTo("!=");
    assertThat(op.toString()).isEqualTo("not equal to");

    verifyOperator(op, true, true, false);
  }

  /** Test of {@link CompareOperator#GREATER_THAN}. */
  @Test
  public void testGreaterThan() {

    // arrange
    CompareOperator op = CompareOperator.GREATER_THAN;

    // act + assert
    assertThat(op.getSymbol()).isEqualTo(">");
    assertThat(op.toString()).isEqualTo("greater than");

    verifyOperator(op, false, true, false);
  }

  /** Test of {@link CompareOperator#GREATER_OR_EQUAL}. */
  @Test
  public void testGreaterOrEqual() {

    // arrange
    CompareOperator op = CompareOperator.GREATER_OR_EQUAL;

    // act + assert
    assertThat(op.getSymbol()).isEqualTo(">=");
    assertThat(op.toString()).isEqualTo("greater or equal to");

    verifyOperator(op, false, true, true);
  }

  /** Test of {@link CompareOperator#LESS_THAN}. */
  @Test
  public void testLessThan() {

    // arrange
    CompareOperator op = CompareOperator.LESS_THAN;

    // act + assert
    assertThat(op.getSymbol()).isEqualTo("<");
    assertThat(op.toString()).isEqualTo("less than");

    verifyOperator(op, true, false, false);
  }

  /** Test of {@link CompareOperator#LESS_OR_EQUAL}. */
  @Test
  public void testLessOrEqual() {

    // arrange
    CompareOperator op = CompareOperator.LESS_OR_EQUAL;

    // act + assert
    assertThat(op.getSymbol()).isEqualTo("<=");
    assertThat(op.toString()).isEqualTo("less or equal to");

    verifyOperator(op, true, false, true);
  }

  private void verifyOperator(CompareOperator op, boolean acceptLess, boolean acceptGreater, boolean acceptEqual) {

    assertThat(op.evalDouble(1, 0)).isEqualTo(acceptGreater);
    assertThat(op.evalDouble(0, 1)).isEqualTo(acceptLess);
    assertThat(op.evalDouble(1, 1)).isEqualTo(acceptEqual);
    assertThat(op.evalNumber(Double.valueOf(1), Integer.valueOf(0))).isEqualTo(acceptGreater);
    assertThat(op.evalNumber(Double.valueOf(0), Integer.valueOf(1))).isEqualTo(acceptLess);
    assertThat(op.evalNumber(Double.valueOf(1), Integer.valueOf(1))).isEqualTo(acceptEqual);
    assertThat(op.evalNumber(new BigInteger("12345678901234567890"), BigInteger.valueOf(Long.MAX_VALUE)))
        .isEqualTo(acceptGreater);
  }

  /**
   * Test of {@link CompareOperator#evalObject(Object, Object)} with {@link Temporal} and {@link Date}/{@link Calendar}
   * values.
   */
  @Test
  public void testTemporals() {

    TemporalConverterLegacy.get(); // activate legacy Date + Calendar support
    TimeZone systemTimeZone = TimeZone.getDefault();
    TimeZone utc = TimeZone.getTimeZone("UTC");
    TimeZone.setDefault(utc); // set system timezone to UTC
    @SuppressWarnings("deprecation")
    Date date = new Date(1999 - 1900, 12 - 1, 31, 23, 59, 59);
    Calendar calendar = Calendar.getInstance();
    calendar.set(1999, 12 - 1, 31, 23, 59, 59);
    calendar.set(Calendar.MILLISECOND, 1);
    Instant instant = calendar.toInstant().plusMillis(1);
    OffsetDateTime offsetDateTime = instant.plusMillis(1).atOffset(ZoneOffset.ofHours(1));
    LocalDateTime localDateTime = offsetDateTime.plusNanos(1000_000).atZoneSameInstant(ZoneOffset.UTC)
        .toLocalDateTime();
    ZonedDateTime zonedDateTime = localDateTime.plusNanos(1000_000).atZone(ZoneOffset.UTC)
        .withZoneSameInstant(ZoneId.of("Europe/Paris"));
    LocalTime localTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).plusNanos(1000_000).toLocalTime();
    OffsetTime offsetTime = localTime.plusNanos(1000_000).atOffset(ZoneOffset.UTC);
    LocalDate localDate = LocalDate.of(2000, 01, 01);
    Object[] temporals = new Object[] { date, calendar, instant, offsetDateTime, localDateTime, zonedDateTime,
    localTime, offsetTime, localDate };
    for (int i = 0; i < temporals.length; i++) {
      for (int j = 0; j < temporals.length; j++) {
        CompareOperator op;
        if (i == j) {
          op = CompareOperator.EQUAL;
        } else if (i < j) {
          op = CompareOperator.LESS_THAN;
        } else {
          op = CompareOperator.GREATER_THAN;
        }
        Object arg1 = temporals[i];
        Object arg2 = temporals[j];
        if ((arg1 instanceof LocalDate) && (arg2 instanceof LocalTime)) {
          // ignore - not comparable combination
        } else if ((arg1 instanceof LocalTime) && (arg2 instanceof LocalDate)) {
          // ignore - not comparable combination
        } else if ((arg1 instanceof LocalDate) && (arg2 instanceof OffsetTime)) {
          // ignore - not comparable combination
        } else if ((arg1 instanceof OffsetTime) && (arg2 instanceof LocalDate)) {
          // ignore - not comparable combination
        } else {
          boolean result = op.evalObject(arg1, arg2);
          String description = arg1.getClass().getSimpleName() + ":" + arg1 + " " + op.getSymbol() + " "
              + arg2.getClass().getSimpleName() + ":" + arg2;
          assertThat(result).as(description).isTrue();
        }
      }
    }
    TimeZone.setDefault(systemTimeZone); // reset system timezone
  }

}
