package io.github.mmm.base.text;

import java.util.List;

/**
 * Interface for a reader or writer of data in a textual format. It has a {@link TextPosition} and can collect
 * {@link #getMessages() messages}.
 */
public interface TextFormatProcessor extends TextPosition {

  /**
   * Adds an {@link TextFormatMessageType#INFO info} {@link TextFormatMessage} for the current {@link TextPosition}.
   *
   * @param text the {@link TextFormatMessage#getText() text of the message}.
   */
  default void addInfo(String text) {

    addMessage(TextFormatMessageType.INFO, text);
  }

  /**
   * Adds a {@link TextFormatMessageType#WARNING warning} {@link TextFormatMessage} for the current
   * {@link TextPosition}.
   *
   * @param text the {@link TextFormatMessage#getText() text of the message}.
   */
  default void addWarning(String text) {

    addMessage(TextFormatMessageType.WARNING, text);
  }

  /**
   * Adds a {@link TextFormatMessageType#ERROR error} {@link TextFormatMessage} for the current {@link TextPosition}.
   *
   * @param text the {@link TextFormatMessage#getText() text of the message}.
   */
  default void addError(String text) {

    addMessage(TextFormatMessageType.ERROR, text);
  }

  /**
   * Adds a {@link TextFormatMessage} for the current {@link TextPosition}.
   *
   * @param type the {@link TextFormatMessage#getType() type of the message}.
   * @param text the {@link TextFormatMessage#getText() text of the message}.
   */
  default void addMessage(TextFormatMessageType type, String text) {

    if (type == null) {
      return;
    }
    addMessage(new TextFormatMessage(getLine(), getColumn(), text, type));
  }

  /**
   * Adds a {@link TextFormatMessage} to the {@link #getMessages() messages}.
   *
   * @param message the {@link TextFormatMessage} to add.
   */
  default void addMessage(TextFormatMessage message) {

    getMessages().add(message);
  }

  /**
   * @return the {@link List} of {@link TextFormatMessage}s.
   */
  List<TextFormatMessage> getMessages();

}
