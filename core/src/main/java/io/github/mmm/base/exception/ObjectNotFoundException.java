/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.exception;

import io.github.mmm.base.i18n.Localizable;

/**
 * An {@link ObjectNotFoundException} is thrown if an object was requested but does not exist or could not be found.
 * <br>
 * This typically happens in situations where required objects are requested by a key (e.g. in a registry-
 * {@link java.util.Map}) but an expected object was not registered or the key is wrong for some reason. <br>
 * If you design your API consider carefully if you should return {@code null} or throw an
 * {@link ObjectNotFoundException}.
 *
 * @since 1.0.0
 */
public class ObjectNotFoundException extends ApplicationException {

  private static final long serialVersionUID = 1L;

  /** @see #getCode() */
  public static final String MESSAGE_CODE = "NotFound";

  /**
   * The constructor.
   *
   * @param object is a description (e.g. the classname) of the object that was required but could not be found.
   */
  public ObjectNotFoundException(Object object) {

    this(object, null, null);
  }

  /**
   * The constructor.
   *
   * @param object is a description (e.g. the classname) of the object that was required but could not be found.
   * @param key is the key to the required object.
   */
  public ObjectNotFoundException(Object object, Object key) {

    this(object, key, null);
  }

  /**
   * The constructor.
   *
   * @param object is a description (e.g. the classname) of the object that was required but could NOT be found.
   * @param key is the key to the required object.
   * @param cause is the {@link #getCause() cause} of this exception.
   */
  public ObjectNotFoundException(Object object, Object key, Throwable cause) {

    super(createMessage(object, key), cause);
  }

  private static String createMessage(Object object, Object key) {

    StringBuilder sb = new StringBuilder("Could not find ");
    sb.append(object);
    if (key != null) {
      sb.append(" for key ");
      sb.append(key);
    }
    sb.append(".");
    return sb.toString();
  }

  /**
   * The constructor.
   *
   * @param message the {@link #getNlsMessage() NLS message}.
   * @param cause is the {@link #getCause() cause} of this exception. May be <code>null</code>.
   */
  protected ObjectNotFoundException(Localizable message, Throwable cause) {

    super(message, cause);
  }

  @Override
  public String getCode() {

    return MESSAGE_CODE;
  }

}
