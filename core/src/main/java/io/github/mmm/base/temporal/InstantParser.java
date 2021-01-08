/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.temporal;

import java.time.Instant;

import io.github.mmm.base.lang.FromStringParser;

/**
 * {@link FromStringParser} for {@link Instant} values.
 *
 * @see #parse(String)
 * @since 1.0.0
 */
public final class InstantParser implements FromStringParser<Instant> {

  private static final InstantParser INSTANCE = new InstantParser();

  private InstantParser() {

  }

  @Override
  public Instant parse(String instant) {

    if (instant == null) {
      return null;
    }
    // example string representations accepted by Instant.parse:
    // 1999-12-31T23:59:59Z
    // 2000-01-01T00:00:00.000001Z
    // 2000-01-01T00:00:00.000000001Z
    int length = instant.length();
    if ((length < 20) || (length > 30)) {
      return null;
    }
    int i = 0;
    int last = length - 1;
    while (i < length) {
      char c = instant.charAt(i);
      char expected = 0;
      if ((i == 4) || (i == 7)) {
        expected = '-';
      } else if (i == 10) {
        expected = 'T';
      } else if ((i == 13) || (i == 16)) {
        expected = ':';
      } else if (i == last) {
        expected = 'Z';
      } else if (i == 19) {
        expected = '.';
      }
      if (expected == 0) {
        if ((c < '0') || (c > '9')) {
          return null;
        }
      } else if (expected != c) {
        return null;
      }
      i++;
    }
    return Instant.parse(instant);
  }

  /**
   * @return the singleton instance of this {@link FromStringParser} for {@link Instant}.
   */
  public static InstantParser get() {

    return INSTANCE;
  }

}
