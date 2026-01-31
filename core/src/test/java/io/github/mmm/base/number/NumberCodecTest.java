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

  private static final int[] INVALID_OFFSETS = { BYTES.length, -1, (BYTES.length - 1) };

  @Test
  void testReadU2() {

    assertThat(NumberCodec.readU2(BYTES[0], BYTES[1])).isEqualTo(0xFEDC);
    assertThat(NumberCodec.readU2(BYTES, 0, true)).isEqualTo(0xFEDC);
    assertThat(NumberCodec.readU2(BYTES, 6, true)).isEqualTo(0x3210);
    assertThat(NumberCodec.readU2(BYTES, 7, true)).isEqualTo(0x10);
    for (int offset : INVALID_OFFSETS) {
      IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> {
        boolean lenient = true;
        if (offset == BYTES.length - 1) {
          lenient = false;
        }
        NumberCodec.readU2(BYTES, offset, lenient);
      });
      assertThat(error).hasMessage("" + offset);
    }
  }

  @Test
  void testReadU4() {

    assertThat(NumberCodec.readU4(BYTES[0], BYTES[1], BYTES[2], BYTES[3])).isEqualTo(0xFEDCBA98);
    assertThat(NumberCodec.readU4(BYTES, 0, false)).isEqualTo(0xFEDCBA98);
    assertThat(NumberCodec.readU4(BYTES, 4, false)).isEqualTo(0x76543210);
    assertThat(NumberCodec.readU4(BYTES, 7, true)).isEqualTo(0x10);
    for (int offset : INVALID_OFFSETS) {
      IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> {
        boolean lenient = true;
        if (offset == BYTES.length - 1) {
          lenient = false;
        }
        NumberCodec.readU4(BYTES, offset, lenient);
      });
      assertThat(error).hasMessage("" + offset);
    }
  }

  @Test
  void testReadU8() {

    assertThat(NumberCodec.readU8(BYTES[0], BYTES[1], BYTES[2], BYTES[3], BYTES[4], BYTES[5], BYTES[6], BYTES[7]))
        .isEqualTo(0xFEDCBA9876543210L);
    assertThat(NumberCodec.readU8(BYTES, 0, false)).isEqualTo(0xFEDCBA9876543210L);
    for (int offset : INVALID_OFFSETS) {
      IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> {
        boolean lenient = true;
        if (offset == BYTES.length - 1) {
          lenient = false;
        }
        NumberCodec.readU8(BYTES, offset, lenient);
      });
      assertThat(error).hasMessage("" + offset);
    }
  }

}
