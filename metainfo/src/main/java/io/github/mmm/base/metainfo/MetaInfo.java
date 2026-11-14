/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo;

import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.base.lang.ValueType;
import io.github.mmm.base.metainfo.impl.MetaInfoEmpty;
import io.github.mmm.base.metainfo.impl.MetaInfoValues;
import io.github.mmm.base.number.NumberType;
import io.github.mmm.base.variable.VariableDefinition;

/// Interface for meta-information similar to [java.util.Properties] but more sophisticated. Implements
/// [Iterable] allowing to [iterate][#iterator()] the available meta-information keys.
/// Use [#empty()] to get the empty instance and then call any `with` method to extended with real data.
/// Example to show usage:
///
/// ```
/// `@`[MetaInfoValues]({"key1=value1", "key2=value2"})
/// public class AnnotatedClass {
///   public static void main(String[] args) {
///     [MetaInfo] metaInfo = [MetaInfo].[#empty()].[with][#with(AnnotatedElement)](AnnotatedClass.class);
///     metaInfo = metaInfo.[with][#with(Map)](Map.of("key2", "two", "key3", "value3"));
///     for (String key : metaInfo) {
///       String value = metaInfo.[get][#get(String)](key);
///       System.out.println(key + "=" + value);
///     }
///   }
/// }
/// ```
///
/// This will print the following output (order is undefined):
///
/// ```
/// key1=value1
/// key2=two
/// key3=value3
/// ```
///
/// **ATTENTION:** Typically the `with` methods will return a new instance of [MetaInfo] with the invoked
/// [MetaInfo] as [parent][#getParent()]. However, this is not guaranteed. Especially immutable
/// implementations may return a [MetaInfo] with a different [parent][#getParent()].
///
/// @since 1.0.0
public interface MetaInfo extends Iterable<String> {

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined
  ///         for the given `key`.
  default String get(String key) {

    return get(true, key);
  }

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined
  ///         for the given `key`.
  /// @throws ObjectNotFoundException if the specified value is undefined.
  default String getRequired(String key) {

    return get(true, true, key);
  }

  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined
  ///         for the given `key`.
  default String get(String key, String defaultValue) {

    return get(true, key, defaultValue);
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to
  ///        only return plain meta-information defined in this [MetaInfo] itself.
  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined
  ///         for the given `key`.
  default String get(boolean inherit, String key, String defaultValue) {

    String value = get(inherit, key);
    if (value == null) {
      value = defaultValue;
    }
    return value;
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to
  ///        only return plain meta-information defined in this [MetaInfo] itself.
  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined
  ///         for the given `key`.
  String get(boolean inherit, String key);

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to
  ///        only return plain meta-information defined in this [MetaInfo] itself.
  /// @param required - `true` if the requested value is required and an exception shall be raised if it is
  ///        undefined, `false` otherwise (return `null` if undefined).
  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined
  ///         for the given `key`.
  /// @throws ObjectNotFoundException if the specified value is undefined and `required` was `true`.
  String get(boolean inherit, boolean required, String key);

  /// @param <V> type of the variable value.
  /// @param variable the [VariableDefinition].
  /// @return the requested [value][#get(String)]. Will be [VariableDefinition#getDefaultValue()] if no value
  ///         was found for the [variable name][VariableDefinition#getName()].
  default <V> V get(VariableDefinition<V> variable) {

    String value = get(variable.getName());
    if (value == null) {
      return variable.getDefaultValue();
    }
    return variable.parse(value);
  }

  /// @param <V> type of the variable value.
  /// @param variable the [VariableDefinition].
  /// @return the requested [value][#get(String)]. Will be [VariableDefinition#getDefaultValue()] if no value
  ///         was found for the [variable name][VariableDefinition#getName()].
  default <V> V getRequired(VariableDefinition<V> variable) {

    V defaultValue = variable.getDefaultValue();
    String value;
    if (defaultValue == null) {
      value = getRequired(variable.getName());
    } else {
      value = get(variable.getName());
      if (value == null) {
        return defaultValue;
      }
    }
    return variable.parse(value);
  }

  /// @param <T> type of the requested value.
  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to
  ///        only return plain meta-information defined in this [MetaInfo] itself.
  /// @param required - `true` if the requested value is required and an exception shall be raised if it is
  ///        undefined, `false` otherwise (return `null` if undefined).
  /// @param key the key of the requested meta-information.
  /// @param type the [Class] reflecting the requested value.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined
  ///         for the given `key`.
  /// @throws ObjectNotFoundException if the specified value is undefined and `required` was `true`.
  <T> T getGeneric(boolean inherit, boolean required, String key, ValueType<T> type);

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as [Integer]. Will be `null`
  ///         if no value is defined for the given `key`.
  /// @throws IllegalArgumentException if the value cannot be parsed as [Integer].
  default Integer getAsInteger(String key) {

    return getAsInteger(true, false, key);
  }

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as `int`.
  /// @throws ObjectNotFoundException if the specified value is undefined.
  /// @throws IllegalArgumentException if the value cannot be parsed as `int`.
  default int getAsIntegerRequired(String key) {

    return getAsInteger(true, true, key).intValue();
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to
  ///        only return plain meta-information defined in this [MetaInfo] itself.
  /// @param required - `true` if the requested value is required and an exception shall be raised if it is
  ///        undefined, `false` otherwise (return `null` if undefined).
  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as [Integer]. Will be `null`
  ///         if no value is defined for the given `key`.
  /// @throws ObjectNotFoundException if the specified value is undefined and `required` was `true`.
  /// @throws IllegalArgumentException if the value cannot be parsed as [Integer].
  default Integer getAsInteger(boolean inherit, boolean required, String key) {

    return getGeneric(inherit, required, key, NumberType.INTEGER);
  }

  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key` parsed as `int`. If the actual value is
  ///         undefined, the given `defaultValue` will be returned.
  /// @throws IllegalArgumentException if the value cannot be parsed as `int`.
  default int getAsInteger(String key, int defaultValue) {

    return getAsInteger(true, key, defaultValue);
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to
  ///        only return plain meta-information defined in this [MetaInfo] itself.
  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key` parsed as `int`. If the actual value is
  ///         undefined, the given `defaultValue` will be returned.
  /// @throws IllegalArgumentException if the value cannot be parsed as `int`.
  default int getAsInteger(boolean inherit, String key, int defaultValue) {

    Integer value = getAsInteger(inherit, false, key);
    if (value == null) {
      return defaultValue;
    }
    return value.intValue();
  }

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as [Long]. Will be `null` if
  ///         no value is defined for the given `key`.
  /// @throws IllegalArgumentException if the value cannot be parsed as [Long].
  default Long getAsLong(String key) {

    return getAsLong(true, false, key);
  }

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as `long`.
  /// @throws ObjectNotFoundException if the specified value is undefined.
  /// @throws IllegalArgumentException if the value cannot be parsed as `long`.
  default long getAsLongRequired(String key) {

    return getAsLong(true, true, key).longValue();
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to
  ///        only return plain meta-information defined in this [MetaInfo] itself.
  /// @param required - `true` if the requested value is required and an exception shall be raised if it is
  ///        undefined, `false` otherwise (return `null` if undefined).
  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as [Long]. Will be `null` if
  ///         no value is defined for the given `key`.
  /// @throws ObjectNotFoundException if the specified value is undefined and `required` was `true`.
  /// @throws IllegalArgumentException if the value cannot be parsed as [Long].
  default Long getAsLong(boolean inherit, boolean required, String key) {

    return getGeneric(inherit, required, key, NumberType.LONG);
  }

  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key` parsed as `long`. If the actual value is
  ///         undefined, the given `defaultValue` will be returned.
  /// @throws IllegalArgumentException if the value cannot be parsed as `long`.
  default long getAsLong(String key, long defaultValue) {

    return getAsLong(true, key, defaultValue);
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to
  ///        only return plain meta-information defined in this [MetaInfo] itself.
  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key` parsed as `long`. If the actual value is
  ///         undefined, the given `defaultValue` will be returned.
  /// @throws IllegalArgumentException if the value cannot be parsed as `long`.
  default long getAsLong(boolean inherit, String key, long defaultValue) {

    Long value = getAsLong(inherit, false, key);
    if (value == null) {
      return defaultValue;
    }
    return value.longValue();
  }

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as [Boolean]. Will be `null`
  ///         if no value is defined for the given `key`.
  /// @throws IllegalArgumentException if the value cannot be parsed as [Boolean].
  default Boolean getAsBoolean(String key) {

    return getAsBoolean(true, false, key);
  }

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as `boolean`.
  /// @throws ObjectNotFoundException if the specified value is undefined.
  /// @throws IllegalArgumentException if the value cannot be parsed as `boolean`.
  default boolean getAsBooleanRequired(String key) {

    return getAsBoolean(true, true, key).booleanValue();
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to
  ///        only return plain meta-information defined in this [MetaInfo] itself.
  /// @param required - `true` if the requested value is required and an exception shall be raised if it is
  ///        undefined, `false` otherwise (return `null` if undefined).
  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as [Boolean]. Will be `null`
  ///         if no value is defined for the given `key`.
  /// @throws ObjectNotFoundException if the specified value is undefined and `required` was `true`.
  /// @throws IllegalArgumentException if the value cannot be parsed as [Boolean].
  default Boolean getAsBoolean(boolean inherit, boolean required, String key) {

    return getGeneric(inherit, required, key, ValueType.BOOLEAN);
  }

  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key` parsed as `boolean`. If the actual value
  ///         is undefined, the given `defaultValue` will be returned.
  /// @throws IllegalArgumentException if the value cannot be parsed as `boolean`.
  default boolean getAsBoolean(String key, boolean defaultValue) {

    return getAsBoolean(true, key, defaultValue);
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to
  ///        only return plain meta-information defined in this [MetaInfo] itself.
  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key` parsed as `boolean`. If the actual value
  ///         is undefined, the given `defaultValue` will be returned.
  /// @throws IllegalArgumentException if the value cannot be parsed as `Boolean`.
  default boolean getAsBoolean(boolean inherit, String key, boolean defaultValue) {

    Boolean value = getAsBoolean(inherit, false, key);
    if (value == null) {
      return defaultValue;
    }
    return value.booleanValue();
  }

  /// @return the number of [entries][#get(String)] contained in this [MetaInfo].
  int size();

  /// @return `true` if empty, `false` otherwise.
  default boolean isEmpty() {

    return !iterator().hasNext();
  }

  /// @return the potential parent [MetaInfo] to inherit from. Will be `null` if nothing is inherited.
  default MetaInfo getParent() {

    return null;
  }

  /// @return the prefix for the [keys][#get(String)] as namespace. E.g. if the key prefix is "spring.datasource."
  ///         and you call [#get(String)] with "password" it will return the property for the key
  ///         "spring.datasource.password" from the underlying mapping. The default key prefix is the empty
  ///         [String].
  default String getKeyPrefix() {

    return "";
  }

  /// Adds or updates the specified meta-info.
  /// **ATTENTION:** Please consider using [#with(Map)] and other related factory methods instead of
  /// sequentially using this method that may be inefficient.
  ///
  /// @param key the new key of the meta-info.
  /// @param value the new value of the meta-info.
  /// @return a new [MetaInfo] that extends this [MetaInfo] with the specified meta-information.
  MetaInfo with(String key, String value);

  /// @param map the [Map] with the meta-information to wrap.
  /// @return a new [MetaInfo] that [inherits][#getParent()] from this [MetaInfo] and extends it with
  ///         the meta-information from the specified parameters.
  default MetaInfo with(Map<String, String> map) {

    return with(map, "");
  }

  /// @param map the [Map] with the meta-information to wrap.
  /// @param keyPrefix the [key prefix][#getKeyPrefix()].
  /// @return a new [MetaInfo] that extends this [MetaInfo] with the specified meta-information.
  MetaInfo with(Map<String, String> map, String keyPrefix);

  /// **ATTENTION:** Due to the design of [Properties] (with ability for inheritance but no API to get access to
  /// the parent defaults) the result will be inefficient.
  ///
  /// @param properties the [Properties] with the meta-information to wrap.
  /// @return a new [MetaInfo] that extends this [MetaInfo] with the specified meta-information.
  default MetaInfo with(Properties properties) {

    return with(properties, "");
  }

  /// **ATTENTION:** Due to the design of [Properties] (with ability for inheritance but no API to get access to
  /// the parent defaults) the result will be inefficient.
  ///
  /// @param properties the [Properties] with the meta-information to wrap.
  /// @param keyPrefix the [key prefix][#getKeyPrefix()].
  /// @return a new [MetaInfo] that extends this [MetaInfo] with the specified meta-information.
  MetaInfo with(Properties properties, String keyPrefix);

  /// @param annotatedElement the [AnnotatedElement] (e.g. [Class]) that is (potentially) annotated with
  ///        [MetaInfos].
  /// @return a new [MetaInfo] that extends this [MetaInfo] with the meta-information from the given
  ///         [AnnotatedElement]. Will be `this` instance itself if the given [AnnotatedElement] has no
  ///         [MetaInfos] annotation.
  default MetaInfo with(AnnotatedElement annotatedElement) {

    MetaInfos metaInfos = annotatedElement.getAnnotation(MetaInfos.class);
    if (metaInfos == null) {
      return this;
    } else {
      return with(metaInfos);
    }
  }

  /// @param metaValues the [MetaInfos].
  /// @return a new [MetaInfo] that extends this [MetaInfo] with the specified meta-information.
  MetaInfo with(MetaInfos metaValues);

  /// @param keyPrefix the [key prefix][#getKeyPrefix()].
  /// @return a new [MetaInfo] that wraps this [MetaInfo] with all its parents using the given
  ///         [key prefix][#getKeyPrefix()]. The resulting [MetaInfo] will return `null` as
  ///         [parent][#getParent()].
  MetaInfo with(String keyPrefix);

  /// @return a new [Properties] instance with all values from this [MetaInfo].
  default Properties asProperties() {

    Properties properties = new Properties();
    for (String key : this) {
      String value = get(key);
      if (value != null) {
        properties.setProperty(key, value);
      }
    }
    return properties;
  }

  /// @return a new [Map] instance with all values from this [MetaInfo].
  default Map<String, String> asMap() {

    Map<String, String> map = new HashMap<>(size());
    for (String key : this) {
      String value = get(key);
      if (value != null) {
        map.put(key, value);
      }
    }
    return map;
  }

  /// @return an instance of [MetaInfo] that is [empty][#isEmpty()].
  static MetaInfo empty() {

    return MetaInfoEmpty.INSTANCE;
  }

  /// @return the [MetaInfo] with the configuration for the running application.
  static MetaInfo config() {

    return io.github.mmm.base.metainfo.impl.AppConfigHolder.CONFIG;
  }
}
