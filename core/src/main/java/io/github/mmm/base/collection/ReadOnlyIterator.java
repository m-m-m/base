/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.collection;

import java.util.Iterator;
import java.util.Objects;

/**
 * Implementation of {@link Iterator} as a read-only view on an existing {@link Iterator} instance.
 *
 * @param <E> type of the elements to iterate.
 * @since 1.0.0
 */
public class ReadOnlyIterator<E> implements Iterator<E> {

  private final Iterator<E> delegate;

  /**
   * The constructor.
   *
   * @param iterator is the {@link Iterator} to adapt.
   */
  public ReadOnlyIterator(Iterator<E> iterator) {

    super();
    Objects.requireNonNull(iterator);
    this.delegate = iterator;
  }

  @Override
  public boolean hasNext() {

    return this.delegate.hasNext();
  }

  @Override
  public E next() {

    return this.delegate.next();
  }

  @Override
  public String toString() {

    return this.delegate.toString();
  }

}