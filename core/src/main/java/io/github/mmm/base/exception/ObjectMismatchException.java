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
   * @param actual the value that does not match as expected.
   * @param expected is the expected value.
   */
  public ObjectMismatchException(Object actual, Object expected) {

    this(actual, expected, null);
  }

  /**
   * The constructor.
   *
   * @param actual the value that does not match as expected.
   * @param expected is the expected value.
   * @param cause the {@link #getCause() cause}.
   */
  public ObjectMismatchException(Object actual, Object expected, Throwable cause) {

    this(null, actual, expected, cause);
  }

  /**
   * The constructor.
   *
   * @param object is the parent object given the context of the mismatch.
   * @param actual the value that does not match as expected.
   * @param expected the expected value.
   */
  public ObjectMismatchException(Object object, Object actual, Object expected) {

    this(object, actual, expected, null);
  }

  /**
   * The constructor.
   *
   * @param object is the parent object given the context of the mismatch.
   * @param actual the value that does not match as expected.
   * @param expected the expected value.
   * @param cause the {@link #getCause() cause}.
   */
  public ObjectMismatchException(Object object, Object actual, Object expected, Throwable cause) {

    super(message(object, actual, expected), cause);
  }

  private static String message(Object object, Object actual, Object expected) {

    return ((object == null) ? "Found " : "For " + object + " found ") + actual + " but expected " + expected;
  }

}
