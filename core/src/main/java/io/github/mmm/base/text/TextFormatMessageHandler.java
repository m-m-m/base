/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.text;

import java.util.List;

/// Interface for the mode to configure [TextFormatProcessor] for handling [TextFormatMessage]s.
public interface TextFormatMessageHandler {

  /// @param message the [TextFormatMessage] to add.
  void add(TextFormatMessage message);

  /// @return the [List] of [TextFormatMessage]s.
  List<TextFormatMessage> getMessages();

}
