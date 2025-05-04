package io.github.mmm.base.collection;

import java.util.function.IntFunction;

/**
 * Interface for a light-weigth {@link java.util.Collection}.
 *
 * @param <T> type of the contained elements.
 */
public interface SizedIterable<T> extends Iterable<T> {

  /**
   * @return the number of {@link #iterator() contained} elements.
   */
  int getSize();

  /**
   * @return {@code true} if empty, {@code false} otherwise.
   */
  default boolean isEmpty() {

    return getSize() == 0;
  }

  /**
   * @param arrayFactory the {@link IntFunction} to {@link IntFunction#apply(int) create} the array.
   * @return the array containing all elements of this {@link SizedIterable}.
   */
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
