/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.justification;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link Justification}.
 */
public class JustificationTest extends Assertions {

  /**
   * Test of {@link Justification#of(String)} and {@link Justification#justify(CharSequence)}.
   */
  @Test
  public void testPadLeadingZeros() {

    Justification justification = Justification.of("0+4");
    assertThat(justification.justify("42")).isEqualTo("0042");
  }

  /**
   * Test of {@link Justification#of(String)} and {@link Justification#justify(CharSequence)}.
   */
  @Test
  public void testPadTrailingDots() {

    Justification justification = Justification.of(".-5");
    assertThat(justification.justify("xx")).isEqualTo("xx...");
    assertThat(justification.justify("1234")).isEqualTo("1234.");
    assertThat(justification.justify("12345")).isEqualTo("12345");
    assertThat(justification.justify("123456")).isEqualTo("123456");
  }

  /**
   * Test of {@link Justification#of(String)} and {@link Justification#justify(CharSequence)}.
   */
  @Test
  public void testCenter() {

    Justification justification = Justification.of("_~7");
    assertThat(justification.justify("x")).isEqualTo("___x___");
    assertThat(justification.justify("xxx")).isEqualTo("__xxx__");
    assertThat(justification.justify("xxxxx")).isEqualTo("_xxxxx_");
    assertThat(justification.justify("xxxxxxx")).isEqualTo("xxxxxxx");
    assertThat(justification.justify("123456789")).isEqualTo("123456789");
  }

  /**
   * Test of {@link Justification#of(String)} and {@link Justification#justify(CharSequence)}.
   */
  @Test
  public void testPadLeftAndTruncate() {

    Justification justification = Justification.of("-+5|");
    assertThat(justification.justify("abc")).isEqualTo("--abc");
    assertThat(justification.justify("abcd")).isEqualTo("-abcd");
    assertThat(justification.justify("abcde")).isEqualTo("abcde");
    assertThat(justification.justify("abcdef")).isEqualTo("abcde");
  }

}
