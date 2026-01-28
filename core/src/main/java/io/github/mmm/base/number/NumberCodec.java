/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.number;

import java.util.Objects;

/**
 * Helper class to compose or decompose numbers from/to bytes.
 */
public final class NumberCodec {

  private static final int BYTE_MASK = 0xff;

  private static final long BYTE_MASK_LONG = BYTE_MASK;

  private static final byte ZERO = 0;

  private NumberCodec() {

  }

  /**
   * @param b1 the first byte.
   * @param b2 the second byte.
   * @return the u2 number composed from the given bytes.
   */
  public static int u2(byte b1, byte b2) {

    return ((b1 & BYTE_MASK) << 8) | (b2 & BYTE_MASK);
  }

  /**
   * @param bytes an array of bytes.
   * @param offset the offset where to start reading from the given array.
   * @return the u2 number composed from the given bytes.
   */
  public static int u2(byte[] bytes, int offset) {

    Objects.requireNonNull(bytes);
    int len = bytes.length - offset;
    if ((offset < 0) || (len <= 0)) {
      throw new IllegalArgumentException("" + offset);
    }
    if (len == 1) {
      return bytes[offset] & BYTE_MASK;
    }
    return u2(bytes[offset], bytes[offset + 1]);
  }

  /**
   * @param b1 the first byte.
   * @param b2 the second byte.
   * @param b3 the third byte.
   * @param b4 the fourth byte.
   * @return the u4 number composed from the given bytes.
   */
  public static int u4(byte b1, byte b2, byte b3, byte b4) {

    return ((b1) << 24) | ((b2 & BYTE_MASK) << 16) | ((b3 & BYTE_MASK) << 8) | (b4 & BYTE_MASK);
  }

  /**
   * @param bytes an array of bytes.
   * @param offset the offset where to start reading from the given array.
   * @return the u4 number composed from the given bytes.
   */
  public static int u4(byte[] bytes, int offset) {

    Objects.requireNonNull(bytes);
    int len = bytes.length - offset;
    if ((offset < 0) || (len <= 0)) {
      throw new IllegalArgumentException("" + offset);
    }
    if (len <= 2) {
      return u2(bytes, offset);
    } else if (len == 3) {
      return u4(ZERO, bytes[offset], bytes[offset + 1], bytes[offset + 2]);
    }
    return u4(bytes[offset], bytes[offset + 1], bytes[offset + 2], bytes[offset + 3]);
  }

  /**
   * @param b1 the first byte.
   * @param b2 the second byte.
   * @param b3 the third byte.
   * @param b4 the fourth byte.
   * @param b5 the fifth byte.
   * @param b6 the sixth byte.
   * @param b7 the seventh byte.
   * @param b8 the eighth byte.
   * @return the u8 number composed from the given bytes.
   */
  public static long u8(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {

    return ((b1 & BYTE_MASK_LONG) << 56) | ((b2 & BYTE_MASK_LONG) << 48) | ((b3 & BYTE_MASK_LONG) << 40)
        | ((b4 & BYTE_MASK_LONG) << 32) | ((b5 & BYTE_MASK_LONG) << 24) | ((b6 & BYTE_MASK_LONG) << 16)
        | ((b7 & BYTE_MASK_LONG) << 8) | (b8 & BYTE_MASK_LONG);
  }

  /**
   * @param bytes an array of bytes.
   * @param offset the offset where to start reading from the given array.
   * @return the u8 number composed from the given bytes.
   */
  public static long u8(byte[] bytes, int offset) {

    Objects.requireNonNull(bytes);
    int len = bytes.length - offset;
    if ((offset < 0) || (len <= 0)) {
      throw new IllegalArgumentException("" + offset);
    }
    if (len <= 2) {
      return u2(bytes, offset);
    } else if (len <= 4) {
      return u4(bytes, offset);
    } else if (len == 5) {
      return u8(ZERO, ZERO, ZERO, bytes[offset], bytes[offset + 1], bytes[offset + 2], bytes[offset + 3],
          bytes[offset + 4]);
    } else if (len == 6) {
      return u8(ZERO, ZERO, bytes[offset], bytes[offset + 1], bytes[offset + 2], bytes[offset + 3], bytes[offset + 4],
          bytes[offset + 5]);
    } else if (len == 7) {
      return u8(ZERO, bytes[offset], bytes[offset + 1], bytes[offset + 2], bytes[offset + 3], bytes[offset + 4],
          bytes[offset + 5], bytes[offset + 6]);
    }
    return u8(bytes[offset], bytes[offset + 1], bytes[offset + 2], bytes[offset + 3], bytes[offset + 4],
        bytes[offset + 5], bytes[offset + 6], bytes[offset + 7]);
  }

}
