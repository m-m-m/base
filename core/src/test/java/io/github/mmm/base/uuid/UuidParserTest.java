/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.uuid;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link UuidParser}.
 */
public class UuidParserTest extends Assertions {

  /**
   * Test of {@link UuidParser#parse(String)}.
   */
  @Test
  public void testParseInstant() {

    UuidParser parser = UuidParser.get();
    assertThat(parser.parse(null)).isNull();
    assertThat(parser.parse("not-a-uuid")).isNull();
    String string = "2dd936d5-7dca-4163-a0df-bccda14108b6";
    UUID uuid = UUID.fromString(string);
    assertThat(parser.parse(string)).isEqualTo(uuid);
    assertThat(parser.parse("2DD936D5-7DCA-4163-A0DF-BCCDA14108B6")).isEqualTo(uuid);
    assertThat(parser.parse("{2dd936d5-7dca-4163-a0df-bccda14108b6}")).isEqualTo(uuid);
    uuid = UUID.randomUUID();
    assertThat(parser.parse(uuid.toString())).isEqualTo(uuid);
  }

}
