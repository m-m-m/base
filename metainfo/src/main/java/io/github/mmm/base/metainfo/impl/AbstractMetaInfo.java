/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.base.metainfo.MetaInfos;

/**
 * Implementation of {@link MetaInfo} as wrapper of {@link Map}.
 *
 * @since 1.0.0
 */
public abstract class AbstractMetaInfo implements MetaInfo {

  @Override
  public AbstractMetaInfo getParent() {

    return null;
  }

  /**
   * @return {@code this} instance as {@link InheritedMetaInfo} to be used as {@link #getParent() parent} of a new child
   *         (see {@code with} methods) or {@code null} if this is the empty instance.
   */
  protected InheritedMetaInfo asParent() {

    return null;
  }

  /**
   * @param key the key of the requested meta-information.
   * @return the value of the meta-information for the given {@code key} without inheritance from {@link #getParent()
   *         parent}. Will be {@code null} if no value is defined for the given {@code key}.
   * @see #get(boolean, String)
   */
  protected abstract String getPlain(String key);

  @Override
  public String get(boolean inherit, String key) {

    String value = getPlain(getKey(key));

    if ((value == null) && inherit) {
      AbstractMetaInfo parent = getParent();
      if (parent != null) {
        value = parent.get(true, key);
      }
    }
    return value;
  }

  /**
   * @return the prefix for the {@link #get(String) keys} as namespace. E.g. when using "spring.datasource." and you
   *         call {@link #get(String)} with "password" it will return the property for the key
   *         "spring.datasource.password" from the given {@link Properties}. The default value is the empty
   *         {@link String}.
   */
  public String getKeyPrefix() {

    return null;
  }

  /**
   * @param key the raw key.
   * @return the qualified key with the {@link #getKeyPrefix() key prefix}.
   */
  protected String getKey(String key) {

    return key;
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

  /**
   * @param sort - {@code true} to sort by keys ensuring deterministic results, {@code false} otherwise.
   * @return the {@link #toString() string representation}.
   */
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
