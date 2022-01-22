/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.uuid;

import java.util.UUID;

import io.github.mmm.base.filter.CharFilter;
import io.github.mmm.base.lang.FromStringParser;

/**
 * {@link FromStringParser} for {@link UUID}.
 *
 * @see #parse(String)
 * @since 1.0.0
 */
public final class UuidParser implements FromStringParser<UUID> {

  private static final UuidParser INSTANCE = new UuidParser();

  private UuidParser() {

  }

  @Override
  public UUID parse(String uuid) {

    if (uuid == null) {
      return null;
    }
    // UUID example: 2dd936d5-7dca-4163-a0df-bccda14108b6
    int length = uuid.length();
    if (length != 36) {
      if ((length == 38) && (uuid.charAt(0) == '{') && (uuid.charAt(37) == '}')) {
        uuid = uuid.substring(1, 37);
      } else {
        return null;
      }
    }
    int i = 0;
    while (i < 36) {
      char c = uuid.charAt(i);
      char expected = 0;
      if ((i == 8) || (i == 13) || (i == 18) || (i == 23)) {
        expected = '-';
      }
      if (expected == 0) {
        if (!CharFilter.HEX_DIGIT.accept(c)) {
          return null;
        }
      } else if (expected != c) {
        return null;
      }
      i++;
    }
    return UUID.fromString(uuid);
  }

  /**
   * @return the singleton instance of this {@link FromStringParser} for {@link UUID}.
   */
  public static UuidParser get() {

    return INSTANCE;
  }

}
