package io.github.mmm.base.text;

/**
 * Interface for the mode to configure {@link TextFormatProcessor} for handling {@link TextFormatMessage}s.
 */
public interface TextFormatMessageHandler {

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
  default TextFormatMessage handle(TextFormatMessage message) {

    return message;
  }

}
