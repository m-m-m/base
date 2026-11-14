/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.exception;

import java.util.Collection;

import io.github.mmm.base.i18n.Localizable;

/// An [ObjectNotFoundException] is thrown if an object was requested but does not exist or could not be found.
///
/// This typically happens in situations where required objects are requested by a key (e.g. in a registry-
/// [java.util.Map]) but an expected object was not registered or the key is wrong for some reason.
/// If you design your API consider carefully if you should return `null` or throw an
/// [ObjectNotFoundException].
///
/// @since 1.0.0
public class ObjectNotFoundException extends ApplicationException {

  private static final long serialVersionUID = 1L;

  /// @see #getCode()
  public static final String MESSAGE_CODE = "NotFound";

  /// The constructor.
  ///
  /// @param object the description (e.g. the classname) of the object that was required but could NOT be found.
  public ObjectNotFoundException(Object object) {

    this(object, null, null);
  }

  /// The constructor.
  ///
  /// @param object the description (e.g. the classname) of the object that was required but could NOT be found.
  /// @param key the key to the required object.
  public ObjectNotFoundException(Object object, Object key) {

    this(object, key, null);
  }

  /// The constructor.
  ///
  /// @param object the description (e.g. the classname) of the object that was required but could NOT be found.
  /// @param key the key to the required object.
  /// @param cause the [cause][#getCause()] of this exception.
  public ObjectNotFoundException(Object object, Object key, Throwable cause) {

    this(object, key, null, cause);
  }

  /// The constructor.
  ///
  /// @param object the description (e.g. the classname) of the object that was required but could NOT be found.
  /// @param key the key to the required object.
  /// @param options the available options (e.g. [Collection] of comma separated [String] of the available
  ///        keys).
  /// @param cause the [cause][#getCause()] of this exception.
  public ObjectNotFoundException(Object object, Object key, Object options, Throwable cause) {

    super(createMessage(object, key, options), cause);
  }

  private static String createMessage(Object object, Object key, Object options) {

    StringBuilder sb = new StringBuilder("Could not find ");
    sb.append(object);
    if (key != null) {
      sb.append(" for key ");
      sb.append('\'');
      sb.append(key);
      sb.append('\'');
    }
    if (options != null) {
      sb.append(" in ");
      sb.append(options);
    }
    sb.append('.');
    return sb.toString();
  }

  /// The constructor.
  ///
  /// @param message the [NLS message][#getNlsMessage()].
  /// @param cause the [cause][#getCause()] of this exception. May be `null`.
  protected ObjectNotFoundException(Localizable message, Throwable cause) {

    super(message, cause);
  }

  @Override
  public String getCode() {

    return MESSAGE_CODE;
  }

}
