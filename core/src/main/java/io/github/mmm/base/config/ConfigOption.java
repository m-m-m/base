/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.config;

import java.util.Map;

/**
 * Represents a single configuration option for a {@link ConfigMap}. It is a simple container for the {@link #getKey()
 * key} using a generic {@link #getType() type} making the {@link ConfigMap} type-safe.
 *
 * @param <T> type of the value for this option. See {@link #getType()}.
 * @since 1.0.0
 */
public class ConfigOption<T> {

  private final String key;

  private final Class<T> type;

  private final T defaultValue;

  /**
   * The constructor.
   *
   * @param key the {@link #getKey() key}.
   * @param type the {@link #getType() type}.
   */
  public ConfigOption(String key, Class<T> type) {

    this(key, type, null);
  }

  /**
   * The constructor.
   *
   * @param key the {@link #getKey() key}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   */
  public ConfigOption(String key, T defaultValue) {

    this(key, null, defaultValue);
  }

  /**
   * The constructor.
   *
   * @param key the {@link #getKey() key}.
   * @param type the {@link #getType() type}.
   * @param defaultValue the {@link #getDefaultValue() default value}.
   */
  @SuppressWarnings("unchecked")
  public ConfigOption(String key, Class<T> type, T defaultValue) {

    super();
    this.key = key;
    if ((type == null) && (defaultValue != null)) {
      this.type = (Class<T>) defaultValue.getClass();
    } else {
      this.type = type;
    }
    this.defaultValue = defaultValue;
  }

  /**
   * @return the {@link Map#containsKey(Object) key} of this configuration option.
   */
  public String getKey() {

    return this.key;
  }

  /**
   * @return the {@link Class} reflecting the value associated with this key.
   */
  public Class<T> getType() {

    return this.type;
  }

  /**
   * @return the optional default value to use if the option is not explicitly configured.
   */
  public T getDefaultValue() {

    return this.defaultValue;
  }

  @Override
  public String toString() {

    return this.key;
  }

}