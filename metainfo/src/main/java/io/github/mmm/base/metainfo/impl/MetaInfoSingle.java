/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.github.mmm.base.metainfo.MetaInfo;

/**
 * Implementation of {@link MetaInfo} as wrapper of {@link Map}.
 *
 * @since 1.0.0
 */
public final class MetaInfoSingle extends InheritedMetaInfo {

  private final String key;

  private final String value;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param key the {@link #get(String) key}.
   * @param value the {@link #get(String) value}.
   */
  public MetaInfoSingle(InheritedMetaInfo parent, String key, String value) {

    super(parent, "");
    Objects.requireNonNull(key);
    Objects.requireNonNull(value);
    this.key = key;
    this.value = value;
  }

  @Override
  protected String getPlain(String metaKey) {

    if (this.key.equals(metaKey)) {
      return this.value;
    }
    return null;
  }

  @Override
  protected Set<String> getKeysPlain() {

    return Collections.singleton(this.key);
  }

  @Override
  protected int getSizePlain() {

    return 1;
  }

  @Override
  public int size() {

    int size = 1;
    if (this.parent != null) {
      size = this.parent.size();
      if (this.parent.get(this.key) == null) {
        size++;
      }
    }
    return size;
  }

}
