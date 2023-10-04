package io.github.mmm.base.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link GlobalExceptionHandler} using SLF4J {@link Logger}.
 */
public class GlobalExceptionHandlerSlf4j implements GlobalExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandlerSlf4j.class);

  @Override
  public void handleError(Object context, Throwable error) {

    LOG.warn("Unhandeled error in {}", context, error);
  }

}
