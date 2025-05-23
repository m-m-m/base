/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.placement;

/**
 * This enum represents a vertical alignment of some object. <br>
 *
 * @see HorizontalAlignment
 */
public enum VerticalAlignment {

  /** Align content to the top. */
  TOP("n", "top"),

  /** Align content to the bottom. */
  BOTTOM("s", "bottom"),

  /**
   * Align content centered to the middle (same space at top and bottom).
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
  private VerticalAlignment(String value, String title) {

    this.value = value;
    this.title = title;
  }

  /**
   * @return the ascii symbol.
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
      case TOP:
        return Alignment.TOP;
      case CENTER:
        return Alignment.CENTER;
      case BOTTOM:
        return Alignment.BOTTOM;
      default:
        throw new IllegalStateException(name());
    }
  }

  @Override
  public String toString() {

    return this.title;
  }

  /**
   * This method gets the {@link VerticalAlignment} with the given {@link #getValue() value}.
   *
   * @param value is the {@link #getValue() value} of the requested {@link VerticalAlignment}.
   * @return the requested {@link VerticalAlignment}.
   */
  public static VerticalAlignment fromValue(String value) {

    for (VerticalAlignment alignment : values()) {
      if (alignment.value.equals(value)) {
        return alignment;
      }
    }
    return null;
  }

}
