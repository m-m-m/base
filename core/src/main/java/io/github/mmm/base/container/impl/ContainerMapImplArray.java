/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container.impl;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

import io.github.mmm.base.collection.ArrayIterator;
import io.github.mmm.base.container.ContainerMap;

/**
 * Implementation of {@link ContainerMap} that is always {@link #isEmpty() empty}.
 *
 * @param <E> type of the contained elements.
 * @since 1.0.0
 */
public final class ContainerMapImplArray<E> extends ContainerMapImpl<E> {

  private final E[] elements;

  /**
   * The constructor.
   *
   * @param nameFunction the {@link Function} to get the name of the element for {@link #get(String)}. May be
   *        {@code null} to not support this.
   * @param elements the elements contained in this container.
   */
  @SafeVarargs
  public ContainerMapImplArray(Function<E, String> nameFunction, E... elements) {

    super(nameFunction);
    Objects.requireNonNull(elements);
    if (elements.length < 2) {
      throw new IllegalStateException();
    }
    this.elements = elements;
  }

  @Override
  public E getFirst() {

    return this.elements[0];
  }

  @Override
  public E get(String name) {

    if (this.nameFunction != null) {
      for (E element : this.elements) {
        if (Objects.equals(this.nameFunction.apply(element), name)) {
          return element;
        }
      }
    }
    return null;
  }

  @Override
  public int getSize() {

    return this.elements.length;
  }

  @Override
  public Iterator<E> iterator() {

    return new ArrayIterator<>(this.elements);
  }

}
