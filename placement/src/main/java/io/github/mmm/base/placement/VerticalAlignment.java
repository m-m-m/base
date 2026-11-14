/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.placement;

/// This enum represents a vertical alignment of some object.
///
/// @see HorizontalAlignment
public enum VerticalAlignment {

  /// Align content to the top.
  TOP("n", "top"),

  /// Align content to the bottom.
  BOTTOM("s", "bottom"),

  /// Align content centered to the middle (same space at top and bottom).
  CENTER("c", "center");

  private final String value;

  private final String title;

  /// The constructor.
  ///
  /// @param value is the [raw value][#getValue()] (symbol).
  /// @param title is the [string representation][#toString()].
  private VerticalAlignment(String value, String title) {

    this.value = value;
    this.title = title;
  }

  /// @return the ascii symbol.
  public String getValue() {

    return this.value;
  }

  /// This method gets the corresponding [Alignment].
  ///
  /// @return the corresponding [Alignment].
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

  /// This method gets the [VerticalAlignment] with the given [value][#getValue()].
  ///
  /// @param value is the [value][#getValue()] of the requested [VerticalAlignment].
  /// @return the requested [VerticalAlignment].
  public static VerticalAlignment fromValue(String value) {

    for (VerticalAlignment alignment : values()) {
      if (alignment.value.equals(value)) {
        return alignment;
      }
    }
    return null;
  }

}
