/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo;

import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.base.metainfo.impl.MetaInfoEmpty;
import io.github.mmm.base.metainfo.impl.MetaInfoValues;

/**
 * Interface for meta-information similar to {@link java.util.Properties} but more sophisticated. Implements
 * {@link Iterable} allowing to {@link #iterator() iterate} the available meta-information keys.<br>
 * Use {@link #empty()} to get the empty instance and then call any {@code with} method to extended with real data.
 * Example to show usage:
 *
 * <pre>
 * {@literal @}{@link MetaInfoValues}({"key1=value1", "key2=value2"})
 * public class AnnotatedClass {
 *   public static void main(String[] args) {
 *     {@link MetaInfo} metaInfo = {@link MetaInfo}.{@link #empty()}.{@link #with(AnnotatedElement) with}(AnnotatedClass.class);
 *     metaInfo = metaInfo.{@link #with(Map) with}(Map.of("key2", "two", "key3", "value3"));
 *     for (String key : metaInfo) {
 *       String value = metaInfo.{@link #get(String) get}(key);
 *       System.out.println(key + "=" + value);
 *     }
 *   }
 * }
 * </pre>
 *
 * This will print the following output (order is undefined):
 *
 * <pre>
 * key1=value1
 * key2=two
 * key3=value3
 * </pre>
 *
 * <b>ATTENTION:</b> Typically the {@code with} methods will return a new instance of {@link MetaInfo} with the invoked
 * {@link MetaInfo} as {@link #getParent() parent}. However, this is not guaranteed. Especially immutable
 * implementations may return a {@link MetaInfo} with a different {@link #getParent() parent}.
 *
 * @since 1.0.0
 */
public interface MetaInfo extends Iterable<String> {

  /**
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key}. Will be {@code null} if no value is defined
   *         for the given {@code key}.
   */
  default String get(String key) {

    return get(true, key);
  }

  /**
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key}. Will be {@code null} if no value is defined
   *         for the given {@code key}.
   * @throws ObjectNotFoundException if the specified value is undefined.
   */
  default String getRequired(String key) {

    return get(true, true, key);
  }

  /**
   * @param key the key of the requested meta-information.
   * @param defaultValue the default value returned if the actual value is undefined.
   * @return the value of the meta-information for the given {@code key}. Will be {@code null} if no value is defined
   *         for the given {@code key}.
   */
  default String get(String key, String defaultValue) {

    return get(true, key, defaultValue);
  }

  /**
   * @param inherit - {@code true} to inherit meta-information from the {@link #getParent() parent}, {@code false} to
   *        only return plain meta-information defined in this {@link MetaInfo} itself.
   * @param key the key of the requested meta-information.
   * @param defaultValue the default value returned if the actual value is undefined.
   * @return the value of the meta-information for the given {@code key}. Will be {@code null} if no value is defined
   *         for the given {@code key}.
   */
  default String get(boolean inherit, String key, String defaultValue) {

    String value = get(inherit, key);
    if (value == null) {
      value = defaultValue;
    }
    return value;
  }

  /**
   * @param inherit - {@code true} to inherit meta-information from the {@link #getParent() parent}, {@code false} to
   *        only return plain meta-information defined in this {@link MetaInfo} itself.
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key}. Will be {@code null} if no value is defined
   *         for the given {@code key}.
   */
  String get(boolean inherit, String key);

  /**
   * @param inherit - {@code true} to inherit meta-information from the {@link #getParent() parent}, {@code false} to
   *        only return plain meta-information defined in this {@link MetaInfo} itself.
   * @param required - {@code true} if the requested value is required and an exception shall be raised if it is
   *        undefined, {@code false} otherwise (return {@code null} if undefined).
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key}. Will be {@code null} if no value is defined
   *         for the given {@code key}.
   * @throws ObjectNotFoundException if the specified value is undefined and {@code required} was {@code true}.
   */
  default String get(boolean inherit, boolean required, String key) {

    String value = get(inherit, key);
    if (value == null) {
      throw new ObjectNotFoundException("MetaInfo-value", key);
    }
    return value;
  }

  /**
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key} parsed as {@link Long}. Will be {@code null} if
   *         no value is defined for the given {@code key}.
   * @throws IllegalArgumentException if the value cannot be parsed as {@link Long}.
   */
  default Long getAsLong(String key) {

    return getAsLong(true, key);
  }

  /**
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key} parsed as {@link Long}.
   * @throws ObjectNotFoundException if the specified value is undefined.
   * @throws IllegalArgumentException if the value cannot be parsed as {@link Long}.
   */
  default long getAsLongRequired(String key) {

    return getAsLong(true, true, key).longValue();
  }

  /**
   * @param inherit - {@code true} to inherit meta-information from the {@link #getParent() parent}, {@code false} to
   *        only return plain meta-information defined in this {@link MetaInfo} itself.
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key} parsed as {@link Long}. Will be {@code null} if
   *         no value is defined for the given {@code key}.
   * @throws IllegalArgumentException if the value cannot be parsed as {@link Long}.
   */
  default Long getAsLong(boolean inherit, String key) {

    return getAsLong(inherit, false, key);
  }

  /**
   * @param inherit - {@code true} to inherit meta-information from the {@link #getParent() parent}, {@code false} to
   *        only return plain meta-information defined in this {@link MetaInfo} itself.
   * @param required - {@code true} if the requested value is required and an exception shall be raised if it is
   *        undefined, {@code false} otherwise (return {@code null} if undefined).
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key} parsed as {@link Long}. Will be {@code null} if
   *         no value is defined for the given {@code key}.
   * @throws ObjectNotFoundException if the specified value is undefined and {@code required} was {@code true}.
   * @throws IllegalArgumentException if the value cannot be parsed as {@link Long}.
   */
  default Long getAsLong(boolean inherit, boolean required, String key) {

    String value = get(inherit, required, key);
    if (value == null) {
      return null;
    }
    try {
      return Long.valueOf(value);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Meta-info for key '" + key + "' is no long value", e);
    }
  }

  /**
   * @param key the key of the requested meta-information.
   * @param defaultValue the default value returned if the actual value is undefined.
   * @return the value of the meta-information for the given {@code key} parsed as {@link long}. If the actual value is
   *         undefined, the given {@code defaultValue} will be returned.
   * @throws IllegalArgumentException if the value cannot be parsed as {@link long}.
   */
  default long getAsLong(String key, long defaultValue) {

    return getAsLong(true, key, defaultValue);
  }

  /**
   * @param inherit - {@code true} to inherit meta-information from the {@link #getParent() parent}, {@code false} to
   *        only return plain meta-information defined in this {@link MetaInfo} itself.
   * @param key the key of the requested meta-information.
   * @param defaultValue the default value returned if the actual value is undefined.
   * @return the value of the meta-information for the given {@code key} parsed as {@link long}. If the actual value is
   *         undefined, the given {@code defaultValue} will be returned.
   * @throws IllegalArgumentException if the value cannot be parsed as {@link long}.
   */
  default long getAsLong(boolean inherit, String key, long defaultValue) {

    Long value = getAsLong(inherit, key);
    if (value == null) {
      return defaultValue;
    }
    return value.longValue();
  }

  /**
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key} parsed as {@link Boolean}. Will be {@code null}
   *         if no value is defined for the given {@code key}.
   * @throws IllegalArgumentException if the value cannot be parsed as {@link Boolean}.
   */
  default Boolean getAsBoolean(String key) {

    return getAsBoolean(true, key);
  }

  /**
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key} parsed as {@link Boolean}.
   * @throws ObjectNotFoundException if the specified value is undefined.
   * @throws IllegalArgumentException if the value cannot be parsed as {@link Boolean}.
   */
  default boolean getAsBooleanRequired(String key) {

    return getAsBoolean(true, true, key).booleanValue();
  }

  /**
   * @param inherit - {@code true} to inherit meta-information from the {@link #getParent() parent}, {@code false} to
   *        only return plain meta-information defined in this {@link MetaInfo} itself.
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key} parsed as {@link Boolean}. Will be {@code null}
   *         if no value is defined for the given {@code key}.
   * @throws IllegalArgumentException if the value cannot be parsed as {@link Boolean}.
   */
  default Boolean getAsBoolean(boolean inherit, String key) {

    return getAsBoolean(inherit, false, key);
  }

  /**
   * @param inherit - {@code true} to inherit meta-information from the {@link #getParent() parent}, {@code false} to
   *        only return plain meta-information defined in this {@link MetaInfo} itself.
   * @param required - {@code true} if the requested value is required and an exception shall be raised if it is
   *        undefined, {@code false} otherwise (return {@code null} if undefined).
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key} parsed as {@link Boolean}. Will be {@code null}
   *         if no value is defined for the given {@code key}.
   * @throws ObjectNotFoundException if the specified value is undefined and {@code required} was {@code true}.
   * @throws IllegalArgumentException if the value cannot be parsed as {@link Boolean}.
   */
  default Boolean getAsBoolean(boolean inherit, boolean required, String key) {

    String value = get(inherit, key);
    if (value == null) {
      return null;
    }
    if ("true".equals(value)) {
      return Boolean.TRUE;
    } else if ("false".equals(value)) {
      return Boolean.FALSE;
    }
    throw new IllegalArgumentException("Meta-info for key '" + key + "' is no boolean value: " + value);
  }

  /**
   * @param key the key of the requested meta-information.
   * @param defaultValue the default value returned if the actual value is undefined.
   * @return the value of the meta-information for the given {@code key} parsed as {@link boolean}. If the actual value
   *         is undefined, the given {@code defaultValue} will be returned.
   * @throws IllegalArgumentException if the value cannot be parsed as {@link Boolean}.
   */
  default boolean getAsBoolean(String key, boolean defaultValue) {

    return getAsBoolean(true, key, defaultValue);
  }

  /**
   * @param inherit - {@code true} to inherit meta-information from the {@link #getParent() parent}, {@code false} to
   *        only return plain meta-information defined in this {@link MetaInfo} itself.
   * @param key the key of the requested meta-information.
   * @param defaultValue the default value returned if the actual value is undefined.
   * @return the value of the meta-information for the given {@code key} parsed as {@link boolean}. If the actual value
   *         is undefined, the given {@code defaultValue} will be returned.
   * @throws IllegalArgumentException if the value cannot be parsed as {@link Boolean}.
   */
  default boolean getAsBoolean(boolean inherit, String key, boolean defaultValue) {

    Boolean value = getAsBoolean(inherit, key);
    if (value == null) {
      return defaultValue;
    }
    return value.booleanValue();
  }

  /**
   * @return the number of {@link #get(String) entries} contained in this {@link MetaInfo}.
   */
  int size();

  /**
   * @return {@code true} if empty, {@code false} otherwise.
   */
  default boolean isEmpty() {

    return !iterator().hasNext();
  }

  /**
   * @return the potential parent {@link MetaInfo} to inherit from. Will be {@code null} if nothing is inherited.
   */
  default MetaInfo getParent() {

    return null;
  }

  /**
   * @return the prefix for the {@link #get(String) keys} as namespace. E.g. if the key prefix is "spring.datasource."
   *         and you call {@link #get(String)} with "password" it will return the property for the key
   *         "spring.datasource.password" from the underlying mapping. The default key prefix is the empty
   *         {@link String}.
   */
  default String getKeyPrefix() {

    return "";
  }

  /**
   * Adds or updates the specified meta-info.<br>
   * <b>ATTENTION:</b> Please consider using {@link #with(Map)} and other related factory methods instead of
   * sequentially using this method that may be inefficient.
   *
   * @param key the new key of the meta-info.
   * @param value the new value of the meta-info.
   * @return a new {@link MetaInfo} that extends this {@link MetaInfo} with the specified meta-information.
   */
  MetaInfo with(String key, String value);

  /**
   * @param map the {@link Map} with the meta-information to wrap.
   * @return a new {@link MetaInfo} that {@link #getParent() inherits} from this {@link MetaInfo} and extends it with
   *         the meta-information from the specified parameters.
   */
  default MetaInfo with(Map<String, String> map) {

    return with(map, "");
  }

  /**
   * @param map the {@link Map} with the meta-information to wrap.
   * @param keyPrefix the {@link #getKeyPrefix() key prefix}.
   * @return a new {@link MetaInfo} that extends this {@link MetaInfo} with the specified meta-information.
   */
  MetaInfo with(Map<String, String> map, String keyPrefix);

  /**
   * <b>ATTENTION:</b> Due to the design of {@link Properties} (with ability for inheritance but no API to get access to
   * the parent defaults) the result will be inefficient.
   *
   * @param properties the {@link Properties} with the meta-information to wrap.
   * @return a new {@link MetaInfo} that extends this {@link MetaInfo} with the specified meta-information.
   */
  default MetaInfo with(Properties properties) {

    return with(properties, "");
  }

  /**
   * <b>ATTENTION:</b> Due to the design of {@link Properties} (with ability for inheritance but no API to get access to
   * the parent defaults) the result will be inefficient.
   *
   * @param properties the {@link Properties} with the meta-information to wrap.
   * @param keyPrefix the {@link #getKeyPrefix() key prefix}.
   * @return a new {@link MetaInfo} that extends this {@link MetaInfo} with the specified meta-information.
   */
  MetaInfo with(Properties properties, String keyPrefix);

  /**
   * @param annotatedElement the {@link AnnotatedElement} (e.g. {@link Class}) that is (potentially) annotated with
   *        {@link MetaInfos}.
   * @return a new {@link MetaInfo} that extends this {@link MetaInfo} with the meta-information from the given
   *         {@link AnnotatedElement}. Will be {@code this} instance itself if the given {@link AnnotatedElement} has no
   *         {@link MetaInfos} annotation.
   */
  default MetaInfo with(AnnotatedElement annotatedElement) {

    MetaInfos metaInfos = annotatedElement.getAnnotation(MetaInfos.class);
    if (metaInfos == null) {
      return this;
    } else {
      return with(metaInfos);
    }
  }

  /**
   * @param metaValues the {@link MetaInfos}.
   * @return a new {@link MetaInfo} that extends this {@link MetaInfo} with the specified meta-information.
   */
  MetaInfo with(MetaInfos metaValues);

  /**
   * @param keyPrefix the {@link #getKeyPrefix() key prefix}.
   * @return a new {@link MetaInfo} that wraps this {@link MetaInfo} with all its parents using the given
   *         {@link #getKeyPrefix() key prefix}. The resulting {@link MetaInfo} will return {@code null} as
   *         {@link #getParent() parent}.
   */
  MetaInfo with(String keyPrefix);

  /**
   * @return a new {@link Properties} instance with all values from this {@link MetaInfo}.
   */
  default Properties asProperties() {

    Properties properties = new Properties();
    for (String key : this) {
      properties.setProperty(key, get(key));
    }
    return properties;
  }

  /**
   * @return a new {@link Map} instance with all values from this {@link MetaInfo}.
   */
  default Map<String, String> asMap() {

    Map<String, String> map = new HashMap<>(size());
    for (String key : this) {
      map.put(key, get(key));
    }
    return map;
  }

  /**
   * @return an instance of {@link MetaInfo} that is {@link #isEmpty() empty}.
   */
  static MetaInfo empty() {

    return MetaInfoEmpty.INSTANCE;
  }

  /**
   * @return the {@link MetaInfo} with the configuration for the running application.
   */
  static MetaInfo config() {

    return io.github.mmm.base.metainfo.impl.AppConfigHolder.CONFIG;
  }
}
