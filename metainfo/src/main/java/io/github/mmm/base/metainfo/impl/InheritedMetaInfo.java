/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.github.mmm.base.collection.ReadOnlyIterator;
import io.github.mmm.base.metainfo.MetaInfo;

/**
 * Implementation of {@link MetaInfo} as wrapper of {@link Map}.
 *
 * @since 1.0.0
 */
public abstract class InheritedMetaInfo extends AbstractMetaInfo {

  /** @see #getParent() */
  protected final InheritedMetaInfo parent;

  /** @see #getKeyPrefix() */
  protected final String keyPrefix;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param keyPrefix the {@link #getKeyPrefix() key prefix}.
   */
  public InheritedMetaInfo(InheritedMetaInfo parent, String keyPrefix) {

    super();
    Objects.requireNonNull(keyPrefix);
    this.parent = parent;
    this.keyPrefix = keyPrefix;
  }

  @Override
  public InheritedMetaInfo getParent() {

    return this.parent;
  }

  @Override
  protected InheritedMetaInfo asParent() {

    return this;
  }

  /**
   * @return a {@link Set} with the plain keys without inheritance from {@link #getParent() parent}.
   * @see java.util.Map#keySet()
   * @see #iterator()
   */
  protected abstract Set<String> getKeysPlain();

  /**
   * @return the plain {@link #size()} of this {@link MetaInfo} without inheritance from {@link #getParent() parent}.
   * @see #size()
   */
  protected int getSizePlain() {

    return getKeysPlain().size();
  }

  boolean containsKeyPlain(String key, InheritedMetaInfo stop) {

    InheritedMetaInfo metaInfo = this;
    while ((metaInfo != null) && (metaInfo != stop)) {
      String qualifiedKey = metaInfo.getKey(key);
      if (metaInfo.getKeysPlain().contains(qualifiedKey)) {
        return true;
      }
      metaInfo = metaInfo.parent;
    }
    return false;
  }

  @Override
  public String getKeyPrefix() {

    return this.keyPrefix;
  }

  @Override
  protected String getKey(String key) {

    if (this.keyPrefix.isEmpty()) {
      return key;
    }
    return this.keyPrefix + key;
  }

  @Override
  public Iterator<String> iterator() {

    if (this.keyPrefix.isEmpty() && (this.parent == null)) {
      return new ReadOnlyIterator<>(getKeysPlain().iterator());
    }
    return new InheritedMetaInfoIterator(this);
  }

  @Override
  public int size() {

    int size;
    if (this.keyPrefix.isEmpty()) {
      if (this.parent != null) {
        // there is no perfect & most efficient solution to this problem, this seems the best we can do...
        Set<String> keySet = getKeysPlain();
        size = keySet.size();
        for (String key : this.parent) {
          if (!keySet.contains(key)) {
            size++;
          }
        }
      } else {
        size = getSizePlain();
      }
    } else {
      size = 0;
      for (String key : this) {
        assert (key != null);
        size++;
      }
    }
    return size;
  }

}
