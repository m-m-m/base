/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.lang;

import java.util.Map;
import java.util.Objects;

import io.github.mmm.base.number.NumberType;

/// Interface for generic support of a [value type][#getType()]. It does not represent a value itself but only its
/// type with ability to [format][#format(Object)] and [parse][#parse(String)] values of the represented type.
///
/// @param <V> the [type type][#getType()].
/// @since 1.0.0
/// @see io.github.mmm.base.number.NumberType
/// @see io.github.mmm.base.temporal.TemporalType
public abstract class ValueType<V> {

  private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAP = Map.of( //
      int.class, Integer.class, //
      long.class, Long.class, //
      boolean.class, Boolean.class, //
      double.class, Double.class, //
      char.class, Character.class, //
      float.class, Float.class, //
      short.class, Short.class, //
      byte.class, Byte.class);

  /// [ValueType] for [String].
  public static final ValueType<String> STRING = new ValueType<>(String.class, null) {
    @Override
    public String parse(String value) {

      return value;
    }
  };

  /// [ValueType] for [Boolean].
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

  /// @see #getType()
  protected final Class<V> type;

  /// @see #getType()
  protected final Class<V> primitiveType;

  /// The constructor.
  ///
  /// @param type the [value type][#getType()].
  public ValueType(Class<V> type) {

    this(type, null);
  }

  /// The constructor.
  ///
  /// @param type the [value type][#getType()].
  /// @param primitiveType the [primitive type][#getPrimitiveType()].
  public ValueType(Class<V> type, Class<V> primitiveType) {

    super();
    Objects.requireNonNull(type);
    this.type = type;
    this.primitiveType = primitiveType;
  }

  /// @return the [Class] reflecting the [Number] represented by this [NumberType].
  public Class<V> getType() {

    return this.type;
  }

  /// @return the primitive type corresponding to the [value type][#getType()] or `null` if not available.
  public Class<V> getPrimitiveType() {

    return this.primitiveType;
  }

  /// @param value the value as [String].
  /// @return the parsed value as its [value type][#getType()].
  public abstract V parse(String value);

  /// @param value the value to format.
  /// @return the [string representation][Object#toString()] of the given `value`.
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

  /// @param type the [Class] reflecting the requested type.
  /// @return the
  public static Class<?> getNonPrimitiveType(Class<?> type) {

    Class<?> result = PRIMITIVE_TYPE_MAP.get(type);
    if (result == null) {
      result = type;
    }
    return result;
  }
}
