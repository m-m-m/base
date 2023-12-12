/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of {@link Iterator} for {@link MetaInfoValue}.
 */
public class MetaInfoValueIterator implements Iterator<String> {

  private MetaInfoValue value;

  private Iterator<String> parentIterator;

  private String next;

  /**
   * The constructor.
   *
   * @param values the {@link MetaInfoValues} to iterate.
   */
  public MetaInfoValueIterator(MetaInfoValues values) {

    super();
    this.value = values.first;
    if (values.parent != null) {
      this.parentIterator = values.parent.iterator();
    }
    this.next = findNext();
  }

  private String findNext() {

    if (this.value != null) {
      String key = this.value.key;
      this.value = this.value.next;
      return key;
    } else if ((this.parentIterator != null) && this.parentIterator.hasNext()) {
      return this.parentIterator.next();
    }
    this.parentIterator = null;
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
