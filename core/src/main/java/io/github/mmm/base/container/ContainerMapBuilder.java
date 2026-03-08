/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container;

import io.github.mmm.base.lang.Builder;

/**
 * {@link Builder} for {@link Container}.
 *
 * @param <E> type of container elements.
 *
 * @since 1.0.0
 */
public interface ContainerMapBuilder<E> extends ContainerBuilder<E> {

  @Override
  default ContainerMapBuilder<E> add(E element) {

    return add(element, -1);
  }

  @Override
  ContainerMapBuilder<E> add(E element, int index);

  @Override
  ContainerMap<E> build();

}
