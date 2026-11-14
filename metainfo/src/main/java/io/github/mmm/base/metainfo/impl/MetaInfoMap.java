/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.github.mmm.base.metainfo.MetaInfo;

/// Implementation of [MetaInfo] as wrapper of [Map].
///
/// @since 1.0.0
public final class MetaInfoMap extends MetaInfoCollection {

  /// @see #getPlain(boolean, String)
  protected final Map<String, String> map;

  /// The constructor.
  ///
  /// @param map the [Map] to wrap as [MetaInfo].
  public MetaInfoMap(Map<String, String> map) {

    this(map, null, "");
  }

  /// The constructor.
  ///
  /// @param map the [Map] to wrap as [MetaInfo].
  /// @param parent the [parent][#getParent()].
  /// @param keyPrefix the [key prefix][#getKeyPrefix()].
  public MetaInfoMap(Map<String, String> map, MetaInfoInherited parent, String keyPrefix) {

    super(parent, keyPrefix);
    Objects.requireNonNull(map);
    this.map = map;
  }

  @Override
  protected Set<String> getKeysPlain() {

    return this.map.keySet();
  }

  @Override
  protected String getPlain(boolean inherit, String key) {

    return this.map.get(key);
  }

}
