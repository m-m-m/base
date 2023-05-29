package io.github.mmm.base.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of {@link TextFormatMessageHandler}.
 */
public abstract class AbstractTextFormatMessageHandler implements TextFormatMessageHandler {

  private final boolean throwOnError;

  private final boolean collectMessages;

  private List<TextFormatMessage> messages;

  /**
   * The constructor.
   *
   * @param throwOnError - {@code true} to throw a {@link RuntimeException} when an {@link TextFormatMessageType#ERROR
   *        error} {@link TextFormatMessage message} is {@link #handle(TextFormatMessage) handled}.
   * @param collectMessages the flag for {@link #isCollectMessages()}.
   */
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

  /**
   * Method to allow custom handling or even transformation of {@link TextFormatMessage}s. Does nothing by default and
   * may be overridden e.g. to log messages and throw a {@link RuntimeException} in case of an
   * {@link TextFormatMessageType#ERROR error}.
   *
   * @param message the {@link TextFormatMessage} to handle.
   * @return the given {@link TextFormatMessage} or potentially a transformation.
   * @throws RuntimeException in case the {@link TextFormatMessage} should immediately lead to an exception aborting any
   *         further processing. By default {@link TextFormatMessage}s should be collected in
   *         {@link TextFormatProcessor#getMessages()} in order to allow maximum fault-tolerance.
   */
  public TextFormatMessage handle(TextFormatMessage message) {

    if (this.throwOnError && (message.getType() == TextFormatMessageType.ERROR)) {
      throw new IllegalStateException(message.getText());
    }
    return message;
  }

  /**
   * @return {@code true} to collect {@link TextFormatProcessor#getMessages() messages} (default) or {@code null} if no
   *         {@link TextFormatMessage} should be collected and {@link TextFormatProcessor#getMessages()} will always
   *         return the {@link java.util.Collections#emptyList() empty list}.
   */
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
