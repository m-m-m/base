/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import io.github.mmm.base.metainfo.MetaInfo;

/**
 * Implementation of {@link MetaInfo} as wrapper of {@link Map}.
 *
 * @since 1.0.0
 */
public abstract class MetaInfoInherited extends AbstractMetaInfo {

  /** @see #getParent() */
  protected final MetaInfoInherited parent;

  /** @see #getKeyPrefix() */
  protected final String keyPrefix;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param keyPrefix the {@link #getKeyPrefix() key prefix}.
   */
  public MetaInfoInherited(MetaInfoInherited parent, String keyPrefix) {

    super();
    Objects.requireNonNull(keyPrefix);
    this.parent = parent;
    this.keyPrefix = keyPrefix;
  }

  @Override
  public MetaInfoInherited getParent() {

    return this.parent;
  }

  @Override
  protected MetaInfoInherited asParent() {

    return this;
  }

  @Override
  public String getKeyPrefix() {

    return this.keyPrefix;
  }

  @Override
  public Iterator<String> iterator() {

    return new MetaInfoInheritedIterator(this);
  }

  /**
   * @return the {@link Iterator} of the plain keys of this {@link MetaInfoInherited} without filtering or inheritance.
   * @see #iterator()
   */
  protected abstract Iterator<String> plainIterator();

  @Override
  public MetaInfo with(String prefix) {

    return new MetaInfoPrefixed(this, prefix);
  }

}
