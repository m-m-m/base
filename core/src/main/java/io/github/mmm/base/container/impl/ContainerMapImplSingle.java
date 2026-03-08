/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container.impl;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

import io.github.mmm.base.collection.SingleElementIterator;
import io.github.mmm.base.container.ContainerMap;

/**
 * Implementation of {@link ContainerMap} that is always {@link #isEmpty() empty}.
 *
 * @param <E> type of the contained elements.
 * @since 1.0.0
 */
public final class ContainerMapImplSingle<E> extends ContainerMapImpl<E> {

  private final E element;

  private final String elementName;

  /**
   * The constructor.
   *
   * @param nameFunction the {@link Function} to get the name of the element for {@link #get(String)}. May be
   *        {@code null} to not support this.
   * @param element the single element container in this container.
   */
  public ContainerMapImplSingle(Function<E, String> nameFunction, E element) {

    super(nameFunction);
    Objects.requireNonNull(element);
    this.element = element;
    if (nameFunction == null) {
      this.elementName = null;
    } else {
      this.elementName = nameFunction.apply(element);
      assert (this.elementName != null);
    }
  }

  @Override
  public E getFirst() {

    return this.element;
  }

  @Override
  public E get(String name) {

    if ((this.elementName != null) && this.elementName.equals(name)) {
      return this.element;
    }
    return null;
  }

  @Override
  public int getSize() {

    return 1;
  }

  @Override
  public Iterator<E> iterator() {

    return new SingleElementIterator<>(this.element);
  }

}
