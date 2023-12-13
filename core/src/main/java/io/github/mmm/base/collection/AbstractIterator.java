package io.github.mmm.base.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Abstract base implementation of {@link Iterator} as reusable pattern to simplify implementations of {@link Iterator}.
 * Please carefully consider the following constraints before use:
 * <ul>
 * <li>This {@link Iterator} cannot iterate {@code null} elements.</li>
 * <li>Implementations extending {@link AbstractIterator} have to call {@link #findFirst()} at the end of their
 * constructor.</li>
 * <li>Finally, you only to do implement {@link #findNext()}.</li>
 * </ul>
 *
 * @param <E> type of the elements to {@link #next() iterate}.
 */
public abstract class AbstractIterator<E> implements Iterator<E> {

  /** @see #next() */
  private E next;

  /**
   * Has to be called from the constructor after initialization of fields to find the first element.
   */
  protected final void findFirst() {

    this.next = findNext();
  }

  /**
   * @return the next element or {@code null} if done.
   */
  protected abstract E findNext();

  @Override
  public final boolean hasNext() {

    return this.next != null;
  }

  @Override
  public final E next() {

    if (this.next == null) {
      throw new NoSuchElementException();
    } else {
      E result = this.next;
      this.next = findNext();
      return result;
    }
  }

  @Override
  public String toString() {

    return "Iterator at " + this.next;
  }

}
