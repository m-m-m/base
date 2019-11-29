/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.impl;

import java.util.Iterator;

import io.github.mmm.base.lang.Composable;

/**
 * {@link Iterator} for {@link Composable}.
 *
 * @param <C> type of iterated children.
 *
 * @since 1.0.0
 */
public class ComposableIterator<C> implements Iterator<C> {

  private final Composable<C> composable;

  private int index;

  /**
   * The constructor.
   * 
   * @param composable the {@link Composable} to iterate.
   */
  public ComposableIterator(Composable<C> composable) {

    super();
    this.composable = composable;
    this.index = 0;
  }

  @Override
  public boolean hasNext() {

    return (this.index < this.composable.getChildCount());
  }

  @Override
  public C next() {

    C result = this.composable.getChild(this.index);
    this.index++;
    return result;
  }

}
