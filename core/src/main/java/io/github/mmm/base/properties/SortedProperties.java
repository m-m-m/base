/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.properties;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import io.github.mmm.base.collection.ArrayIterator;

/**
 * This class extends {@link Properties} and makes them sorted by their {@link #keys() keys}. Sorting the properties is
 * useful when changes shall be compared using diff-algorithms.
 *
 * @since 1.0.0
 */
public class SortedProperties extends Properties {

  private static final long serialVersionUID = -7676191449564478612L;

  /**
   * The constructor.
   */
  public SortedProperties() {

    super();
  }

  /**
   * The constructor.
   *
   * @param defaults are the {@link Properties} to inherit as defaults.
   */
  public SortedProperties(Properties defaults) {

    super(defaults);
  }

  @Override
  public synchronized Enumeration<Object> keys() {

    Enumeration<Object> keyEnumeration = super.keys();
    Object[] keys = new Object[size()];
    int i = 0;
    while (keyEnumeration.hasMoreElements()) {
      keys[i] = keyEnumeration.nextElement();
    }
    assert (i == keys.length);
    Arrays.sort(keys);
    return new ArrayIterator<>(keys);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Set<Entry<Object, Object>> entrySet() {

    SortedSet<Entry<Object, Object>> set = new TreeSet<>((e1, e2) -> ((Comparable) e1.getKey()).compareTo(e2.getKey()));
    set.addAll(super.entrySet());
    return set;
  }

  @Override
  public Set<Object> keySet() {

    return new TreeSet<>(super.keySet());
  }

}
