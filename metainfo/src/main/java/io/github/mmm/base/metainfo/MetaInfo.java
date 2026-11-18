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
import io.github.mmm.base.number.NumberType;
import io.github.mmm.base.variable.VariableDefinition;

/// Interface for meta-information similar to [java.util.Properties] but more sophisticated. Implements [Iterable] allowing to [iterate][#iterator()] the
/// available meta-information keys. Use [#empty()] to get the empty instance and then call any `with` method to extended with real data. Example to show usage:
///
/// ```
/// `@`[io.github.mmm.base.metainfo.impl.MetaInfoValues]({"key1=value1", "key2=value2"})
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
/// **ATTENTION:** Typically the `with` methods will return a new instance of [MetaInfo] with the invoked [MetaInfo] as [parent][#getParent()]. However, this is
/// not guaranteed. Especially immutable implementations may return a [MetaInfo] with a different [parent][#getParent()].
///
/// @since 1.0.0
public interface MetaInfo extends Iterable<String> {

  /// [VariableDefinition] for the source a [MetaInfo] was loaded from in [#config()].
  public static final VariableDefinition<String> KEY_SOURCE = VariableDefinition.ofString("@source", "undefined");

  /// [Value][@link #get(VariableDefinition)] for [#KEY_SOURCE] for [System#getProperties() system properties].
  public static final String VALUE_SOURCE_SYSTEM_PROPERTIES = "SystemProperties";

  /// [Value][@link #get(VariableDefinition)] for [#KEY_SOURCE] for [System#getenv() system environment].
  public static final String VALUE_SOURCE_SYSTEM_ENVIRONMENT = "SystemEnvironment";

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined for the given `key`.
  default String get(String key) {

    return get(true, key);
  }

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined for the given `key`.
  /// @throws ObjectNotFoundException if the specified value is undefined.
  default String getRequired(String key) {

    return get(true, true, key);
  }

  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined for the given `key`.
  default String get(String key, String defaultValue) {

    return get(true, key, defaultValue);
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to only return plain meta-information defined in this
  ///        [MetaInfo] itself.
  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined for the given `key`.
  default String get(boolean inherit, String key, String defaultValue) {

    String value = get(inherit, key);
    if (value == null) {
      value = defaultValue;
    }
    return value;
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to only return plain meta-information defined in this
  ///        [MetaInfo] itself.
  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined for the given `key`.
  String get(boolean inherit, String key);

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to only return plain meta-information defined in this
  ///        [MetaInfo] itself.
  /// @param required - `true` if the requested value is required and an exception shall be raised if it is undefined, `false` otherwise (return `null` if
  ///        undefined).
  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined for the given `key`.
  /// @throws ObjectNotFoundException if the specified value is undefined and `required` was `true`.
  String get(boolean inherit, boolean required, String key);

  /// @param <V> type of the variable value.
  /// @param variable the [VariableDefinition].
  /// @return the requested [value][#get(String)]. Will be [VariableDefinition#getDefaultValue()] if no value was found for the
  ///         [variable name][VariableDefinition#getName()].
  default <V> V get(VariableDefinition<V> variable) {

    String value = get(variable.getName());
    if (value == null) {
      return variable.getDefaultValue();
    }
    return variable.parse(value);
  }

  /// @param <V> type of the variable value.
  /// @param variable the [VariableDefinition].
  /// @return the requested [value][#get(String)]. Will be [VariableDefinition#getDefaultValue()] if no value was found for the
  ///         [variable name][VariableDefinition#getName()].
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
  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to only return plain meta-information defined in this
  ///        [MetaInfo] itself.
  /// @param required - `true` if the requested value is required and an exception shall be raised if it is undefined, `false` otherwise (return `null` if
  ///        undefined).
  /// @param key the key of the requested meta-information.
  /// @param type the [Class] reflecting the requested value.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined for the given `key`.
  /// @throws ObjectNotFoundException if the specified value is undefined and `required` was `true`.
  <T> T getGeneric(boolean inherit, boolean required, String key, ValueType<T> type);

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as [Integer]. Will be `null` if no value is defined for the given `key`.
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

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to only return plain meta-information defined in this
  ///        [MetaInfo] itself.
  /// @param required - `true` if the requested value is required and an exception shall be raised if it is undefined, `false` otherwise (return `null` if
  ///        undefined).
  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as [Integer]. Will be `null` if no value is defined for the given `key`.
  /// @throws ObjectNotFoundException if the specified value is undefined and `required` was `true`.
  /// @throws IllegalArgumentException if the value cannot be parsed as [Integer].
  default Integer getAsInteger(boolean inherit, boolean required, String key) {

    return getGeneric(inherit, required, key, NumberType.INTEGER);
  }

  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key` parsed as `int`. If the actual value is undefined, the given `defaultValue` will be returned.
  /// @throws IllegalArgumentException if the value cannot be parsed as `int`.
  default int getAsInteger(String key, int defaultValue) {

    return getAsInteger(true, key, defaultValue);
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to only return plain meta-information defined in this
  ///        [MetaInfo] itself.
  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key` parsed as `int`. If the actual value is undefined, the given `defaultValue` will be returned.
  /// @throws IllegalArgumentException if the value cannot be parsed as `int`.
  default int getAsInteger(boolean inherit, String key, int defaultValue) {

    Integer value = getAsInteger(inherit, false, key);
    if (value == null) {
      return defaultValue;
    }
    return value.intValue();
  }

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as [Long]. Will be `null` if no value is defined for the given `key`.
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

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to only return plain meta-information defined in this
  ///        [MetaInfo] itself.
  /// @param required - `true` if the requested value is required and an exception shall be raised if it is undefined, `false` otherwise (return `null` if
  ///        undefined).
  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as [Long]. Will be `null` if no value is defined for the given `key`.
  /// @throws ObjectNotFoundException if the specified value is undefined and `required` was `true`.
  /// @throws IllegalArgumentException if the value cannot be parsed as [Long].
  default Long getAsLong(boolean inherit, boolean required, String key) {

    return getGeneric(inherit, required, key, NumberType.LONG);
  }

  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key` parsed as `long`. If the actual value is undefined, the given `defaultValue` will be
  ///         returned.
  /// @throws IllegalArgumentException if the value cannot be parsed as `long`.
  default long getAsLong(String key, long defaultValue) {

    return getAsLong(true, key, defaultValue);
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to only return plain meta-information defined in this
  ///        [MetaInfo] itself.
  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key` parsed as `long`. If the actual value is undefined, the given `defaultValue` will be
  ///         returned.
  /// @throws IllegalArgumentException if the value cannot be parsed as `long`.
  default long getAsLong(boolean inherit, String key, long defaultValue) {

    Long value = getAsLong(inherit, false, key);
    if (value == null) {
      return defaultValue;
    }
    return value.longValue();
  }

  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as [Boolean]. Will be `null` if no value is defined for the given `key`.
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

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to only return plain meta-information defined in this
  ///        [MetaInfo] itself.
  /// @param required - `true` if the requested value is required and an exception shall be raised if it is undefined, `false` otherwise (return `null` if
  ///        undefined).
  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` parsed as [Boolean]. Will be `null` if no value is defined for the given `key`.
  /// @throws ObjectNotFoundException if the specified value is undefined and `required` was `true`.
  /// @throws IllegalArgumentException if the value cannot be parsed as [Boolean].
  default Boolean getAsBoolean(boolean inherit, boolean required, String key) {

    return getGeneric(inherit, required, key, ValueType.BOOLEAN);
  }

  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key` parsed as `boolean`. If the actual value is undefined, the given `defaultValue` will be
  ///         returned.
  /// @throws IllegalArgumentException if the value cannot be parsed as `boolean`.
  default boolean getAsBoolean(String key, boolean defaultValue) {

    return getAsBoolean(true, key, defaultValue);
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to only return plain meta-information defined in this
  ///        [MetaInfo] itself.
  /// @param key the key of the requested meta-information.
  /// @param defaultValue the default value returned if the actual value is undefined.
  /// @return the value of the meta-information for the given `key` parsed as `boolean`. If the actual value is undefined, the given `defaultValue` will be
  ///         returned.
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

  /// @return the prefix for the [keys][#get(String)] as namespace. E.g. if the key prefix is "spring.datasource." and you call [#get(String)] with "password"
  ///         it will return the property for the key "spring.datasource.password" from the underlying mapping. The default key prefix is the empty [String].
  default String getKeyPrefix() {

    return "";
  }

  /// Adds or updates the specified meta-info. **ATTENTION:** Please consider using [#with(Map)] and other related factory methods instead of sequentially using
  /// this method that may be inefficient.
  ///
  /// @param key the new key of the meta-info.
  /// @param value the new value of the meta-info.
  /// @return a new [MetaInfo] that extends this [MetaInfo] with the specified meta-information.
  MetaInfo with(String key, String value);

  /// @param map the [Map] with the meta-information to wrap.
  /// @return a new [MetaInfo] that [inherits][#getParent()] from this [MetaInfo] and extends it with the meta-information from the specified parameters.
  default MetaInfo with(Map<String, String> map) {

    return with(map, "");
  }

  /// @param map the [Map] with the meta-information to wrap.
  /// @param keyPrefix the [key prefix][#getKeyPrefix()].
  /// @return a new [MetaInfo] that extends this [MetaInfo] with the specified meta-information.
  MetaInfo with(Map<String, String> map, String keyPrefix);

  /// **ATTENTION:** Due to the design of [Properties] (with ability for inheritance but no API to get access to the parent defaults) the result will be
  /// inefficient.
  ///
  /// @param properties the [Properties] with the meta-information to wrap.
  /// @return a new [MetaInfo] that extends this [MetaInfo] with the specified meta-information.
  default MetaInfo with(Properties properties) {

    return with(properties, "");
  }

  /// **ATTENTION:** Due to the design of [Properties] (with ability for inheritance but no API to get access to the parent defaults) the result will be
  /// inefficient.
  ///
  /// @param properties the [Properties] with the meta-information to wrap.
  /// @param keyPrefix the [key prefix][#getKeyPrefix()].
  /// @return a new [MetaInfo] that extends this [MetaInfo] with the specified meta-information.
  MetaInfo with(Properties properties, String keyPrefix);

  /// @param annotatedElement the [AnnotatedElement] (e.g. [Class]) that is (potentially) annotated with [MetaInfos].
  /// @return a new [MetaInfo] that extends this [MetaInfo] with the meta-information from the given [AnnotatedElement]. Will be `this` instance itself if the
  ///         given [AnnotatedElement] has no [MetaInfos] annotation.
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
  /// @return a new [MetaInfo] that wraps this [MetaInfo] with all its parents using the given [key prefix][#getKeyPrefix()]. The resulting [MetaInfo] will
  ///         return `null` as [parent][#getParent()].
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

  /// This method provides a full-fledged application configuration allowing to configure your application on the
  /// following levels:
  /// 1. `application.properties` found in the current working directory or on your Java class-/module-path.
  /// 2. [System environment variables][System#getenv()]
  /// 3. `app-config/application.properties` found in the current working directory or on your Java class-/module-path.
  /// 4. [System properties][System#getProperties()]
  ///
  /// Variables defined on an earlier level can be overridden by variables on a deeper level. Typically, you will ship
  /// your application with some `application.properties` in the "root package" placed in
  /// `src/main/resources/application.properties` (see 1.). There you define default settings reasonable for every
  /// environment. When using containers (e.g. docker), you typically configure applications via environment variables
  /// (see 2.) that will add and override to these defaults. Maybe you need some special configuration for a particular
  /// environment that you want to manage more explicitly. You can put that in the file
  /// `./app-config/application.properties` relative to the working directory from where your application gets started
  /// (see 3.). Here, you can add and override to the previous configuration (e.g. setting `db.default.url`). Finally,
  /// you can explicitly add and override all of that with explicit system properties. For example, if your start script
  /// is explicitly launching your application with `-Ddb.default.url=jdbc:postgresql://localhost/appdb` this will take
  /// highest precedence overriding any other configuration.
  ///
  /// Why did we chose `app-config` instead of `config` following spring-boot? This is because `config` is a legal Java
  /// package and will be protected by the JPMS (Java Platform Module System) forcing you to always open or export that
  /// package in your `module-info.java` causing confusion and complexity. Unlike spring-boot this framework was designed
  /// for JPMS from the start.
  ///
  /// To summarise, all you need to do for building a flexible application is getting your configuration via
  /// `MetaInfo.config()`. The [MetaInfo] API provides various methods to read your configuration properties. Here is an
  /// example:
  /// ```java
  /// MetaInfo config = MetaInfo.config();
  /// MetaInfo eventBus = config.with("event.bus.");
  /// String host = eventBus.get("host", "localhost"); // use "localhost" as default if event.bus.host is undefined.
  /// int port = eventBus.getAsInteger("port", 1791); // default for event.bus.port is 1791.
  /// String password = eventBus.getRequired("password"); // throw an exception if event.bus.password is undefined.
  /// ```
  ///
  /// @return the [MetaInfo] with the configuration for the running application.
  static MetaInfo config() {

    return io.github.mmm.base.metainfo.impl.AppConfigHolder.CONFIG;
  }
}
