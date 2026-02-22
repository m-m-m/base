/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.collection;

import java.util.Collection;
import java.util.List;

import io.github.mmm.base.sort.SortOrder;

/**
 * Simple helper for {@link Collection} specific stuff.
 *
 * @since 1.0.0
 */
public final class CollectionHelper {

  private CollectionHelper() {

  }

  /**
   * @param element the item to {@link List#add(int, Object) add} to the {@link List} preserving the sort order. Must
   *        not be {@code null}.
   * @param sortedList the {@link List} already sorted in the given {@link SortOrder}.
   * @param <E> type of the {@link List} elements.
   * @return the index where the new value has been inserted, or a negative value if the given {@code element} is
   *         already {@link List#contains(Object) contained} in the given {@link List}.
   */
  public static <E extends Comparable<? super E>> int sortedInsert(E element, List<E> sortedList) {

    return sortedInsert(element, sortedList, SortOrder.ASC, true);
  }

  /**
   * @param element the item to {@link List#add(int, Object) add} to the {@link List} preserving the sort order. Must
   *        not be {@code null}.
   * @param sortedList the {@link List} already sorted in the given {@link SortOrder}.
   * @param order the {@link SortOrder}.
   * @param unique - {@code true} to prevent duplicates, {@code false} otherwise.
   * @param <E> type of the {@link List} elements.
   * @return the index where the new value has been inserted, or a negative value if the given {@code element} is
   *         already {@link List#contains(Object) contained} in the given {@link List} and {@code true} was given for
   *         {@code unique}.
   *
   */
  public static <E extends Comparable<? super E>> int sortedInsert(E element, List<E> sortedList, SortOrder order,
      boolean unique) {

    // binary search for sorted insert
    int low = 0;
    int high = sortedList.size() - 1;
    while (low <= high) {
      int mid = (low + high) >>> 1;
      E midVal = sortedList.get(mid);
      int cmp = midVal.compareTo(element);
      if (order == SortOrder.DESC) {
        cmp = -cmp;
      }
      if (cmp < 0) {
        low = mid + 1;
      } else if (cmp > 0) {
        high = mid - 1;
      } else {
        if (unique) {
          return -(low + 1);
        }
        break;
      }
    }
    sortedList.add(low, element);
    return low;
  }
}
