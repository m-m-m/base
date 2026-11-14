package io.github.mmm.base.exception;

/// Implementation of [GlobalExceptionHandler] that simply performs a [Throwable#printStackTrace()].
public class GlobalExceptionHandlerSysout implements GlobalExceptionHandler {

  @Override
  public void handleError(Object context, Throwable error) {

    error.printStackTrace();
  }

}
