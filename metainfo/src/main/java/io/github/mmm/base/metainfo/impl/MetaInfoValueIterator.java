/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Iterator;

import io.github.mmm.base.collection.AbstractIterator;

/**
 * Implementation of {@link Iterator} for {@link MetaInfoValue}.
 */
public class MetaInfoValueIterator extends AbstractIterator<String> {

  private final MetaInfoValues values;

  private MetaInfoValue value;

  private Iterator<String> parentIterator;

  /**
   * The constructor.
   *
   * @param values the {@link MetaInfoValues} to iterate.
   * @param inherit - {@code true} to also iterate the inherited keys, {@code false} otherwise.
   */
  public MetaInfoValueIterator(MetaInfoValues values, boolean inherit) {

    super();
    this.values = values;
    this.value = values.first;
    if (inherit && (values.parent != null)) {
      this.parentIterator = values.parent.iterator();
    }
    findFirst();
  }

  @Override
  protected String findNext() {

    if (this.value != null) {
      String key = this.value.key;
      this.value = this.value.next;
      return key;
    } else if (this.parentIterator != null) {
      while (this.parentIterator.hasNext()) {
        String key = this.parentIterator.next();
        // avoid iterating duplicated keys...
        // inefficient solution but intended trade-off as MetaInfoValues is for small size with minimum memory overhead
        if (this.values.getPlain(true, key) == null) {
          return key;
        }
      }
    }
    this.parentIterator = null;
    return null;
  }

}
