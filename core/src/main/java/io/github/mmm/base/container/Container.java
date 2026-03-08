/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

import io.github.mmm.base.collection.SizedIterable;
import io.github.mmm.base.container.impl.ContainerMapBuilderImplList;
import io.github.mmm.base.container.impl.ContainerMapImplEmpty;
import io.github.mmm.base.lang.ToString;

/**
 * {@link Container} is similar to {@link java.util.Collection} but more lightweight and immutable by design. A
 * {@link Container} cannot contain {@code null} elements.
 *
 * @param <E> type of the contained elements.
 */
public interface Container<E> extends SizedIterable<E>, ToString {

  /**
   * @return the first element of this container.
   */
  E getFirst();

  /**
   * @param <E> type of the contained elements.
   * @param element the single contained element or {@code null} to get an empty container.
   * @return the {@link Container} with the given elements.
   */
  static <E> Container<E> of(E element) {

    return ContainerMap.of(null, element);
  }

  /**
   * @param <E> type of the contained elements.
   * @param elements the array containing the elements.
   * @return the {@link Container} with the given elements.
   */
  @SafeVarargs
  static <E> Container<E> of(E... elements) {

    return ContainerMap.of((Function<E, String>) null, elements);
  }

  /**
   * @param <E> type of the contained elements.
   * @param elements the {@link Iterator} containing the elements.
   * @return the {@link Container} with the given elements.
   */
  static <E> Container<E> of(Iterator<E> elements) {

    return ContainerMap.of((Function<E, String>) null, elements);
  }

  /**
   * @param <E> type of the contained elements.
   * @param elements the {@link Iterable} containing the elements (e.g. {@link java.util.Collection}).
   * @return the {@link Container} with the given elements.
   */
  static <E> Container<E> of(Iterable<E> elements) {

    return ContainerMap.of((Function<E, String>) null, elements);
  }

  /**
   * @param <E> type of the contained elements.
   * @param elements the {@link Stream} containing the elements.
   * @return the {@link Container} with the given elements.
   */
  static <E> Container<E> of(Stream<E> elements) {

    return ContainerMap.of((Function<E, String>) null, elements);
  }

  /**
   * @param <E> type of the contained elements.
   * @return the empty instance of {@link ContainerMap}.
   */
  static <E> Container<E> getEmpty() {

    return ContainerMapImplEmpty.get();
  }

  /**
   * @param <E> type of the contained elements.
   * @return the new {@link ContainerBuilder} instance.
   */
  static <E> ContainerBuilder<E> builder() {

    return new ContainerMapBuilderImplList<>(null);
  }

}
