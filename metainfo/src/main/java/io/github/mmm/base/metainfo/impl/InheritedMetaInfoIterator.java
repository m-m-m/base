/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of {@link Iterator} for {@link AbstractMetaInfo}.
 */
public class InheritedMetaInfoIterator implements Iterator<String> {

  private final InheritedMetaInfo metaInfo;

  private String keyPrefix;

  private InheritedMetaInfo currentMetaInfo;

  private Iterator<String> iterator;

  private String next;

  /**
   * The constructor.
   *
   * @param metaInfo the {@link AbstractMetaInfo} to iterate.
   */
  public InheritedMetaInfoIterator(InheritedMetaInfo metaInfo) {

    super();
    this.metaInfo = metaInfo;
    this.keyPrefix = metaInfo.keyPrefix;
    this.iterator = metaInfo.getKeysPlain().iterator();
    this.next = findNext();
  }

  private String findNext() {

    while (this.iterator != null) {
      while (this.iterator.hasNext()) {
        String key = this.iterator.next();
        if (key != null) {
          if (!this.keyPrefix.isEmpty()) {
            key = this.keyPrefix + key;
          }
          if ((this.currentMetaInfo == null) || !this.metaInfo.containsKeyPlain(key, this.currentMetaInfo)) {
            return key;
          }
        }
      }
      if (this.currentMetaInfo == null) {
        this.currentMetaInfo = this.metaInfo.getParent();
      } else {
        this.currentMetaInfo = this.currentMetaInfo.getParent();
      }
      if (this.currentMetaInfo == null) {
        this.iterator = null;
      } else {
        this.iterator = this.currentMetaInfo.iterator();
        this.keyPrefix = this.currentMetaInfo.keyPrefix;
      }
    }
    return null;
  }

  @Override
  public boolean hasNext() {

    return this.next != null;
  }

  @Override
  public String next() {

    if (this.next == null) {
      throw new NoSuchElementException();
    }
    String key = this.next;
    this.next = findNext();
    return key;
  }

}
