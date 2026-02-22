/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.text;

import java.util.List;

/**
 * Interface for the mode to configure {@link TextFormatProcessor} for handling {@link TextFormatMessage}s.
 */
public interface TextFormatMessageHandler {

  /**
   * @param message the {@link TextFormatMessage} to add.
   */
  void add(TextFormatMessage message);

  /**
   * @return the {@link List} of {@link TextFormatMessage}s.
   */
  List<TextFormatMessage> getMessages();

}
