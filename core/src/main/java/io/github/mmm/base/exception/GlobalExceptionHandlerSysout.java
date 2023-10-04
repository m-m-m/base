package io.github.mmm.base.exception;

/**
 * Implementation of {@link GlobalExceptionHandler} that simply performs a {@link Throwable#printStackTrace()}.
 */
public class GlobalExceptionHandlerSysout implements GlobalExceptionHandler {

  @Override
  public void handleError(Object context, Throwable error) {

    error.printStackTrace();
  }

}
