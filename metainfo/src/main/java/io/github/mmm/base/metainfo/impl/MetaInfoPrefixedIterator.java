/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Iterator;

import io.github.mmm.base.collection.AbstractIterator;

/**
 * Implementation of {@link Iterator} for {@link AbstractMetaInfo}.
 */
public class MetaInfoPrefixedIterator extends AbstractIterator<String> {

  private final MetaInfoPrefixed metaInfo;

  private final Iterator<String> iterator;

  /**
   * The constructor.
   *
   * @param metaInfo the {@link AbstractMetaInfo} to iterate.
   */
  public MetaInfoPrefixedIterator(MetaInfoPrefixed metaInfo) {

    super();
    this.metaInfo = metaInfo;
    this.iterator = metaInfo.parent.iterator();
    findFirst();
  }

  @Override
  protected String findNext() {

    while (this.iterator.hasNext()) {
      String key = this.metaInfo.unqualifyKey(this.iterator.next());
      if (key != null) {
        return key;
      }
    }
    return null;
  }

}
