/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.exception;

/**
 * A {@link RuntimeIoException} is like an {@link java.io.IOException} but as a {@link RuntimeException unchecked}
 * {@link ApplicationException}.
 */
public class RuntimeIoException extends ApplicationException {

  private static final long serialVersionUID = 1L;

  /** @see #getCode() */
  public static final String MESSAGE_CODE = "IO";

  /**
   * The constructor.
   *
   * @param cause is the {@link #getCause() cause} of this exception. This should be an {@link java.io.IOException}.
   *        However it may also be an {@link java.io.IOError}.
   */
  public RuntimeIoException(Throwable cause) {

    super(cause.getMessage(), cause);
  }

  @Override
  public String getCode() {

    return MESSAGE_CODE;
  }

}
