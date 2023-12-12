/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Collections;
import java.util.Iterator;

import io.github.mmm.base.metainfo.MetaInfo;

/**
 * Empty {@link MetaInfo} implementation.
 */
public final class MetaInfoEmpty extends AbstractMetaInfo {

  /** The singleton instance. */
  public static final MetaInfoEmpty INSTANCE = new MetaInfoEmpty();

  private MetaInfoEmpty() {

    super();
  }

  @Override
  public Iterator<String> iterator() {

    return Collections.emptyIterator();
  }

  @Override
  public String getPlain(String key) {

    return null;
  }

  @Override
  public int size() {

    return 0;
  }

  @Override
  public boolean isEmpty() {

    return true;
  }

  @Override
  public MetaInfo with(String key, String value) {

    return new MetaInfoValues(new MetaInfoValue(key, value), null);
  }

  @Override
  public String toString(boolean sort) {

    return "{}";
  }

}
