/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import io.github.mmm.base.metainfo.MetaInfo;

/**
 * Implementation of {@link MetaInfo} as wrapper of {@link Map}.
 *
 * @since 1.0.0
 */
public final class MetaInfoProperties extends InheritedMetaInfo {

  private final Properties properties;

  /**
   * The constructor.
   *
   * @param properties the {@link Properties}.
   * @param parent the {@link #getParent() parent}.
   * @param keyPrefix the {@link #getKeyPrefix() key prefix}.
   */
  public MetaInfoProperties(Properties properties, InheritedMetaInfo parent, String keyPrefix) {

    super(parent, keyPrefix);
    Objects.requireNonNull(properties);
    this.properties = properties;
  }

  @Override
  protected Set<String> getKeysPlain() {

    return this.properties.stringPropertyNames();
  }

  @Override
  protected String getPlain(String key) {

    return this.properties.getProperty(key);
  }

}
