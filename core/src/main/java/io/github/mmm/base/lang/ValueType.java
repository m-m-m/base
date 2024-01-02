/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.lang;

import java.util.Objects;

import io.github.mmm.base.number.NumberType;

/**
 * Interface for generic support of a {@link #getType() value type}. It does not represent a value itself but only its
 * type with ability to {@link #format(Object) format} and {@link #parse(String) parse} values of the represented type.
 *
 * @param <V> the {@link #getType() type type}.
 * @since 1.0.0
 * @see io.github.mmm.base.number.NumberType
 * @see io.github.mmm.base.temporal.TemporalType
 */
public abstract class ValueType<V> {

  /** {@link ValueType} for {@link String}. */
  public static final ValueType<String> STRING = new ValueType<>(String.class, null) {
    @Override
    public String parse(String value) {

      return value;
    }
  };

  /** {@link ValueType} for {@link String}. */
  public static final ValueType<Boolean> BOOLEAN = new ValueType<>(Boolean.class, boolean.class) {
    @Override
    public Boolean parse(String value) {

      if (value == null) {
        return null;
      }
      if ("true".equals(value)) {
        return Boolean.TRUE;
      } else if ("false".equals(value)) {
        return Boolean.FALSE;
      }
      throw new IllegalArgumentException(value);
    }
  };

  /** @see #getType() */
  protected final Class<V> type;

  /** @see #getType() */
  protected final Class<V> primitiveType;

  /**
   * The constructor.
   *
   * @param type the {@link #getType() value type}.
   */
  public ValueType(Class<V> type) {

    this(type, null);
  }

  /**
   * The constructor.
   *
   * @param type the {@link #getType() value type}.
   * @param primitiveType the {@link #getPrimitiveType() primitive type}.
   */
  public ValueType(Class<V> type, Class<V> primitiveType) {

    super();
    Objects.requireNonNull(type);
    this.type = type;
    this.primitiveType = primitiveType;
  }

  /**
   * @return the {@link Class} reflecting the {@link Number} represented by this {@link NumberType}.
   */
  public Class<V> getType() {

    return this.type;
  }

  /**
   * @return the primitive type corresponding to the {@link #getType() value type} or {@code null} if not available.
   */
  public Class<V> getPrimitiveType() {

    return this.primitiveType;
  }

  /**
   * @param value the value as {@link String}.
   * @return the parsed value as its {@link #getType() value type}.
   */
  public abstract V parse(String value);

  /**
   * @param value the value to format.
   * @return the {@link Object#toString() string representation} of the given {@code value}.
   */
  public String format(V value) {

    if (value == null) {
      return null;
    }
    return value.toString();
  }

  @Override
  public String toString() {

    String name = this.type.getName();
    if (name.startsWith("java.")) {
      return this.type.getSimpleName();
    }
    return name;
  }
}
