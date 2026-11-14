/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import io.github.mmm.base.exception.ObjectMismatchException;
import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.base.lang.ValueType;
import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.base.metainfo.MetaInfos;

/// Implementation of [MetaInfo] as wrapper of [Map].
///
/// @since 1.0.0
public abstract class AbstractMetaInfo implements MetaInfo {

  @Override
  public AbstractMetaInfo getParent() {

    return null;
  }

  /// @return `this` instance as [MetaInfoInherited] to be used as [parent][#getParent()] of a new child
  ///         (see `with` methods) or `null` if this is the empty instance.
  protected MetaInfoInherited asParent() {

    return null;
  }

  /// @param inherit the inherit flag from [#get(boolean, String)]. Typically to be ignored but may be used in
  ///        special cases.
  /// @param key the key of the requested meta-information.
  /// @return the value of the meta-information for the given `key` without inheritance from [parent][#getParent()]. Will be `null` if no value is defined for the given `key`.
  /// @see #get(boolean, String)
  protected abstract String getPlain(boolean inherit, String key);

  @Override
  public String get(boolean inherit, String key) {

    return getInternal(inherit, key, null);
  }

  @Override
  public String get(boolean inherit, boolean required, String key) {

    String value = get(inherit, key);
    if ((value == null) && required) {
      throw new ObjectNotFoundException("MetaInfo-value", qualifyKey(key));
    }
    return value;
  }

  @Override
  public <T> T getGeneric(boolean inherit, boolean required, String key, ValueType<T> type) {

    String value = get(inherit, required, key);
    if (value == null) {
      return null;
    }
    try {
      return type.parse(value);
    } catch (Exception e) {
      String expected = type.toString() + " for " + qualifyKey(key);
      throw new ObjectMismatchException(value, expected, e);
    }
  }

  /// @param inherit - `true` to inherit meta-information from the [parent][#getParent()], `false` to
  ///        only return plain meta-information defined in this [MetaInfo] itself.
  /// @param key the key of the requested meta-information.
  /// @param stop the ancestor [AbstractMetaInfo] where to stop inheriting.
  /// @return the value of the meta-information for the given `key`. Will be `null` if no value is defined
  ///         for the given `key`.
  public String getInternal(boolean inherit, String key, MetaInfo stop) {

    String value = getPlain(inherit, qualifyKey(key));

    if ((value == null) && inherit) {
      AbstractMetaInfo parent = getParent();
      if ((parent != null) && (parent != stop)) {
        value = parent.get(true, key);
      }
    }
    return value;
  }

  /// @param key the raw key.
  /// @return the qualified key with the [key prefix][#getKeyPrefix()] prepended.
  protected String qualifyKey(String key) {

    String keyPrefix = getKeyPrefix();
    if (keyPrefix.isEmpty() || (key == null)) {
      return key;
    }
    return keyPrefix + key;
  }

  /// @param key the raw key.
  /// @return the unqualified key with the [key prefix][#getKeyPrefix()] removed. Will be `null` if the key
  ///         was `null` or the key did not [start with][String#startsWith(String)] the [key prefix][#getKeyPrefix()].
  protected String unqualifyKey(String key) {

    String keyPrefix = getKeyPrefix();
    if (keyPrefix.isEmpty()) {
      return key;
    }
    if ((key != null) && key.startsWith(keyPrefix)) {
      return key.substring(keyPrefix.length());
    }
    return null;
  }

  @Override
  public int size() {

    int size = 0;
    for (String key : this) {
      assert (key != null);
      size++;
    }
    return size;
  }

  @Override
  public MetaInfo with(String key, String value) {

    return new MetaInfoSingle(asParent(), key, value);
  }

  @Override
  public MetaInfo with(Map<String, String> map, String keyPrefix) {

    if (map == null) {
      return this;
    }
    return new MetaInfoMap(map, asParent(), keyPrefix);
  }

  @Override
  public MetaInfo with(Properties properties, String keyPrefix) {

    if (properties == null) {
      return this;
    }
    return new MetaInfoProperties(properties, asParent(), keyPrefix);
  }

  @Override
  public MetaInfo with(MetaInfos metaValues) {

    String[] values = metaValues.value();
    if (values.length == 0) {
      return this;
    }
    MetaInfoValue value = null;
    // process in reverse order to retain original order...
    for (int i = values.length - 1; i >= 0; i--) {
      value = MetaInfoValue.of(values[i], value);
    }
    MetaInfoValues result = new MetaInfoValues(value, asParent());
    assert result.hasUniqueKeys();
    return result;
  }

  @Override
  public String toString() {

    return toString(false);
  }

  /// @param sort - `true` to sort by keys ensuring deterministic results, `false` otherwise.
  /// @return the [string representation][#toString()].
  public String toString(boolean sort) {

    StringBuilder sb = new StringBuilder();
    Iterable<String> keys = this;
    if (sort) {
      Set<String> keySet = new TreeSet<>();
      for (String key : this) {
        keySet.add(key);
      }
      keys = keySet;
    }
    sb.append('{');
    boolean first = true;
    for (String key : keys) {
      if (first) {
        first = false;
      } else {
        sb.append(", ");
      }
      String value = get(key);
      sb.append(key);
      sb.append('=');
      sb.append(value);
    }
    sb.append('}');
    return sb.toString();
  }

}
