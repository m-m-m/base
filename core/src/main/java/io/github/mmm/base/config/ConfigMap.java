/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple wrapper for a {@link Map} with configuration.
 *
 * @since 1.0.0
 */
public class ConfigMap {

  private final Map<String, Object> map;

  private final Map<String, Object> unmodifyableMap;

  /**
   * The constructor.
   */
  public ConfigMap() {

    this(new HashMap<>());
  }

  /**
   * The constructor.
   *
   * @param map the raw {@link Map} with the configuration values.
   */
  public ConfigMap(Map<String, Object> map) {

    super();
    this.map = map;
    this.unmodifyableMap = Collections.unmodifiableMap(map);
  }

  /**
   * @param <T> type of config value.
   * @param key the {@link ConfigOption}.
   * @return the configuration value for the given {@link ConfigOption}.
   */
  @SuppressWarnings("unchecked")
  public <T> T get(ConfigOption<T> key) {

    if (key == null) {
      return null;
    }
    T value = (T) this.map.get(key.getKey());
    if (value == null) {
      value = key.getDefaultValue();
    }
    return value;
  }

  /**
   * @param <T> type of config value.
   * @param key the {@link ConfigOption}.
   * @param value the new configuration value for the given {@link ConfigOption}.
   * @return the old configuration value that was previously associated with the given {@link ConfigOption}. Will be
   *         {@code null} if no value was configured before.
   */
  @SuppressWarnings("unchecked")
  public <T> T set(ConfigOption<T> key, T value) {

    return (T) this.map.put(key.getKey(), value);
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
