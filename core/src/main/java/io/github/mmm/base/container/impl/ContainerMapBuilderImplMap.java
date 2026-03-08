/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;

import io.github.mmm.base.collection.SingleElementIterator;
import io.github.mmm.base.container.ContainerMap;
import io.github.mmm.base.container.ContainerMapBuilder;
import io.github.mmm.base.lang.Builder;

/**
 * Implementation of {@link Builder} for {@link ContainerMap} based on {@link Map}.
 *
 * @param <E> type of container elements.
 *
 * @since 1.0.0
 */
public final class ContainerMapBuilderImplMap<E> implements ContainerMapBuilder<E> {

  private final Function<E, String> nameFunction;

  private E first;

  private Map<String, E> map;

  /**
   * The constructor.
   *
   * @param nameFunction the {@link Function} to get the name of the element for {@link ContainerMap#get(String)}.
   */
  public ContainerMapBuilderImplMap(Function<E, String> nameFunction) {

    super();
    Objects.requireNonNull(nameFunction);
    this.nameFunction = nameFunction;
  }

  @Override
  public ContainerMapBuilderImplMap<E> add(E element, int index) {

    if (this.map == null) {
      if (this.first == null) {
        this.first = element;
      } else {
        this.map = new TreeMap<>();
        put(this.first);
        this.first = null;
      }
    }
    if (this.map != null) {
      put(element);
    }
    return this;
  }

  private void put(E element) {

    this.map.put(this.nameFunction.apply(element), element);
  }

  @Override
  public int getSize() {

    if (this.map != null) {
      return this.map.size();
    } else if (this.first != null) {
      return 1;
    }
    return 0;
  }

  @Override
  public ContainerMap<E> build() {

    if (this.map == null) {
      return ContainerMap.of(this.nameFunction, this.first);
    } else {
      return ContainerMap.of(this.nameFunction, this.map);
    }
  }

  @Override
  public Iterator<E> iterator() {

    if (this.map != null) {
      return this.map.values().iterator();
    } else if (this.first != null) {
      return new SingleElementIterator<>(this.first);
    }
    return Collections.emptyIterator();
  }

}
