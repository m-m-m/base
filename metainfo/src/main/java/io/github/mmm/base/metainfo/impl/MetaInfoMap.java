/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.github.mmm.base.metainfo.MetaInfo;

/**
 * Implementation of {@link MetaInfo} as wrapper of {@link Map}.
 *
 * @since 1.0.0
 */
public final class MetaInfoMap extends InheritedMetaInfo {

  /** @see #getPlain(String) */
  protected final Map<String, String> map;

  /**
   * The constructor.
   *
   * @param map the {@link Map} to wrap as {@link MetaInfo}.
   */
  public MetaInfoMap(Map<String, String> map) {

    this(map, null, "");
  }

  /**
   * The constructor.
   *
   * @param map the {@link Map} to wrap as {@link MetaInfo}.
   * @param parent the {@link #getParent() parent}.
   * @param keyPrefix the {@link #getKeyPrefix() key prefix}.
   */
  public MetaInfoMap(Map<String, String> map, InheritedMetaInfo parent, String keyPrefix) {

    super(parent, keyPrefix);
    Objects.requireNonNull(map);
    this.map = map;
  }

  @Override
  protected Set<String> getKeysPlain() {

    return this.map.keySet();
  }

  @Override
  protected String getPlain(String key) {

    return this.map.get(key);
  }

  @Override
  protected int getSizePlain() {

    return this.map.size();
  }

}
