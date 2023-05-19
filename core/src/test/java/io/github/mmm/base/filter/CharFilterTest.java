/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link CharFilter}.
 */
public class CharFilterTest extends Assertions {

  /** Test of {@link CharFilter#LATIN_LETTER_OR_DIGIT}. */
  @Test
  public void testDigitOrLetter() {

    CharFilter letterOrDigit = CharFilter.LATIN_LETTER_OR_DIGIT;
    assertThat(letterOrDigit).hasToString("[a-z][A-Z][0-9]").hasToString(letterOrDigit.getDescription());
    assertThat(letterOrDigit.filter("äabcdefghijklmnoöpqrstüuvwxyzAÄBCDEFGHIJKLMNÖOPQRSTUÜVWXY-Z.012345678 9_"))
        .isEqualTo("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
    assertThat(
        letterOrDigit.negate().filter("äabcdefghijklmnoöpqrstüuvwxyzAÄBCDEFGHIJKLMNÖOPQRSTUÜVWXY-Z.012345678 9_"))
            .isEqualTo("äöüÄÖÜ-. _");
  }

}
