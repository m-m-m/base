/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.collection;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of both {@link Iterator} and {@link Enumeration} that {@link #next() iterates} only a single element.
 *
 * @param <E> type of the element to iterate.
 * @since 1.0.0
 */
public class SingleElementIterator<E> implements Enumeration<E>, Iterator<E> {

  private E element;

  /**
   * The constructor.
   *
   * @param element the single element to iterate. If {@code null} the iterator will be empty.
   */
  public SingleElementIterator(E element) {

    super();
    this.element = element;
  }

  @Override
  public boolean hasMoreElements() {

    return hasNext();
  }

  @Override
  public boolean hasNext() {

    return this.element != null;
  }

  @Override
  public E nextElement() {

    return next();
  }

  @Override
  public E next() {

    if (this.element == null) {
      throw new NoSuchElementException();
    }
    E result = this.element;
    this.element = null;
    return result;
  }

}
