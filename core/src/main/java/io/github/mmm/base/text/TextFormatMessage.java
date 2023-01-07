package io.github.mmm.base.text;

import java.util.Objects;

/**
 * A message that occurred whilst reading (parsing) or writing (formatting) textual data.
 */
public class TextFormatMessage implements TextPosition {

  private final int line;

  private final int column;

  private final TextFormatMessageType type;

  private final String text;

  /**
   * The constructor.
   *
   * @param line the {@link #getLine() line}.
   * @param column the {@link #getColumn() column}.
   * @param type the {@link #getType() type}.
   * @param text the {@link #getText() text}.
   */
  public TextFormatMessage(int line, int column, String text, TextFormatMessageType type) {

    super();
    Objects.requireNonNull(type);
    Objects.requireNonNull(text);
    this.line = line;
    this.column = column;
    this.text = text;
    this.type = type;
  }

  @Override
  public int getLine() {

    return this.line;
  }

  @Override
  public int getColumn() {

    return this.column;
  }

  /**
   * @return the {@link TextFormatMessageType}.
   */
  public TextFormatMessageType getType() {

    return this.type;
  }

  /**
   * @return the message text.
   */
  public String getText() {

    return this.text;
  }

  @Override
  public String toString() {

    return this.type + "@" + this.line + "," + this.column + ":" + this.text;
  }
}