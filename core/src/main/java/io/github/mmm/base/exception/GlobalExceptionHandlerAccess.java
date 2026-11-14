package io.github.mmm.base.exception;

import java.util.ServiceLoader;

import io.github.mmm.base.service.ServiceHelper;

/// Class giving [global access][#get()] to the [GlobalExceptionHandler] implementation.
public final class GlobalExceptionHandlerAccess {

  private static final GlobalExceptionHandler HANDLER = ServiceHelper
      .singleton(ServiceLoader.load(GlobalExceptionHandler.class), false);

  private GlobalExceptionHandlerAccess() {

  }

  /// @return the [GlobalExceptionHandler] instance.
  public static GlobalExceptionHandler get() {

    return HANDLER;
  }

}
