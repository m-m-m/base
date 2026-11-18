/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import io.github.mmm.base.metainfo.MetaInfo;

/// Implementation of [MetaInfo] as wrapper of [Map].
///
/// @since 1.0.0
public final class MetaInfoProperties extends MetaInfoCollection {

  private final Properties properties;

  /// The constructor.
  ///
  /// @param properties the [Properties].
  /// @param parent the [parent][#getParent()].
  /// @param keyPrefix the [key prefix][#getKeyPrefix()].
  public MetaInfoProperties(Properties properties, MetaInfoInherited parent, String keyPrefix) {

    super(parent, keyPrefix);
    Objects.requireNonNull(properties);
    this.properties = properties;
  }

  @Override
  protected Set<String> getKeysPlain() {

    return this.properties.stringPropertyNames();
  }

  @Override
  protected String getPlain(boolean inherit, String key) {

    if (KEY_SOURCE.getName().equals(key) && this.properties == System.getProperties()) {
      return VALUE_SOURCE_SYSTEM_PROPERTIES;
    }
    return this.properties.getProperty(key);
  }

}
