/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.variable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple wrapper for a {@link Map} of {@link VariableDefinition variables}.
 *
 * @since 1.0.0
 */
public class VariableMap {

  private final Map<String, Object> map;

  private final Map<String, Object> unmodifyableMap;

  /**
   * The constructor.
   */
  public VariableMap() {

    this(new HashMap<>());
  }

  /**
   * The constructor.
   *
   * @param map the raw {@link Map} with the configuration values.
   */
  public VariableMap(Map<String, Object> map) {

    super();
    this.map = map;
    this.unmodifyableMap = Collections.unmodifiableMap(map);
  }

  /**
   * @param <T> type of config value.
   * @param variable the {@link VariableDefinition}.
   * @return the value of the given {@link VariableDefinition}.
   */
  @SuppressWarnings("unchecked")
  public <T> T get(VariableDefinition<T> variable) {

    if (variable == null) {
      return null;
    }
    String key = variable.getName();
    T value = (T) this.map.get(key);
    if ((value == null) && !this.map.containsKey(key)) {
      value = variable.getDefaultValue();
    }
    return value;
  }

  /**
   * @param variable the {@link VariableDefinition}.
   * @return the value of the given {@link VariableDefinition}.
   */
  public boolean getBoolean(VariableDefinition<Boolean> variable) {

    Boolean value = get(variable);
    if (value != null) {
      return value.booleanValue();
    }
    throw new IllegalStateException("The variable " + variable.getName() + " is undefined and has no default value.");
  }

  /**
   * @param variable the {@link VariableDefinition}.
   * @param defaultValue the additional default value returned if the variable is undefined and the given
   *        {@link VariableDefinition} has no {@link VariableDefinition#getDefaultValue() default value}.
   * @return the value of the given {@link VariableDefinition}.
   */
  public boolean getBoolean(VariableDefinition<Boolean> variable, boolean defaultValue) {

    Boolean value = get(variable);
    if (value != null) {
      return value.booleanValue();
    }
    return defaultValue;
  }

  /**
   * @param <T> type of config value.
   * @param key the {@link VariableDefinition}.
   * @param value the new configuration value for the given {@link VariableDefinition}.
   * @return the old configuration value that was previously associated with the given {@link VariableDefinition}. Will
   *         be {@code null} if no value was configured before.
   */
  @SuppressWarnings("unchecked")
  public <T> T set(VariableDefinition<T> key, T value) {

    return (T) this.map.put(key.getName(), value);
  }

  /**
   * @return the raw {@link Map} with the configuration.
   */
  public Map<String, Object> getMap() {

    return this.unmodifyableMap;
  }

  @Override
  public String toString() {

    return this.map.toString();
  }

}
