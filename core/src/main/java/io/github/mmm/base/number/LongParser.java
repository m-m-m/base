/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.number;

import io.github.mmm.base.filter.CharFilter;
import io.github.mmm.base.lang.FromStringParser;

/**
 * {@link FromStringParser} for {@link Long}.
 *
 * @see #parse(String)
 * @since 1.0.0
 */
public final class LongParser implements FromStringParser<Long> {

  private static final LongParser INSTANCE = new LongParser();

  private LongParser() {

  }

  @Override
  public Long parse(String string) {

    if (string == null) {
      return null;
    }
    // min: -9223372036854775808
    // max: +9223372036854775807
    int length = string.length();
    if ((length < 1) || (length > 20)) {
      return null;
    }
    char c = string.charAt(0);
    if (CharFilter.LATIN_DIGIT.accept(c)) {
      if (length > 19) {
        return null;
      }
    } else if ((c != '+') && (c != '-')) {
      return null;
    }
    int i = 1;
    while (i < length) {
      c = string.charAt(i);
      if (!CharFilter.LATIN_DIGIT.accept(c)) {
        return null;
      }
      i++;
    }
    try {
      return Long.valueOf(string);
    } catch (NumberFormatException e) {
      // overflow/underflow, very unlikely
      return null;
    }
  }

  /**
   * @return the singleton instance of this {@link FromStringParser} for {@link Long}.
   */
  public static LongParser get() {

    return INSTANCE;
  }

}
