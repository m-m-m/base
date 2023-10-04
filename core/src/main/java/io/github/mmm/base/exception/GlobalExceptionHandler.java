/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.exception;

/**
 * Interface used as fallback to {@link #handleError(Object, Throwable) handle an error} that can not be handled in a
 * specific way by generic components. It allows to write portable code that can delegate error handling to this
 * interface allowing to exchange its implementation and therefore the handling strategy.
 */
public interface GlobalExceptionHandler {

  /**
   * This method handles an error that occurred in a generic component that can not handle it in a specific way. <br>
   * In a typical server application you may like to log the errors while in a client application you might want to show
   * a popup that displays the error.
   *
   * @param context is an Object with information about the context when the error occurred. Its
   *        {@link Object#toString() string representation} should be human readable and give additional hints to track
   *        down the error. E.g. the source or parameters of an operation where the error occurred. This parameter may
   *        also be {@code null} if no context information is available.
   * @param error is the {@link Throwable error} that has been catched and shall be handled.
   */
  void handleError(Object context, Throwable error);

}