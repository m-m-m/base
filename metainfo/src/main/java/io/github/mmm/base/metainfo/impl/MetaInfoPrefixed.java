/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Iterator;
import java.util.Objects;

import io.github.mmm.base.metainfo.MetaInfo;

/**
 * Empty {@link MetaInfo} implementation.
 */
public final class MetaInfoPrefixed extends MetaInfoInherited {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param keyPrefix the {@link #getKeyPrefix() key prefix}.
   */
  public MetaInfoPrefixed(MetaInfoInherited parent, String keyPrefix) {

    super(parent, keyPrefix);
    Objects.requireNonNull(parent);
    assert (!keyPrefix.isEmpty());
  }

  @Override
  public String getPlain(boolean inherit, String key) {

    return this.parent.get(inherit, key);
  }

  @Override
  public MetaInfoInherited getParent() {

    return null;
  }

  @Override
  public Iterator<String> iterator() {

    return new MetaInfoPrefixedIterator(this);
  }

  @Override
  protected Iterator<String> plainIterator() {

    return this.parent.iterator();
  }

}
