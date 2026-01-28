/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.number;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link NumberCodec}.
 */
class NumberCodecTest extends Assertions {

  private static final byte[] BYTES = new byte[] { (byte) 0xFE, (byte) 0xDC, (byte) 0xBA, (byte) 0x98, (byte) 0x76,
  (byte) 0x54, (byte) 0x32, (byte) 0x10 };

  private static final int[] INVALID_OFFSETS = { BYTES.length, -1 };

  @Test
  void testU2() {

    assertThat(NumberCodec.u2(BYTES[0], BYTES[1])).isEqualTo(0xFEDC);
    assertThat(NumberCodec.u2(BYTES, 0)).isEqualTo(0xFEDC);
    assertThat(NumberCodec.u2(BYTES, 6)).isEqualTo(0x3210);
    assertThat(NumberCodec.u2(BYTES, 7)).isEqualTo(0x10);
    for (int offset : INVALID_OFFSETS) {
      IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> {
        NumberCodec.u2(BYTES, offset);
      });
      assertThat(error).hasMessage("" + offset);
    }
  }

  @Test
  void testU4() {

    assertThat(NumberCodec.u4(BYTES[0], BYTES[1], BYTES[2], BYTES[3])).isEqualTo(0xFEDCBA98);
    assertThat(NumberCodec.u4(BYTES, 0)).isEqualTo(0xFEDCBA98);
    assertThat(NumberCodec.u4(BYTES, 4)).isEqualTo(0x76543210);
    assertThat(NumberCodec.u4(BYTES, 7)).isEqualTo(0x10);
    for (int offset : INVALID_OFFSETS) {
      IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> {
        NumberCodec.u4(BYTES, offset);
      });
      assertThat(error).hasMessage("" + offset);
    }
  }

  @Test
  void testU8() {

    assertThat(NumberCodec.u8(BYTES[0], BYTES[1], BYTES[2], BYTES[3], BYTES[4], BYTES[5], BYTES[6], BYTES[7]))
        .isEqualTo(0xFEDCBA9876543210L);
    assertThat(NumberCodec.u8(BYTES, 0)).isEqualTo(0xFEDCBA9876543210L);
    for (int offset : INVALID_OFFSETS) {
      IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> {
        NumberCodec.u8(BYTES, offset);
      });
      assertThat(error).hasMessage("" + offset);
    }
  }

}
