/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.number;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link LongParser}.
 */
public class LongParserTest extends Assertions {

  /**
   * Test of {@link LongParser#parse(String)}.
   */
  @Test
  public void testParseInstant() {

    LongParser parser = LongParser.get();
    assertThat(parser.parse(null)).isNull();
    assertThat(parser.parse("not-a-long")).isNull();
    assertThat(parser.parse("9223372036854775808")).isNull();
    assertThat(parser.parse("12345678901234567890")).isNull();
    assertThat(parser.parse("-12345678901234567890")).isNull();
    assertThat(parser.parse("0")).isEqualTo(0);
    assertThat(parser.parse("1")).isEqualTo(1);
    assertThat(parser.parse("-1")).isEqualTo(-1);
    assertThat(parser.parse("9223372036854775807")).isEqualTo(Long.MAX_VALUE);
    assertThat(parser.parse("+9223372036854775807")).isEqualTo(Long.MAX_VALUE);
    assertThat(parser.parse("-9223372036854775808")).isEqualTo(Long.MIN_VALUE);
  }

}
