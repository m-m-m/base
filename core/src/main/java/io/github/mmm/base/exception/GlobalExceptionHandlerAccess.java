package io.github.mmm.base.exception;

import java.util.ServiceLoader;

import io.github.mmm.base.config.ServiceHelper;

/**
 * Class giving {@link #get() global access} to the {@link GlobalExceptionHandler} implementation.
 */
public final class GlobalExceptionHandlerAccess {

  private static final GlobalExceptionHandler HANDLER = ServiceHelper
      .singleton(ServiceLoader.load(GlobalExceptionHandler.class), false);

  private GlobalExceptionHandlerAccess() {

  }

  /**
   * @return the {@link GlobalExceptionHandler} instance.
   */
  public static GlobalExceptionHandler get() {

    return HANDLER;
  }

}
