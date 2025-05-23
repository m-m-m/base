/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.placement;

/**
 * This enum represents a horizontal alignment. <br>
 * The following table gives an example to illustrate the expected behavior of according to an
 * {@link HorizontalAlignment}.
 * <table border="1">
 * <tr>
 * <th>Alignment</th>
 * <th>Text</th>
 * </tr>
 * <tr>
 * <td>{@link #LEFT}</td>
 * <td><code>Hello&nbsp;&nbsp;&nbsp;&nbsp;</code></td>
 * </tr>
 * <tr>
 * <td>{@link #CENTER}</td>
 * <td><code>&nbsp;&nbsp;Hello&nbsp;&nbsp;</code></td>
 * </tr>
 * <tr>
 * <td>{@link #RIGHT}</td>
 * <td><code>&nbsp;&nbsp;&nbsp;&nbsp;Hello</code></td>
 * </tr>
 * </table>
 */
public enum HorizontalAlignment {

  /** Align content to the left side. */
  LEFT("w", "left"),

  /** Align content to the right side. */
  RIGHT("e", "right"),

  /**
   * Align content centered to the middle (same space to the left and the right).
   */
  CENTER("c", "center");

  private final String value;

  private final String title;

  /**
   * The constructor.
   *
   * @param value is the {@link #getValue() raw value} (symbol).
   * @param title is the {@link #toString() string representation}.
   */
  private HorizontalAlignment(String value, String title) {

    this.value = value;
    this.title = title;
  }

  /**
   * @return the alignment symbol.
   */
  public String getValue() {

    return this.value;
  }

  /**
   * This method gets the corresponding {@link Alignment}.
   *
   * @return the corresponding {@link Alignment}.
   */
  public Alignment toAlignment() {

    switch (this) {
      case LEFT:
        return Alignment.LEFT;
      case CENTER:
        return Alignment.CENTER;
      case RIGHT:
        return Alignment.RIGHT;
      default:
        throw new IllegalStateException(name());
    }
  }

  @Override
  public String toString() {

    return this.title;
  }

  /**
   * This method gets the {@link HorizontalAlignment} with the given {@link #getValue() value}.
   *
   * @param value is the {@link #getValue() value} of the requested {@link HorizontalAlignment}.
   * @return the requested {@link HorizontalAlignment}.
   */
  public static HorizontalAlignment fromValue(String value) {

    for (HorizontalAlignment alignment : values()) {
      if (alignment.value.equals(value)) {
        return alignment;
      }
    }
    return null;
  }

}
