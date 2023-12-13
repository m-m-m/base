/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Iterator;
import java.util.Set;

import io.github.mmm.base.collection.ReadOnlyIterator;

/**
 * Abstract base implementation of {@link MetaInfoInherited} based on {@link java.util.Map}.
 *
 * @since 1.0.0
 */
public abstract class MetaInfoCollection extends MetaInfoInherited {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param keyPrefix the {@link #getKeyPrefix() key prefix}.
   */
  public MetaInfoCollection(MetaInfoInherited parent, String keyPrefix) {

    super(parent, keyPrefix);
  }

  /**
   * @return a {@link Set} with the plain keys without inheritance from {@link #getParent() parent}.
   * @see java.util.Map#keySet()
   * @see #iterator()
   */
  protected abstract Set<String> getKeysPlain();

  @Override
  protected Iterator<String> plainIterator() {

    return getKeysPlain().iterator();
  }

  @Override
  public Iterator<String> iterator() {

    if (this.keyPrefix.isEmpty() && (this.parent == null)) {
      return new ReadOnlyIterator<>(getKeysPlain().iterator());
    }
    return super.iterator();
  }

  @Override
  public int size() {

    if (this.keyPrefix.isEmpty()) {
      if (this.parent == null) {
        return getKeysPlain().size();
      } else {
        // there is no perfect & most efficient solution to this problem, this seems the best we can do...
        Set<String> keySet = getKeysPlain();
        int size = keySet.size();
        for (String key : this.parent) {
          if (!keySet.contains(key)) {
            size++;
          }
        }
        return size;
      }
    }
    return super.size();
  }

  @Override
  public boolean isEmpty() {

    if (this.keyPrefix.isEmpty() && !getKeysPlain().isEmpty()) {
      return false;
    }
    return super.isEmpty();
  }

}
