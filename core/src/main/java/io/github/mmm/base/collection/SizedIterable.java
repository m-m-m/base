package io.github.mmm.base.collection;

import java.util.function.IntFunction;

/// Interface for a light-weigth [java.util.Collection].
///
/// @param <T> type of the contained elements.
public interface SizedIterable<T> extends Iterable<T> {

  /// @return the number of [contained][#iterator()] elements.
  int getSize();

  /// @return `true` if empty, `false` otherwise.
  default boolean isEmpty() {

    return getSize() == 0;
  }

  /// @param arrayFactory the [IntFunction] to [create][IntFunction#apply(int)] the array.
  /// @return the array containing all elements of this [SizedIterable].
  default T[] toArray(IntFunction<T[]> arrayFactory) {

    int size = getSize();
    T[] array = arrayFactory.apply(size);
    assert (array.length == size);
    int i = 0;
    for (T element : this) {
      array[i++] = element;
    }
    assert (i == size);
    return array;
  }

}
