/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.variable;

import java.util.Map;

/**
 * Definition of a variable for a {@link VariableMap}. It is a simple container for the {@link #getName() name} of the
 * variable with its {@link #getDefaultValue()} and its {@link #getType() type} making the
 * {@link VariableMap#get(VariableDefinition) access of the value} type-safe.
 *
 * @param <T> type of the value for this option. See {@link #getType()}.
 * @since 1.0.0
 */
public class VariableDefinition<T> {

  private final String name;

  private final Class<T> type;

  private final T defaultValue;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param type the {@link #getType() type}.
   */
  public VariableDefinition(String name, Class<T> type) {

    this(name, type, null);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   */
  public VariableDefinition(String name, T defaultValue) {

    this(name, null, defaultValue);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param type the {@link #getType() type}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   */
  @SuppressWarnings("unchecked")
  public VariableDefinition(String name, Class<T> type, T defaultValue) {

    super();
    this.name = name;
    if ((type == null) && (defaultValue != null)) {
      this.type = (Class<T>) defaultValue.getClass();
    } else {
      this.type = type;
    }
    this.defaultValue = defaultValue;
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
   * @return the optional default value to use if the variable is not explicitly defined.
   */
  public T getDefaultValue() {

    return this.defaultValue;
  }

  @Override
  public String toString() {

    return this.name;
  }

}