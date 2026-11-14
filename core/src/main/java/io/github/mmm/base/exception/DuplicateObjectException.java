/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.exception;

import java.util.Map;

/// An [DuplicateObjectException] is thrown if an object was rejected because it is a duplicate. This typically
/// happens if objects are registered (e.g. in a [java.util.Map]) and an object is already associated for the same
/// key.
public class DuplicateObjectException extends ApplicationException {

  private static final long serialVersionUID = 1L;

  /// @see #getCode()
  public static final String MESSAGE_CODE = "DuplicateObject";

  /// The constructor.
  ///
  /// @param object is the object that was rejected because it is a duplicate.
  public DuplicateObjectException(Object object) {

    super("Duplicate object: " + object);
  }

  /// The constructor.
  ///
  /// @param object is the object that was rejected because it is a duplicate.
  /// @param key is the key the object could NOT be associated with because it already leads to another object.
  public DuplicateObjectException(Object object, Object key) {

    super("Duplicate object for key '" + key + "': " + object);
  }

  /// The constructor.
  ///
  /// @param object is the object that was rejected because it is a duplicate.
  /// @param key is the key the object could NOT be associated with because it already leads to another object.
  /// @param existing is the object that is already registered for the given `key`.
  /// @since 3.0.0
  public DuplicateObjectException(Object object, Object key, Object existing) {

    super("Duplicate object for key '" + key + "': " + object + " - already mapped to: " + existing);
  }

  /// This method [puts][Map#put(Object, Object)] the given `value` into the given `map` using the given
  /// `key`.
  ///
  /// @param <KEY> is the generic type of the `key`.
  /// @param <VALUE> is the generic type of the `value`.
  /// @param map is the [Map].
  /// @param key is the [key][Map#get(Object)].
  /// @param value is the value to [put][Map#put(Object, Object)].
  /// @throws DuplicateObjectException if the given `map` already contains a value for the given `key` that
  ///         is NOT equal to the given `value`.
  public static <KEY, VALUE> void put(Map<KEY, VALUE> map, KEY key, VALUE value) throws DuplicateObjectException {

    VALUE old = map.putIfAbsent(key, value);
    if ((old != null) && (!old.equals(value))) {
      throw new DuplicateObjectException(value, key, old);
    }
  }

  @Override
  public String getCode() {

    return MESSAGE_CODE;
  }

}
