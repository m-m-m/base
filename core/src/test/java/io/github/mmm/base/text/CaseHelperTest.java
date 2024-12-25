/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.text;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link CaseHelper}.
 */
public class CaseHelperTest extends Assertions {

  /** Test of {@link CaseHelper#toLowerCase(String)}. */
  @Test
  public void testToLowerCase() {

    assertThat(CaseHelper.toLowerCase("ABC")).isEqualTo("abc");
    // Here is rationale for CaseHelper: "HI".toLowerCase() can "fail" to return "hi" depending on your system locale
    Locale system = Locale.getDefault();
    Locale turkish = new Locale.Builder().setLanguage("tr").build();
    Locale.setDefault(turkish);
    assertThat(CaseHelper.toLowerCase("HI")).isEqualTo("hi").isNotEqualTo("HI".toLowerCase());
    Locale.setDefault(system);
  }

  /** Test of {@link CaseHelper#toUpperCase(String)}. */
  @Test
  public void testToUpperCase() {

    assertThat(CaseHelper.toUpperCase("abc")).isEqualTo("ABC");
    assertThat(CaseHelper.toUpperCase("Füße")).isEqualTo("FÜSSE");
  }

}
