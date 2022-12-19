/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.lang;

import java.util.Iterator;

import io.github.mmm.base.impl.ComposableIterator;

/**
 * Interface for an object that may contain {@link #getChild(int) children}.
 *
 * @param <C> type of the children. Should typically be the sub-type implementing this interface itself.
 * @since 1.0.0
 */
public interface Composable<C> extends Iterable<C> {

  /**
   * @return the number of contained {@link #getChild(int) children}.
   * @see #getChild(int)
   * @see java.util.Collection#size()
   */
  int getChildCount();

  /**
   * Gets the child-object at the given {@code index}.
   *
   * @param index is the index of the child to get.
   * @return the requested child or {@code null} if no such child exists because the given {@code index} is not in the
   *         range from {@code 0} to <code>{@link #getChildCount() child-count} - 1</code>.
   * @see java.util.List#get(int)
   */
  C getChild(int index);

  @Override
  default Iterator<C> iterator() {

    return new ComposableIterator<>(this);
  }

}
