/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container;

import io.github.mmm.base.collection.SizedIterable;
import io.github.mmm.base.lang.Builder;

/**
 * {@link Builder} for {@link Container}.
 *
 * @param <E> type of container elements.
 *
 * @since 1.0.0
 */
public interface ContainerBuilder<E> extends Builder<Container<E>>, SizedIterable<E> {

  /**
   * @param element the element to add to the container.
   * @return this builder for fluent API calls.
   */
  default ContainerBuilder<E> add(E element) {

    return add(element, -1);
  }

  /**
   * Use this method with care and ensure that no gaps remain before calling {@link #build()}.
   *
   * @param element the element to add to the container.
   * @param index the index where to add the child or {@code -1} to append.
   * @return this builder for fluent API calls.
   */
  ContainerBuilder<E> add(E element, int index);

}
