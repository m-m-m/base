/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.variable;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Definition of a variable for a {@link VariableMap}. It is a simple container for the {@link #getName() name} of the
 * variable with its {@link #getDefaultValue()} and its {@link #getType() type} making the
 * {@link VariableMap#get(VariableDefinition) access of the value} type-safe.
 *
 * @param <T> type of the value for this option. See {@link #getType()}.
 * @since 1.0.0
 */
public class VariableDefinition<T> {

  private static final Function<Object, String> DEFAULT_FORMATTER = Object::toString;

  private final String name;

  private final Class<T> type;

  private final T defaultValue;

  private final Function<String, T> parser;

  private final Function<T, String> formatter;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param type the {@link #getType() type}.
   * @param parser the {@link Function} to {@link #parse(String) parse} the value from {@link String}.
   */
  public VariableDefinition(String name, Class<T> type, Function<String, T> parser) {

    this(name, type, null, parser);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   * @param parser the {@link Function} to {@link #parse(String) parse} the value from {@link String}.
   */
  public VariableDefinition(String name, T defaultValue, Function<String, T> parser) {

    this(name, null, defaultValue, parser);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param type the {@link #getType() type}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   * @param parser the {@link Function} to {@link #parse(String) parse} the value from {@link String}.
   */
  @SuppressWarnings("unchecked")
  public VariableDefinition(String name, Class<T> type, T defaultValue, Function<String, T> parser) {

    this(name, type, defaultValue, parser, (Function<T, String>) DEFAULT_FORMATTER);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param type the {@link #getType() type}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   * @param parser the {@link Function} to {@link #parse(String) parse} the value from {@link String}.
   * @param formatter the {@link Function} to {@link #format(Object) format} the value to {@link String}.
   */
  @SuppressWarnings("unchecked")
  public VariableDefinition(String name, Class<T> type, T defaultValue, Function<String, T> parser,
      Function<T, String> formatter) {

    super();
    Objects.requireNonNull(name);
    Objects.requireNonNull(parser);
    Objects.requireNonNull(formatter);
    this.name = name;
    if ((type == null) && (defaultValue != null)) {
      this.type = (Class<T>) defaultValue.getClass();
    } else {
      this.type = type;
    }
    Objects.requireNonNull(this.type);
    this.defaultValue = defaultValue;
    this.parser = parser;
    this.formatter = formatter;
  }

  /**
   * @return the name of the variable used as {@link Map#containsKey(Object) key} to
   *         {@link VariableMap#get(VariableDefinition) get its value}.
   */
  public String getName() {

    return this.name;
  }

  /**
   * @return the {@link Class} reflecting the value of this variable.
   */
  public Class<T> getType() {

    return this.type;
  }

  /**
   * @param value the value as {@link String}.
   * @return the parsed value.
   */
  public final T parse(String value) {

    if (value == null) {
      return parseNull();
    }
    return this.parser.apply(value);
  }

  /**
   * @return the result of {@link #parse(String)} if {@code null} is given as argument.
   */
  protected T parseNull() {

    return null;
  }

  /**
   * @param value the value to format.
   * @return the formatted value as {@link String}.
   * @see Object#toString()
   */
  public String format(T value) {

    if (value == null) {
      return formatNull();
    }
    return this.formatter.apply(value);
  }

  /**
   * @return the result of {@link #format(Object)} if {@code null} is given as argument.
   */
  protected String formatNull() {

    return null;
  }

  /**
   * @return the optional default value to use if the variable is not explicitly defined.
   */
  public T getDefaultValue() {

    return this.defaultValue;
  }

  @Override
  public String toString() {

    return this.name;
  }

  /**
   * @param name the variable {@link #getName() name}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   * @return the specified {@link VariableDefinition}.
   */
  public static VariableDefinition<String> ofString(String name, String defaultValue) {

    return new VariableDefinition<>(name, String.class, defaultValue, s -> s);
  }

  /**
   * @param name the variable {@link #getName() name}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   * @return the specified {@link VariableDefinition}.
   */
  public static VariableDefinition<Boolean> ofBoolean(String name, Boolean defaultValue) {

    return new VariableDefinition<>(name, Boolean.class, defaultValue, Boolean::parseBoolean);
  }

  /**
   * @param <E> type of the {@link Enum}.
   * @param name the variable {@link #getName() name}.
   * @param defaultValue the {@link #getDefaultValue() default value}. Must not be {@code null}.
   * @return the specified {@link VariableDefinition}.
   */
  @SuppressWarnings("unchecked")
  public static <E extends Enum<E>> VariableDefinition<E> ofEnum(String name, E defaultValue) {

    Class<E> enumClass = (Class<E>) defaultValue.getClass();
    return new VariableDefinition<>(name, enumClass, defaultValue, s -> Enum.valueOf(enumClass, s));
  }

  /**
   * @param <E> type of the {@link Enum}.
   * @param name the variable {@link #getName() name}.
   * @param enumClass the {@link Class} reflecting the {@link Enum}.
   * @return the specified {@link VariableDefinition}.
   */
  public static <E extends Enum<E>> VariableDefinition<E> ofEnum(String name, Class<E> enumClass) {

    return new VariableDefinition<>(name, enumClass, null, s -> Enum.valueOf(enumClass, s));
  }

  /**
   * @param name the variable {@link #getName() name}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   * @return the specified {@link VariableDefinition}.
   */
  public static VariableDefinition<Integer> ofInteger(String name, Integer defaultValue) {

    return new VariableDefinition<>(name, Integer.class, defaultValue, Integer::valueOf);
  }

  /**
   * @param name the variable {@link #getName() name}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   * @return the specified {@link VariableDefinition}.
   */
  public static VariableDefinition<Long> ofLong(String name, Long defaultValue) {

    return new VariableDefinition<>(name, Long.class, defaultValue, Long::valueOf);
  }

  /**
   * @param name the variable {@link #getName() name}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   * @return the specified {@link VariableDefinition}.
   */
  public static VariableDefinition<Double> ofDouble(String name, Double defaultValue) {

    return new VariableDefinition<>(name, Double.class, defaultValue, Double::valueOf);
  }

  /**
   * @param name the variable {@link #getName() name}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   * @return the specified {@link VariableDefinition}.
   */
  public static VariableDefinition<Duration> ofDuration(String name, Duration defaultValue) {

    return new VariableDefinition<>(name, Duration.class, defaultValue, Duration::parse);
  }

}