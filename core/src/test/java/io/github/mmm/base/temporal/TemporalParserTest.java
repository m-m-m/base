/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.temporal;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link TemporalParser}.
 */
public class TemporalParserTest extends Assertions {

  /**
   * Test of {@link TemporalParser#parse(String)} with {@link Instant}.
   */
  @Test
  public void testInstant() {

    Instant now = Instant.now();
    Temporal temporal = TemporalParser.get().parse(now.toString());
    assertThat(temporal).isEqualTo(now);
  }

  /**
   * Test of {@link TemporalParser#parse(String)} with {@link LocalDate}.
   */
  @Test
  public void testLocalDate() {

    LocalDate now = LocalDate.now();
    Temporal temporal = TemporalParser.get().parse(now.toString());
    assertThat(temporal).isEqualTo(now);
  }

  /**
   * Test of {@link TemporalParser#parse(String)} with {@link LocalTime}.
   */
  @Test
  public void testLocalTime() {

    LocalTime now = LocalTime.now();
    Temporal temporal = TemporalParser.get().parse(now.toString());
    assertThat(temporal).isEqualTo(now);
  }

  /**
   * Test of {@link TemporalParser#parse(String)} with {@link LocalDateTime}.
   */
  @Test
  public void testLocalDateTime() {

    LocalDateTime now = LocalDateTime.now();
    Temporal temporal = TemporalParser.get().parse(now.toString());
    assertThat(temporal).isEqualTo(now);
  }

  /**
   * Test of {@link TemporalParser#parse(String)} with {@link OffsetTime}.
   */
  @Test
  public void testOffsetTime() {

    OffsetTime now = OffsetTime.now();
    Temporal temporal = TemporalParser.get().parse(now.toString());
    assertThat(temporal).isEqualTo(now);
  }

  /**
   * Test of {@link TemporalParser#parse(String)} with {@link OffsetDateTime}.
   */
  @Test
  public void testOffsetDateTime() {

    OffsetDateTime now = Instant.now().atOffset(ZoneOffset.ofHours(1));
    Temporal temporal = TemporalParser.get().parse(now.toString());
    assertThat(temporal).isEqualTo(now);
  }

  /**
   * Test of {@link TemporalParser#parse(String)} with {@link ZonedDateTime}.
   */
  @Test
  public void testZonedDateTime() {

    ZonedDateTime now = Instant.now().atZone(ZoneId.of("Europe/Paris"));
    Temporal temporal = TemporalParser.get().parse(now.toString());
    assertThat(temporal).isEqualTo(now);
  }

  /**
   * Test of {@link TemporalParser#parse(String)} with {@link YearMonth}.
   */
  @Test
  public void testYearMonth() {

    YearMonth now = YearMonth.now();
    Temporal temporal = TemporalParser.get().parse(now.toString());
    assertThat(temporal).isEqualTo(now);
  }

  /**
   * Test of {@link TemporalParser#parse(String)} with invalid values.
   */
  @Test
  public void testInvalid() {

    TemporalParser parser = TemporalParser.get();
    assertThat(parser.parse(null)).isNull();
    assertThat(parser.parse("")).isNull();
    assertThat(parser.parse("2000")).isNull();
    assertThat(parser.parse("--12-31")).isNull();
    assertThat(parser.parse("23:59")).isNull();
    assertThat(parser.parse("23.59:59")).isNull();
    assertThat(parser.parse("2000:12-31")).isNull();
  }

}
