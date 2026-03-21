/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.mmm.base.lang.Builder;

/**
 * {@link Builder} to {@link #build()} an immutable {@link List} efficiently and easy.
 *
 * @see #add(Object)
 *
 * @param <E> type of the elements to be contained in the {@link List}.
 */
public class ListBuilder<E> implements Builder<List<E>> {

  private E first;

  private int firstIndex;

  private List<E> list;

  /**
   * @param element the element to add to the container.
   * @return this builder for fluent API calls.
   * @see #add(Object, int)
   */
  public ListBuilder<E> add(E element) {

    return add(element, -1);
  }

  /**
   * Use this method with care and ensure that no gaps remain before calling {@link #build()}.
   *
   * @param element the element to add to the container.
   * @param index the index where to add the child or {@code -1} to append.
   * @return this builder for fluent API calls.
   * @see #add(Object)
   */
  public ListBuilder<E> add(E element, int index) {

    Objects.requireNonNull(element);
    if (this.list == null) {
      if (this.first == null) {
        this.first = element;
        this.firstIndex = index;
        return this;
      }
      this.list = new ArrayList<>();
      addInternal(this.first, this.firstIndex);
      this.first = null;
    }
    addInternal(element, index);
    return this;
  }

  private void addInternal(E element, int index) {

    if ((index == -1) || (index == this.list.size())) {
      this.list.add(element);
    } else {
      int delta = this.list.size() - index;
      if (delta > 32) {
        throw new IllegalStateException("" + delta);
      }
      while (delta > 0) {
        this.list.add(null);
        delta--;
      }
      this.list.add(index, element);
    }
  }

  /**
   * @return the current size of the {@link List} to build.
   * @see List#size()
   */
  public int size() {

    if (this.list != null) {
      return this.list.size();
    } else if (this.first != null) {
      return 1;
    }
    return 0;
  }

  /**
   * @return {@code true} if empty, {@code false} otherwise.
   */
  public boolean isEmpty() {

    return size() == 0;
  }

  @Override
  public List<E> build() {

    if (this.list == null) {
      if (this.first == null) {
        return List.of();
      }
      assert (this.firstIndex <= 0);
      return List.of(this.first);
    } else {
      assert (!this.list.contains(null));
      return List.copyOf(this.list);
    }
  }

  @Override
  public String toString() {

    if (this.list == null) {
      if (this.first == null) {
        return "";
      }
      return this.first.toString();
    }
    return this.list.toString();
  }
}
