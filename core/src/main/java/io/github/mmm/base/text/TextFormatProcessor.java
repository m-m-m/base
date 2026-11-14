/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.text;

import java.util.List;

/// Interface for a reader or writer of data in a textual format. It has a [TextPosition] and can collect
/// [messages][#getMessages()].
public interface TextFormatProcessor extends TextPosition {

  /// Adds an [info][TextFormatMessageType#INFO] [TextFormatMessage] for the current [TextPosition].
  ///
  /// @param text the [text of the message][TextFormatMessage#getText()].
  default void addInfo(String text) {

    addMessage(TextFormatMessageType.INFO, text);
  }

  /// Adds a [warning][TextFormatMessageType#WARNING] [TextFormatMessage] for the current
  /// [TextPosition].
  ///
  /// @param text the [text of the message][TextFormatMessage#getText()].
  default void addWarning(String text) {

    addMessage(TextFormatMessageType.WARNING, text);
  }

  /// Adds a [error][TextFormatMessageType#ERROR] [TextFormatMessage] for the current [TextPosition].
  ///
  /// @param text the [text of the message][TextFormatMessage#getText()].
  default void addError(String text) {

    addMessage(TextFormatMessageType.ERROR, text);
  }

  /// Adds a [TextFormatMessage] for the current [TextPosition].
  ///
  /// @param type the [type of the message][TextFormatMessage#getType()].
  /// @param text the [text of the message][TextFormatMessage#getText()].
  default void addMessage(TextFormatMessageType type, String text) {

    if (type == null) {
      return;
    }
    addMessage(new TextFormatMessage(getLine(), getColumn(), text, type));
  }

  /// Adds a [TextFormatMessage] to the [messages][#getMessages()].
  ///
  /// @param message the [TextFormatMessage] to add.
  default void addMessage(TextFormatMessage message) {

    getMessages().add(message);
  }

  /// @return the [List] of [TextFormatMessage]s.
  List<TextFormatMessage> getMessages();

}
