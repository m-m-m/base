/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.placement;

/// This enum contains all possible directions.
public enum Direction {

  /// Direction down (south/bottom).
  DOWN("s", "down"),

  /// Direction right (east).
  RIGHT("e", "right"),

  /// Direction left (west).
  LEFT("w", "left"),

  /// Direction up (north/top).
  UP("n", "up"),

  /// Direction down-right (south-east).
  DOWN_RIGHT("se", "down-right"),

  /// Direction down-left (south-west).
  DOWN_LEFT("sw", "down-left"),

  /// Direction up-right (north-east).
  UP_RIGHT("ne", "up-right"),

  /// Direction up-left (north-west).
  UP_LEFT("nw", "up-left");

  private final String value;

  private final String title;

  /// The constructor.
  ///
  /// @param value - see [#getValue()].
  /// @param title - see [#toString()].
  private Direction(String value, String title) {

    this.value = value;
    this.title = title;
  }

  /// @return the shortcut representation.
  public String getValue() {

    return this.value;
  }

  @Override
  public String toString() {

    return this.title;
  }

  /// @return `true` if pointing right ([#RIGHT], [#DOWN_RIGHT], or [#UP_RIGHT]), `false`
  ///         otherwise.
  public boolean isRight() {

    return (this == RIGHT) || (this == DOWN_RIGHT) || (this == UP_RIGHT);
  }

  /// @return `true` if pointing left ([#LEFT], [#DOWN_LEFT], or [#UP_LEFT]), `false`
  ///         otherwise.
  public boolean isLeft() {

    return (this == LEFT) || (this == DOWN_LEFT) || (this == UP_LEFT);
  }

  /// @return `true` if pointing down ([#DOWN], [#DOWN_RIGHT], or [#DOWN_LEFT]), `false`
  ///         otherwise.
  public boolean isDown() {

    return (this == DOWN) || (this == DOWN_RIGHT) || (this == DOWN_LEFT);
  }

  /// @return `true` if pointing up ([#UP], [#UP_RIGHT], or [#UP_LEFT]), `false` otherwise.
  public boolean isUp() {

    return (this == UP) || (this == UP_RIGHT) || (this == UP_LEFT);
  }

  /// @return the [Alignment] corresponding to this [Direction].
  public Alignment toAlignment() {

    switch (this) {
      case UP:
        return Alignment.TOP;
      case DOWN:
        return Alignment.BOTTOM;
      case LEFT:
        return Alignment.LEFT;
      case RIGHT:
        return Alignment.RIGHT;
      case UP_LEFT:
        return Alignment.TOP_LEFT;
      case UP_RIGHT:
        return Alignment.TOP_RIGHT;
      case DOWN_LEFT:
        return Alignment.BOTTOM_LEFT;
      case DOWN_RIGHT:
        return Alignment.BOTTOM_RIGHT;
    }
    throw new IllegalStateException(toString());
  }

}
