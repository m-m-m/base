/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
public final class ContainerMapBuilderImplList<E> implements ContainerMapBuilder<E> {

  private final Function<E, String> nameFunction;

  private E first;

  private int firstIndex;

  private List<E> list;

  /**
   * The constructor.
   *
   * @param nameFunction the {@link Function} to get the name of the element for {@link ContainerMap#get(String)}.
   */
  public ContainerMapBuilderImplList(Function<E, String> nameFunction) {

    super();
    this.nameFunction = nameFunction;
  }

  @Override
  public ContainerMapBuilderImplList<E> add(E element, int index) {

    Objects.requireNonNull(element);
    if (this.list == null) {
      if (this.first == null) {
        this.first = element;
        this.firstIndex = index;
        return this;
      }
      this.list = new ArrayList<>();
      addInternal(this.first, this.firstIndex);
      this.first = null;
    }
    addInternal(element, index);
    return this;
  }

  private void addInternal(E element, int index) {

    if ((index == -1) || (index == this.list.size())) {
      this.list.add(element);
    } else {
      int delta = this.list.size() - index;
      if (delta > 32) {
        throw new IllegalStateException("" + delta);
      }
      while (delta > 0) {
        this.list.add(null);
        delta--;
      }
      this.list.add(index, element);
    }
  }

  @Override
  public int getSize() {

    if (this.list != null) {
      return this.list.size();
    } else if (this.first != null) {
      return 1;
    }
    return 0;
  }

  @Override
  public ContainerMap<E> build() {

    if (this.list == null) {
      return ContainerMap.of(this.nameFunction, this.first);
    } else {
      assert (!this.list.contains(null));
      return ContainerMap.of(this.nameFunction, this.list);
    }
  }

  @Override
  public Iterator<E> iterator() {

    if (this.list != null) {
      return this.list.iterator();
    } else if (this.first != null) {
      return new SingleElementIterator<>(this.first);
    }
    return Collections.emptyIterator();
  }

}
