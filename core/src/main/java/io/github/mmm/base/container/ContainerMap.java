/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import io.github.mmm.base.container.impl.ContainerMapBuilderImplList;
import io.github.mmm.base.container.impl.ContainerMapBuilderImplMap;
import io.github.mmm.base.container.impl.ContainerMapImplArray;
import io.github.mmm.base.container.impl.ContainerMapImplEmpty;
import io.github.mmm.base.container.impl.ContainerMapImplMap;
import io.github.mmm.base.container.impl.ContainerMapImplSingle;

/**
 * Extends {@link Container} with ability to {@link #get(String) get elements by name}.
 *
 * @param <E> type of the contained elements.
 */
public interface ContainerMap<E> extends Container<E> {

  /**
   * @param name the name or key of the requested element from this container.
   * @return the requested element or {@code null} if not found.
   */
  E get(String name);

  /**
   * @param <E> type of the contained elements.
   * @param nameFunction the {@link Function} to get the name of the element for {@link #get(String)}. May be
   *        {@code null} to not support this.
   * @param element the single contained element or {@code null} to get an empty container.
   * @return the {@link Container} with the given elements.
   */
  static <E> ContainerMap<E> of(Function<E, String> nameFunction, E element) {

    if (element == null) {
      return ContainerMapImplEmpty.get();
    }
    return new ContainerMapImplSingle<>(nameFunction, element);
  }

  /**
   * @param <E> type of the contained elements.
   * @param nameFunction the {@link Function} to get the name of the element for {@link #get(String)}. May be
   *        {@code null} to not support this.
   * @param elements the array containing the elements.
   * @return the {@link Container} with the given elements.
   */
  @SafeVarargs
  static <E> ContainerMap<E> of(Function<E, String> nameFunction, E... elements) {

    if ((elements == null) || (elements.length == 0)) {
      return ContainerMapImplEmpty.get();
    } else if (elements.length == 1) {
      return new ContainerMapImplSingle<>(nameFunction, elements[0]);
    }
    return new ContainerMapImplArray<>(nameFunction, elements);
  }

  /**
   * @param <E> type of the contained elements.
   * @param nameFunction the {@link Function} to get the name of the element for {@link #get(String)}. May be
   *        {@code null} to not support this.
   * @param elements the {@link Iterator} containing the elements.
   * @return the {@link Container} with the given elements.
   */
  @SuppressWarnings("unchecked")
  static <E> ContainerMap<E> of(Function<E, String> nameFunction, Iterator<E> elements) {

    if (elements.hasNext()) {
      E element = elements.next();
      if (elements.hasNext()) {
        List<E> list = new ArrayList<>();
        list.add(element);
        do {
          list.add(elements.next());
        } while (elements.hasNext());
        Object[] array = list.toArray(Object[]::new);
        return new ContainerMapImplArray<>(nameFunction, (E[]) array);
      } else {
        return new ContainerMapImplSingle<>(nameFunction, element);
      }
    } else {
      return ContainerMapImplEmpty.get();
    }
  }

  /**
   * @param <E> type of the contained elements.
   * @param nameFunction the {@link Function} to get the name of the element for {@link #get(String)}. May be
   *        {@code null} to not support this.
   * @param elements the {@link Iterable} containing the elements (e.g. {@link java.util.Collection}).
   * @return the {@link Container} with the given elements.
   */
  static <E> ContainerMap<E> of(Function<E, String> nameFunction, Iterable<E> elements) {

    return of(nameFunction, elements.iterator());
  }

  /**
   * @param <E> type of the contained elements.
   * @param nameFunction the {@link Function} to get the name of the element for {@link #get(String)}. May be
   *        {@code null} to not support this.
   * @param elements the {@link Stream} containing the elements.
   * @return the {@link Container} with the given elements.
   */
  static <E> ContainerMap<E> of(Function<E, String> nameFunction, Stream<E> elements) {

    return of(nameFunction, elements.iterator());
  }

  /**
   * @param <E> type of the contained elements.
   * @param nameFunction the {@link Function} to get the name of the element for {@link #get(String)}. May be
   *        {@code null} to not support this.
   * @param elements the {@link Iterable} containing the elements (e.g. {@link java.util.Collection}).
   * @return the {@link Container} with the given elements.
   */
  static <E> ContainerMap<E> of(Function<E, String> nameFunction, Map<String, E> elements) {

    int size = elements.size();
    if (size == 0) {
      return ContainerMapImplEmpty.get();
    } else if (size == 1) {
      Entry<String, E> entry = elements.entrySet().iterator().next();
      assert entry.getKey().equals(nameFunction.apply(entry.getValue()));
      return new ContainerMapImplSingle<>(nameFunction, entry.getValue());
    }
    return new ContainerMapImplMap<>(nameFunction, elements);
  }

  /**
   * @param <E> type of the contained elements.
   * @return the empty instance of {@link ContainerMap}.
   */
  static <E> ContainerMap<E> getEmpty() {

    return ContainerMapImplEmpty.get();
  }

  /**
   * @param <E> type of the contained elements.
   * @param nameFunction the {@link Function} to get the name of the element for {@link #get(String)}. May be
   *        {@code null} to not support this.
   * @param map - {@code true} to build using an underlying map, {@code false} otherwise (use a list).
   * @return the new {@link ContainerMapBuilder}.
   */
  static <E> ContainerMapBuilder<E> builder(Function<E, String> nameFunction, boolean map) {

    Objects.requireNonNull(nameFunction);
    if (map) {
      return new ContainerMapBuilderImplMap<>(nameFunction);
    } else {
      return new ContainerMapBuilderImplList<>(nameFunction);
    }
  }

}
