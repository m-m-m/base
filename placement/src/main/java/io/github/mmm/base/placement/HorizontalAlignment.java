/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.placement;

/// This enum represents a horizontal alignment.
/// The following table gives an example to illustrate the expected behavior of according to an
/// [HorizontalAlignment].
/// <table border="1">
/// <tr>
/// <th>Alignment</th>
/// <th>Text</th>
/// </tr>
/// <tr>
/// <td>[#LEFT]</td>
/// <td>`Hello    `</td>
/// </tr>
/// <tr>
/// <td>[#CENTER]</td>
/// <td>`  Hello  `</td>
/// </tr>
/// <tr>
/// <td>[#RIGHT]</td>
/// <td>`    Hello`</td>
/// </tr>
/// </table>
public enum HorizontalAlignment {

  /// Align content to the left side.
  LEFT("w", "left"),

  /// Align content to the right side.
  RIGHT("e", "right"),

  /// Align content centered to the middle (same space to the left and the right).
  CENTER("c", "center");

  private final String value;

  private final String title;

  /// The constructor.
  ///
  /// @param value is the [raw value][#getValue()] (symbol).
  /// @param title is the [string representation][#toString()].
  private HorizontalAlignment(String value, String title) {

    this.value = value;
    this.title = title;
  }

  /// @return the alignment symbol.
  public String getValue() {

    return this.value;
  }

  /// This method gets the corresponding [Alignment].
  ///
  /// @return the corresponding [Alignment].
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

  /// This method gets the [HorizontalAlignment] with the given [value][#getValue()].
  ///
  /// @param value is the [value][#getValue()] of the requested [HorizontalAlignment].
  /// @return the requested [HorizontalAlignment].
  public static HorizontalAlignment fromValue(String value) {

    for (HorizontalAlignment alignment : values()) {
      if (alignment.value.equals(value)) {
        return alignment;
      }
    }
    return null;
  }

}
