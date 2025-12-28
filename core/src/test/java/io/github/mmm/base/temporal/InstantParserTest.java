/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.temporal;

import java.time.Instant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link InstantParser}.
 */
class InstantParserTest extends Assertions {

  /**
   * Test of {@link InstantParser#parse(String)}.
   */
  @Test
  void testParseInstant() {

    InstantParser parser = InstantParser.get();
    assertThat(parser.parse(null)).isNull();
    assertThat(parser.parse("not-an-instant")).isNull();
    String string = "1999-12-31T23:59:59Z";
    assertThat(parser.parse(string)).isEqualTo(Instant.parse(string));
    Instant instant = Instant.now();
    assertThat(parser.parse(instant.toString())).isEqualTo(instant);
  }

}
