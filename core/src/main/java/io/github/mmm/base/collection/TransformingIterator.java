package io.github.mmm.base.collection;

import java.util.Iterator;
import java.util.function.Function;

/**
 * Implementation of {@link Iterator} to transform a given {@link Iterator} from a source to a target type. This is like
 * the {@link java.util.stream.Stream#map(java.util.function.Function) map function}. As example we assume we have the
 * following list:
 *
 * <pre>
 * List&lt;Instant> list = List.of(Instant.parse("1999-12-31T23:59:59Z"), Instant.now());
 * </pre>
 *
 * Now we can create a stream and map the Instant's to {@link String}:
 *
 * <pre>
 * Iterator&lt;String> it = list.stream().map(Instant::toString).iterator();
 * </pre>
 *
 * However, we can do the same this way and this works for any {@link Iterator} also in cases where you have no stream
 * support:
 *
 * <pre>
 * Iterator&lt;String> it = new {@link TransformingIterator}(list.iterator(), Instant::toString);
 * </pre>
 *
 * @param <S> source type.
 * @param <T> target type.
 */
public class TransformingIterator<S, T> implements Iterator<T> {

  private final Iterator<S> iterator;

  private final Function<S, T> transformer;

  /**
   * The constructor.
   *
   * @param iterator the {@link Iterator} to transform.
   * @param transformer the {@link Function} to {@link Function#apply(Object) map} from source to target type.
   */
  public TransformingIterator(Iterator<S> iterator, Function<S, T> transformer) {

    super();
    this.iterator = iterator;
    this.transformer = transformer;
  }

  @Override
  public boolean hasNext() {

    return this.iterator.hasNext();
  }

  @Override
  public T next() {

    S next = this.iterator.next();
    if (next == null) {
      return null;
    }
    return this.transformer.apply(next);
  }

  @Override
  public void remove() {

    this.iterator.remove();
    ;
  }

}
