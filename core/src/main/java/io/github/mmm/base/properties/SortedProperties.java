/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.properties;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class extends {@link Properties} and makes them sorted by their {@link #keys() keys}. Sorting the properties is
 * useful when changes shall be compared using diff-algorithms.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 4.0.0
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
    return new ArrayEnumeration<>(keys);
  }

  @Override
  public Set<Object> keySet() {

    return new TreeSet<>(super.keySet());
  }

  private static class ArrayEnumeration<E> implements Enumeration<E> {

    private final E[] array;

    private int i = 0;

    public ArrayEnumeration(E[] array) {

      super();
      this.array = array;
    }

    @Override
    public boolean hasMoreElements() {

      return (this.i < this.array.length);
    }

    @Override
    public E nextElement() {

      return this.array[this.i++];
    }
  }
}
