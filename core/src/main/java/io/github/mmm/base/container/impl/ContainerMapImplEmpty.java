/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container.impl;

import java.util.Collections;
import java.util.Iterator;

import io.github.mmm.base.container.ContainerMap;
import io.github.mmm.base.lang.AbstractToString;

/**
 * Implementation of {@link ContainerMap} that is always {@link #isEmpty() empty}.
 *
 * @param <E> type of the contained elements.
 * @since 1.0.0
 */
public final class ContainerMapImplEmpty<E> extends AbstractToString implements ContainerMap<E> {

  private static final ContainerMapImplEmpty<Object> INSTANCE = new ContainerMapImplEmpty<>();

  private ContainerMapImplEmpty() {

    super();
  }

  @Override
  public E getFirst() {

    return null;
  }

  @Override
  public E get(String name) {

    return null;
  }

  @Override
  public int getSize() {

    return 0;
  }

  @Override
  public Iterator<E> iterator() {

    return Collections.emptyIterator();
  }

  @Override
  public void toString(StringBuilder sb, int mode) {

  }

  /**
   * @param <E> type of the contained elements.
   * @return the singleton instance of {@link ContainerMapImplEmpty}.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <E> ContainerMapImplEmpty<E> get() {

    return (ContainerMapImplEmpty) INSTANCE;
  }

}
