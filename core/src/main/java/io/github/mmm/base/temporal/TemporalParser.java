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
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mmm.base.lang.FromStringParser;

/// Parser that can detect [String]s representing a [Temporal] value via [#parse(String)] but returns
/// `null` if not a [Temporal] instead of throwing an exception.
///
/// @see #parse(String)
/// @since 1.0.0
public final class TemporalParser implements FromStringParser<Temporal> {

  private static final Pattern TEMPORAL_PATTERN = Pattern.compile(
      "([0-9]{4}-[0-1][0-9]-[0-3][0-9])?T?([0-2][0-9]:[0-5][0-9]:[0-5][0-9](.[0-9]{1,9})?(Z|[+][0-9]{2}:[0-9]{2}(\\[[^\\]]+\\])?)?)?");

  private static final Pattern YEAR_MONTH_PATTERN = Pattern.compile("[0-9]{4}-[0-1][0-9]");

  private static final TemporalParser INSTANCE = new TemporalParser();

  private TemporalParser() {

  }

  /// @param value the [default string representation][Object#toString()] of a (potential) [Temporal] value.
  ///        This can be [Instant], [LocalDate], [LocalTime], [LocalDateTime],
  ///        [OffsetTime], [OffsetDateTime], [ZonedDateTime], or [YearMonth]. Please note that
  ///        [java.time.chrono.ChronoLocalDate] such as [java.time.chrono.HijrahDate],
  ///        [java.time.chrono.JapaneseDate], [java.time.chrono.MinguoDate], or
  ///        [java.time.chrono.ThaiBuddhistDate] are not supported. Also [java.time.Year] is not supported as
  ///        it can easily be confused with a regular integer and can also be detected easily with just two lines of
  ///        code.
  /// @return the given `value` parsed as [Temporal] or `null` if not any of the supported
  ///         [Temporal] types.
  @Override
  public Temporal parse(String value) {

    if (value == null) {
      return null;
    }
    int length = value.length();
    if (length < 5) {
      return null;
    }
    if ((length == 7) && YEAR_MONTH_PATTERN.matcher(value).matches()) {
      return YearMonth.parse(value);
    }
    Matcher matcher = TEMPORAL_PATTERN.matcher(value);
    if (matcher.matches()) {
      String date = matcher.group(1);
      String time = matcher.group(2);
      if (time == null) {
        if (date == null) {
          return null;
        }
        return LocalDate.parse(value);
      } else if (date == null) {
        String tz = matcher.group(4);
        if (tz == null) {
          return LocalTime.parse(value);
        } else {
          return OffsetTime.parse(value);
        }
      } else {
        String tz = matcher.group(4);
        if (tz == null) {
          return LocalDateTime.parse(value);
        } else if (tz.equals("Z")) {
          return Instant.parse(value);
        } else if (tz.startsWith("+")) {
          if (tz.length() == 6) {
            return OffsetDateTime.parse(value);
          } else {
            return ZonedDateTime.parse(value);
          }
        } else {
          // ...
        }
      }
    }
    return null;
  }

  /// @return the singleton instance of this [FromStringParser] for [Temporal].
  public static TemporalParser get() {

    return INSTANCE;
  }

}
