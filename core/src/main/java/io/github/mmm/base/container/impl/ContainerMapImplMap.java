/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import io.github.mmm.base.collection.ReadOnlyIterator;
import io.github.mmm.base.container.ContainerMap;

/**
 * Implementation of {@link ContainerMap} that is always {@link #isEmpty() empty}.
 *
 * @param <E> type of the contained elements.
 * @since 1.0.0
 */
public final class ContainerMapImplMap<E> extends ContainerMapImpl<E> {

  private final Map<String, E> map;

  /**
   * The constructor.
   *
   * @param nameFunction the {@link Function} to get the name of the element for {@link #get(String)}. May be
   *        {@code null} to not support this.
   * @param map the {@link Map} containing the elements by name.
   */
  public ContainerMapImplMap(Function<E, String> nameFunction, Map<String, E> map) {

    super(nameFunction);
    Objects.requireNonNull(map);
    if (map.size() < 2) {
      throw new IllegalStateException();
    }
    this.map = map;
  }

  @Override
  public E getFirst() {

    return this.map.values().iterator().next();
  }

  @Override
  public E get(String name) {

    return this.map.get(name);
  }

  @Override
  public int getSize() {

    return this.map.size();
  }

  @Override
  public Iterator<E> iterator() {

    return new ReadOnlyIterator<>(this.map.values().iterator());
  }

}
