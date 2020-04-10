/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.exception;

/**
 * An {@link ObjectMismatchException} is thrown if an object or value does not match an expected result.
 *
 * @since 1.0.0
 */
public class ObjectMismatchException extends ApplicationException {

  private static final long serialVersionUID = 1L;

  /** @see #getCode() */
  public static final String MESSAGE_CODE = "Mismatch";

  /**
   * The constructor.
   *
   * @param object is the object (value) that does not match as expected.
   * @param expected is the expected object (value).
   */
  public ObjectMismatchException(Object object, Object expected) {

    this(object, expected, null);
  }

  /**
   * The constructor.
   *
   * @param object is the object (value) that does not match as expected.
   * @param expected is the expected object (value).
   * @param cause the {@link #getCause() cause}.
   */
  public ObjectMismatchException(Object object, Object expected, Throwable cause) {

    super("Found " + object + " but expected " + expected, cause);
  }

}
