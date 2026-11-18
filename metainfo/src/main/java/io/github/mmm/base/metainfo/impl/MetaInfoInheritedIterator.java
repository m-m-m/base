/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Iterator;

import io.github.mmm.base.collection.AbstractIterator;

/// Implementation of [Iterator] for [MetaInfoInherited].
public class MetaInfoInheritedIterator extends AbstractIterator<String> {

  private final MetaInfoInherited metaInfo;

  private MetaInfoInherited currentMetaInfo;

  private Iterator<String> iterator;

  /// The constructor.
  ///
  /// @param metaInfo the [AbstractMetaInfo] to iterate.
  public MetaInfoInheritedIterator(MetaInfoInherited metaInfo) {

    super();
    this.metaInfo = metaInfo;
    this.currentMetaInfo = metaInfo;
    this.iterator = metaInfo.plainIterator();
    findFirst();
  }

  @Override
  protected String findNext() {

    while (this.iterator != null) {
      while (this.iterator.hasNext()) {
        String rawKey = this.iterator.next();
        String key = this.currentMetaInfo.unqualifyKey(rawKey);
        if ((key != null) && ((this.currentMetaInfo == this.metaInfo) || (this.metaInfo.getInternal(true, key, this.currentMetaInfo) == null))) {
          return key;
        }
      }
      if (this.currentMetaInfo != null) {
        this.currentMetaInfo = this.currentMetaInfo.getParent();
      }
      if (this.currentMetaInfo == null) {
        this.iterator = null;
      } else {
        this.iterator = this.currentMetaInfo.plainIterator();
      }
    }
    return null;
  }

}
