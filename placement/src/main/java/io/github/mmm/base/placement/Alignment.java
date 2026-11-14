/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.placement;

/// This enum contains the available values for the alignment of an object.
///
/// @see HorizontalAlignment
/// @see VerticalAlignment
public enum Alignment {

  /// the component will be horizontally and vertically centered.
  /// `
  /// ...
  /// .**X**.
  /// ...
  /// `
  CENTER("c", "center") {

    @Override
    public Alignment getMirrored() {

      return CENTER;
    }
  },

  /// the component will be located at the top and horizontally centered.
  /// `
  /// .**X**.
  /// ...
  /// ...
  /// `
  TOP("n", "top") {

    @Override
    public Alignment getMirrored() {

      return BOTTOM;
    }
  },

  /// the component will be located at the bottom and horizontally centered.
  /// `
  /// ...
  /// ...
  /// .**X**.
  /// `
  BOTTOM("s", "bottom") {

    @Override
    public Alignment getMirrored() {

      return TOP;
    }

  },

  /// the component will be located at the left and vertically centered.
  /// `
  /// ...
  /// **X**..
  /// ...
  /// `
  LEFT("w", "left") {

    @Override
    public Alignment getMirrored() {

      return RIGHT;
    }

  },

  /// the component will be located at the right and vertically centered.
  /// `
  /// ...
  /// ..**X**
  /// ...
  /// `
  RIGHT("e", "right") {

    @Override
    public Alignment getMirrored() {

      return LEFT;
    }

  },

  /// the component will be located at the right and vertically centered.
  /// `
  /// **X**..
  /// ...
  /// ...
  /// `
  TOP_LEFT("nw", "top left") {

    @Override
    public Alignment getMirrored() {

      return BOTTOM_RIGHT;
    }

  },

  /// the component will be located at the right and vertically centered.
  /// `
  /// ..**X**
  /// ...
  /// ...
  /// `
  TOP_RIGHT("ne", "top right") {

    @Override
    public Alignment getMirrored() {

      return BOTTOM_LEFT;
    }

  },

  /// the component will be located at the right and vertically centered.
  /// `
  /// ...
  /// ...
  /// **X**..
  /// `
  BOTTOM_LEFT("sw", "bottom left") {

    @Override
    public Alignment getMirrored() {

      return TOP_RIGHT;
    }

  },

  /// the component will be located at the right and vertically centered.
  /// `
  /// ...
  /// ...
  /// ..**X**
  /// `
  BOTTOM_RIGHT("se", "bottom right") {

    @Override
    public Alignment getMirrored() {

      return TOP_LEFT;
    }

  };

  private final String syntax;

  private final String title;

  /// The constructor.
  ///
  /// @param syntax is the [syntax][#getSyntax()].
  /// @param title is the [string representation][#toString()].
  private Alignment(String syntax, String title) {

    this.syntax = syntax;
    this.title = title;
  }

  /// @return the compact ASCII syntax.
  public String getSyntax() {

    return this.syntax;
  }

  @Override
  public String toString() {

    return this.title;
  }

  /// This method gets the [Alignment] with the given [value][#getSyntax()].
  ///
  /// @param value is the [value][#getSyntax()] of the requested [Alignment].
  /// @return the requested [Alignment].
  public static Alignment fromValue(String value) {

    for (Alignment alignment : values()) {
      if (alignment.syntax.equals(value)) {
        return alignment;
      }
    }
    return null;
  }

  /// This method gets the horizontal part of the alignment.
  ///
  /// @return the [HorizontalAlignment].
  public HorizontalAlignment toHorizontalAlignment() {

    if ((this == TOP_LEFT) || (this == LEFT) || (this == BOTTOM_LEFT)) {
      return HorizontalAlignment.LEFT;
    } else if ((this == TOP_RIGHT) || (this == RIGHT) || (this == BOTTOM_RIGHT)) {
      return HorizontalAlignment.RIGHT;
    } else {
      return HorizontalAlignment.CENTER;
    }
  }

  /// This method gets the vertical part of the alignment.
  ///
  /// @return the [VerticalAlignment].
  public VerticalAlignment toVerticalAlignment() {

    if ((this == TOP_LEFT) || (this == TOP) || (this == TOP_RIGHT)) {
      return VerticalAlignment.TOP;
    } else if ((this == BOTTOM_LEFT) || (this == BOTTOM) || (this == BOTTOM_RIGHT)) {
      return VerticalAlignment.BOTTOM;
    } else {
      return VerticalAlignment.CENTER;
    }
  }

  /// This method extracts the [horizontal][Orientation#HORIZONTAL] or [vertical][Orientation#VERTICAL] part of
  /// this alignment.
  ///
  /// @param orientation is the [Orientation] of the requested part.
  /// @return the [Alignment] from [#toHorizontalAlignment()] if orientation is
  ///         [Orientation#HORIZONTAL], or [#toVerticalAlignment()] otherwise.
  public Alignment toAlignment(Orientation orientation) {

    if (orientation == Orientation.HORIZONTAL) {
      return toHorizontalAlignment().toAlignment();
    } else {
      return toVerticalAlignment().toAlignment();
    }
  }

  /// This method gets the inverse alignment.
  ///
  /// @return the inverse alignment. E.g. [#BOTTOM_RIGHT] for [#TOP_LEFT]. Returns itself if [#CENTER].
  public abstract Alignment getMirrored();

  /// @return the corresponding [Direction] or `null` for [#CENTER].
  public Direction toDirection() {

    switch (this) {
      case BOTTOM:
        return Direction.DOWN;
      case BOTTOM_LEFT:
        return Direction.DOWN_LEFT;
      case BOTTOM_RIGHT:
        return Direction.DOWN_RIGHT;
      case CENTER:
        return null;
      case LEFT:
        return Direction.LEFT;
      case RIGHT:
        return Direction.RIGHT;
      case TOP:
        return Direction.UP;
      case TOP_LEFT:
        return Direction.UP_LEFT;
      case TOP_RIGHT:
        return Direction.UP_RIGHT;
      default:
        throw new IllegalStateException(name());
    }
  }

  /// This is the inverse operation for [#toDirection()].
  ///
  /// @param direction is the [Direction]. May be `null` for [#CENTER].
  /// @return the corresponding [Alignment].
  public static Alignment fromDirection(Direction direction) {

    if (direction == null) {
      return CENTER;
    }
    switch (direction) {
      case RIGHT:
        return RIGHT;
      case LEFT:
        return LEFT;
      case UP:
        return BOTTOM;
      case DOWN:
        return TOP;
      case DOWN_RIGHT:
        return BOTTOM_RIGHT;
      case DOWN_LEFT:
        return BOTTOM_LEFT;
      case UP_RIGHT:
        return TOP_RIGHT;
      case UP_LEFT:
        return TOP_LEFT;
      default:
        throw new IllegalStateException(direction.name());
    }
  }

}
