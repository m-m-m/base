/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.exception;

import java.util.Map;

/**
 * An {@link DuplicateObjectException} is thrown if an object was rejected because it is a duplicate. This typically
 * happens if objects are registered (e.g. in a {@link java.util.Map}) and an object is already associated for the same
 * key.
 */
public class DuplicateObjectException extends ApplicationException {

  private static final long serialVersionUID = 1L;

  /** @see #getCode() */
  public static final String MESSAGE_CODE = "DuplicateObject";

  /**
   * The constructor for de-serialization in GWT.
   */
  protected DuplicateObjectException() {

    super();
  }

  /**
   * The constructor.
   *
   * @param object is the object that was rejected because it is a duplicate.
   */
  public DuplicateObjectException(Object object) {

    super("Duplicate object: " + object);
  }

  /**
   * The constructor.
   *
   * @param object is the object that was rejected because it is a duplicate.
   * @param key is the key the object could NOT be associated with because it already leads to another object.
   */
  public DuplicateObjectException(Object object, Object key) {

    super("Duplicate object for key '" + key + "': " + object);
  }

  /**
   * The constructor.
   *
   * @param object is the object that was rejected because it is a duplicate.
   * @param key is the key the object could NOT be associated with because it already leads to another object.
   * @param existing is the object that is already registered for the given {@code key}.
   * @since 3.0.0
   */
  public DuplicateObjectException(Object object, Object key, Object existing) {

    super("Duplicate object for key '" + key + "': " + object + " - already mapped to: " + existing);
  }

  /**
   * This method {@link Map#put(Object, Object) puts} the given {@code value} into the given {@code map} using the given
   * {@code key}.
   *
   * @param <KEY> is the generic type of the {@code key}.
   * @param <VALUE> is the generic type of the {@code value}.
   * @param map is the {@link Map}.
   * @param key is the {@link Map#get(Object) key}.
   * @param value is the value to {@link Map#put(Object, Object) put}.
   * @throws DuplicateObjectException if the given {@code map} already contains a value for the given {@code key} that
   *         is NOT equal to the given {@code value}.
   */
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
