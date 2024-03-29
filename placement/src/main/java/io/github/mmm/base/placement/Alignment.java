/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.placement;

/**
 * This enum contains the available values for the alignment of an object.
 *
 * @see HorizontalAlignment
 * @see VerticalAlignment
 */
public enum Alignment {

  /**
   * the component will be horizontally and vertically centered. <br>
   * <code>
   * ...<br>
   * .<b>X</b>.<br>
   * ...<br>
   * </code>
   */
  CENTER("c", "center") {

    @Override
    public Alignment getMirrored() {

      return CENTER;
    }
  },

  /**
   * the component will be located at the top and horizontally centered. <br>
   * <code>
   * .<b>X</b>.<br>
   * ...<br>
   * ...<br>
   * </code>
   */
  TOP("n", "top") {

    @Override
    public Alignment getMirrored() {

      return BOTTOM;
    }
  },

  /**
   * the component will be located at the bottom and horizontally centered. <br>
   * <code>
   * ...<br>
   * ...<br>
   * .<b>X</b>.<br>
   * </code>
   */
  BOTTOM("s", "bottom") {

    @Override
    public Alignment getMirrored() {

      return TOP;
    }

  },

  /**
   * the component will be located at the left and vertically centered. <br>
   * <code>
   * ...<br>
   * <b>X</b>..<br>
   * ...<br>
   * </code>
   */
  LEFT("w", "left") {

    @Override
    public Alignment getMirrored() {

      return RIGHT;
    }

  },

  /**
   * the component will be located at the right and vertically centered. <br>
   * <code>
   * ...<br>
   * ..<b>X</b><br>
   * ...<br>
   * </code>
   */
  RIGHT("e", "right") {

    @Override
    public Alignment getMirrored() {

      return LEFT;
    }

  },

  /**
   * the component will be located at the right and vertically centered. <br>
   * <code>
   * <b>X</b>..<br>
   * ...<br>
   * ...<br>
   * </code>
   */
  TOP_LEFT("nw", "top left") {

    @Override
    public Alignment getMirrored() {

      return BOTTOM_RIGHT;
    }

  },

  /**
   * the component will be located at the right and vertically centered. <br>
   * <code>
   * ..<b>X</b><br>
   * ...<br>
   * ...<br>
   * </code>
   */
  TOP_RIGHT("ne", "top right") {

    @Override
    public Alignment getMirrored() {

      return BOTTOM_LEFT;
    }

  },

  /**
   * the component will be located at the right and vertically centered. <br>
   * <code>
   * ...<br>
   * ...<br>
   * <b>X</b>..<br>
   * </code>
   */
  BOTTOM_LEFT("sw", "bottom left") {

    @Override
    public Alignment getMirrored() {

      return TOP_RIGHT;
    }

  },

  /**
   * the component will be located at the right and vertically centered. <br>
   * <code>
   * ...<br>
   * ...<br>
   * ..<b>X</b><br>
   * </code>
   */
  BOTTOM_RIGHT("se", "bottom right") {

    @Override
    public Alignment getMirrored() {

      return TOP_LEFT;
    }

  };

  private final String syntax;

  private final String title;

  /**
   * The constructor.
   *
   * @param syntax is the {@link #getSyntax() syntax}.
   * @param title is the {@link #toString() string representation}.
   */
  private Alignment(String syntax, String title) {

    this.syntax = syntax;
    this.title = title;
  }

  /**
   * @return the compact ASCII syntax.
   */
  public String getSyntax() {

    return this.syntax;
  }

  @Override
  public String toString() {

    return this.title;
  }

  /**
   * This method gets the {@link Alignment} with the given {@link #getSyntax() value}.
   *
   * @param value is the {@link #getSyntax() value} of the requested {@link Alignment}.
   * @return the requested {@link Alignment}.
   */
  public static Alignment fromValue(String value) {

    for (Alignment alignment : values()) {
      if (alignment.syntax.equals(value)) {
        return alignment;
      }
    }
    return null;
  }

  /**
   * This method gets the horizontal part of the alignment.
   *
   * @return the {@link HorizontalAlignment}.
   */
  public HorizontalAlignment toHorizontalAlignment() {

    if ((this == TOP_LEFT) || (this == LEFT) || (this == BOTTOM_LEFT)) {
      return HorizontalAlignment.LEFT;
    } else if ((this == TOP_RIGHT) || (this == RIGHT) || (this == BOTTOM_RIGHT)) {
      return HorizontalAlignment.RIGHT;
    } else {
      return HorizontalAlignment.CENTER;
    }
  }

  /**
   * This method gets the vertical part of the alignment.
   *
   * @return the {@link VerticalAlignment}.
   */
  public VerticalAlignment toVerticalAlignment() {

    if ((this == TOP_LEFT) || (this == TOP) || (this == TOP_RIGHT)) {
      return VerticalAlignment.TOP;
    } else if ((this == BOTTOM_LEFT) || (this == BOTTOM) || (this == BOTTOM_RIGHT)) {
      return VerticalAlignment.BOTTOM;
    } else {
      return VerticalAlignment.CENTER;
    }
  }

  /**
   * This method extracts the {@link Orientation#HORIZONTAL horizontal} or {@link Orientation#VERTICAL vertical} part of
   * this alignment.
   *
   * @param orientation is the {@link Orientation} of the requested part.
   * @return the {@link Alignment} from {@link #toHorizontalAlignment()} if orientation is
   *         {@link Orientation#HORIZONTAL}, or {@link #toVerticalAlignment()} otherwise.
   */
  public Alignment toAlignment(Orientation orientation) {

    if (orientation == Orientation.HORIZONTAL) {
      return toHorizontalAlignment().toAlignment();
    } else {
      return toVerticalAlignment().toAlignment();
    }
  }

  /**
   * This method gets the inverse alignment.
   *
   * @return the inverse alignment. E.g. {@link #BOTTOM_RIGHT} for {@link #TOP_LEFT}. Returns itself if {@link #CENTER}.
   */
  public abstract Alignment getMirrored();

  /**
   * @return the corresponding {@link Direction} or {@code null} for {@link #CENTER}.
   */
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

  /**
   * This is the inverse operation for {@link #toDirection()}.
   *
   * @param direction is the {@link Direction}. May be {@code null} for {@link #CENTER}.
   * @return the corresponding {@link Alignment}.
   */
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
