/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.text;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link CaseConversion}.
 */
class CaseConversionTest extends Assertions {

  /**
   * Test of {@link CaseConversion#convert(char)}.
   */
  @Test
  void testConvertChar() {

    assertThat(CaseConversion.UPPER_CASE.convert('a')).isEqualTo('A');
    assertThat(CaseConversion.UPPER_CASE.convert('z')).isEqualTo('Z');
    assertThat(CaseConversion.UPPER_CASE.convert('A')).isEqualTo('A');
    assertThat(CaseConversion.UPPER_CASE.convert('Z')).isEqualTo('Z');
    assertThat(CaseConversion.UPPER_CASE.convert('0')).isEqualTo('0');

    assertThat(CaseConversion.LOWER_CASE.convert('a')).isEqualTo('a');
    assertThat(CaseConversion.LOWER_CASE.convert('z')).isEqualTo('z');
    assertThat(CaseConversion.LOWER_CASE.convert('A')).isEqualTo('a');
    assertThat(CaseConversion.LOWER_CASE.convert('Z')).isEqualTo('z');
    assertThat(CaseConversion.LOWER_CASE.convert('0')).isEqualTo('0');

    assertThat(CaseConversion.ORIGINAL_CASE.convert('a')).isEqualTo('a');
    assertThat(CaseConversion.ORIGINAL_CASE.convert('z')).isEqualTo('z');
    assertThat(CaseConversion.ORIGINAL_CASE.convert('A')).isEqualTo('A');
    assertThat(CaseConversion.ORIGINAL_CASE.convert('Z')).isEqualTo('Z');
    assertThat(CaseConversion.ORIGINAL_CASE.convert('0')).isEqualTo('0');
  }

  /**
   * Test of {@link CaseConversion#convert(String)} and {@link CaseConversion#convert(String, java.util.Locale)}.
   */
  @Test
  void testConvertString() {

    assertThat(CaseConversion.LOWER_CASE.convert("HI ÄÖÜßSS")).isEqualTo("hi äöüßss");
    assertThat(CaseConversion.UPPER_CASE.convert("hi äöüßss")).isEqualTo("HI ÄÖÜSSSS");
    assertThat(CaseConversion.ORIGINAL_CASE.convert("Hi ÄöüßsS")).isEqualTo("Hi ÄöüßsS");
    assertThat(
        CaseConversion.LOWER_CASE.convert("HI ÄÖÜßSS", new Locale.Builder().setLanguage("tr").setRegion("TR").build()))
            .isEqualTo("hı äöüßss");
  }

}
