/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/// Default implementation of [TextFormatMessageHandler].
public abstract class AbstractTextFormatMessageHandler implements TextFormatMessageHandler {

  private final boolean throwOnError;

  private final boolean collectMessages;

  private List<TextFormatMessage> messages;

  /// The constructor.
  ///
  /// @param throwOnError - `true` to throw a [RuntimeException] when an [error][TextFormatMessageType#ERROR] [message][TextFormatMessage] is [handled][#handle(TextFormatMessage)].
  /// @param collectMessages the flag for [#isCollectMessages()].
  public AbstractTextFormatMessageHandler(boolean throwOnError, boolean collectMessages) {

    super();
    this.throwOnError = throwOnError;
    this.collectMessages = collectMessages;
  }

  @Override
  public void add(TextFormatMessage message) {

    message = handle(message);
    if ((message != null) && this.collectMessages) {
      if (this.messages == null) {
        this.messages = new ArrayList<>();
      }
      this.messages.add(message);
    }
  }

  /// Method to allow custom handling or even transformation of [TextFormatMessage]s. Does nothing by default and
  /// may be overridden e.g. to log messages and throw a [RuntimeException] in case of an
  /// [error][TextFormatMessageType#ERROR].
  ///
  /// @param message the [TextFormatMessage] to handle.
  /// @return the given [TextFormatMessage] or potentially a transformation.
  /// @throws RuntimeException in case the [TextFormatMessage] should immediately lead to an exception aborting any
  ///         further processing. By default [TextFormatMessage]s should be collected in
  ///         [TextFormatProcessor#getMessages()] in order to allow maximum fault-tolerance.
  public TextFormatMessage handle(TextFormatMessage message) {

    if (this.throwOnError && (message.getType() == TextFormatMessageType.ERROR)) {
      throw new IllegalStateException(message.getText());
    }
    return message;
  }

  /// @return `true` to collect [messages][TextFormatProcessor#getMessages()] (default) or `null` if no
  ///         [TextFormatMessage] should be collected and [TextFormatProcessor#getMessages()] will always
  ///         return the [empty list][java.util.Collections#emptyList()].
  public boolean isCollectMessages() {

    return this.collectMessages;
  }

  @Override
  public List<TextFormatMessage> getMessages() {

    if (this.messages == null) {
      return Collections.emptyList();
    }
    return this.messages;
  }

}
