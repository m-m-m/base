/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.base.metainfo.MetaInfos;

/**
 * Implementation of {@link MetaInfo} based on {@link MetaInfoValue}.
 *
 * @since 1.0.0
 */
public final class MetaInfoValues extends MetaInfoInherited {

  final MetaInfoValue first;

  private final int size;

  /**
   * The constructor.
   *
   * @param first the first {@link MetaInfoValue}.
   * @param parent the {@link #getParent() parent}.
   */
  public MetaInfoValues(MetaInfoValue first, MetaInfoInherited parent) {

    super(parent, "");
    this.first = first;
    int i = 0;
    MetaInfoValue current = first;
    while (current != null) {
      i++;
      current = current.next;
    }
    this.size = i;
  }

  boolean hasUniqueKeys() {

    Set<String> keys = new HashSet<>(this.size);
    MetaInfoValue current = this.first;
    while (current != null) {
      boolean added = keys.add(current.key);
      if (!added) {
        throw new DuplicateObjectException("MetaInfoValue", current.key);
      }
      current = current.next;
    }
    return true;
  }

  @Override
  protected String getPlain(boolean inherit, String key) {

    MetaInfoValue current = this.first;
    while (current != null) {
      if (current.key.equals(key)) {
        return current.value;
      }
      current = current.next;
    }
    return null;
  }

  @Override
  public Iterator<String> iterator() {

    return new MetaInfoValueIterator(this, true);
  }

  @Override
  protected Iterator<String> plainIterator() {

    return new MetaInfoValueIterator(this, false);
  }

  @Override
  public int size() {

    if (this.parent == null) {
      return this.size;
    }
    return super.size();
  }

  @Override
  public boolean isEmpty() {

    return false;
  }

  @Override
  public MetaInfo with(String key, String value) {

    return new MetaInfoValues(new MetaInfoValue(key, value, this.first), this.parent);
  }

  @Override
  public MetaInfo with(MetaInfos metaValues) {

    String[] values = metaValues.value();
    if (values.length == 0) {
      return this;
    }
    MetaInfoValue value = this.first;
    // process in reverse order to retain original order...
    for (int i = values.length - 1; i >= 0; i--) {
      value = MetaInfoValue.of(values[i], value);
    }
    MetaInfoValues result = new MetaInfoValues(value, getParent());
    return result;
  }

}
