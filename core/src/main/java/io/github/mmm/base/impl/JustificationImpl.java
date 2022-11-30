/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.impl;

import java.io.IOException;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.base.justification.Justification;

/**
 * Implementation of a {@link Justification}.
 */
public class JustificationImpl implements Justification {

  /** The character used to identify the truncate mode. */
  private static final char MODE_TRUNCATE = '|';

  /** The character used to identify the default mode. */
  private static final char MODE_DEFAULT = '\0';

  /** The pattern for the format. */
  private static final String FORMAT_PATTERN = ".[-+~][0-9]+[|]?";

  private static final char ALIGN_LEFT = '-';

  private static final char ALIGN_RIGHT = '+';

  private static final char ALIGN_CENTER = '~';

  /** The alignment. */
  private final char alignment;

  /**
   * The character used to fill up.
   */
  private final char filler;

  /**
   * The width of the justified string.
   */
  private final int width;

  /**
   * The mode of the {@link Justification}.
   */
  private final char mode;

  /**
   * The constructor.
   *
   * @param format is the justification-format.
   */
  public JustificationImpl(String format) {

    super();
    if (format.length() < 2) {
      illegalFormat(format);
    }
    this.filler = format.charAt(0);
    char align = format.charAt(1);
    switch (align) {
      case '+':
      case '-':
      case '~':
        this.alignment = align;
        break;
      default:
        throw illegalFormat(format);
    }
    int endIndex = format.length();
    char modeChar = format.charAt(format.length() - 1);
    if ((modeChar >= '0') && (modeChar <= '9')) {
      this.mode = MODE_DEFAULT;
    } else {
      this.mode = modeChar;
      endIndex--;
      if (this.mode != MODE_TRUNCATE) {
        illegalFormat(format);
      }
    }
    this.width = Integer.parseInt(format.substring(2, endIndex));
  }

  private RuntimeException illegalFormat(String format) {

    throw new IllegalArgumentException("Illegal format '" + format + "' - has to match " + FORMAT_PATTERN);
  }

  @Override
  public String justify(CharSequence value) {

    StringBuilder sb = new StringBuilder();
    justify(value, sb);
    return sb.toString();
  }

  @Override
  public void justify(CharSequence value, Appendable target) {

    try {
      int space = this.width - value.length();
      int leftSpace = 0;
      int rightSpace = 0;
      if (space > 0) {
        switch (this.alignment) {
          case ALIGN_CENTER:
            leftSpace = space / 2;
            rightSpace = space - leftSpace;
            break;
          case ALIGN_LEFT:
            rightSpace = space;
            break;
          case ALIGN_RIGHT:
            leftSpace = space;
            break;
          default:
            throw new IllegalStateException("" + this.alignment);
        }
      }
      for (int i = 0; i < leftSpace; i++) {
        target.append(this.filler);
      }
      if ((space < 0) && (this.mode == MODE_TRUNCATE)) {
        target.append(value.subSequence(0, this.width));
      } else {
        target.append(value);
      }
      for (int i = 0; i < rightSpace; i++) {
        target.append(this.filler);
      }
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder(8);
    sb.append(this.filler);
    sb.append(this.alignment);
    sb.append(this.width);
    return sb.toString();
  }
}
