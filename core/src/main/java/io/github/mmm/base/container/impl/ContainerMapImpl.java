/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container.impl;

import java.util.Iterator;
import java.util.function.Function;

import io.github.mmm.base.container.ContainerMap;
import io.github.mmm.base.lang.AbstractToString;
import io.github.mmm.base.lang.ToString;

/**
 * Abstract base implementation of {@link ContainerMap}.
 *
 * @param <E> type of the contained elements.
 * @since 1.0.0
 */
public abstract class ContainerMapImpl<E> extends AbstractToString implements ContainerMap<E> {

  /** @see #get(String) */
  protected final Function<E, String> nameFunction;

  /**
   * The constructor.
   *
   * @param nameFunction the {@link Function} to get the name of the element for {@link #get(String)}. May be
   *        {@code null} to not support this.
   */
  public ContainerMapImpl(Function<E, String> nameFunction) {

    super();
    this.nameFunction = nameFunction;
  }

  @Override
  public void toString(StringBuilder sb, int mode) {

    boolean first = true;
    Iterator<E> iterator = iterator();
    while (iterator.hasNext()) {
      E child = iterator.next();
      if (first) {
        first = false;
      } else {
        sb.append(',');
      }
      if (child instanceof ToString childToString) {
        childToString.toString(sb, mode);
      } else if (this.nameFunction == null) {
        sb.append(child);
      } else {
        sb.append(this.nameFunction.apply(child));
      }
    }
  }

}
