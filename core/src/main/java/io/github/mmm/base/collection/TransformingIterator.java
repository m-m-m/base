package io.github.mmm.base.collection;

import java.util.Iterator;
import java.util.function.Function;

/// Implementation of [Iterator] to transform a given [Iterator] from a source to a target type. This is like
/// the [map function][java.util.stream.Stream#map(java.util.function.Function)]. As example we assume we have the
/// following list:
///
/// ```
/// List<Instant> list = List.of(Instant.parse("1999-12-31T23:59:59Z"), Instant.now());
/// ```
///
/// Now we can create a stream and map the Instant's to [String]:
///
/// ```
/// Iterator<String> it = list.stream().map(Instant::toString).iterator();
/// ```
///
/// However, we can do the same this way and this works for any [Iterator] also in cases where you have no stream
/// support:
///
/// ```
/// Iterator<String> it = new [TransformingIterator](list.iterator(), Instant::toString);
/// ```
///
/// @param <S> source type.
/// @param <T> target type.
public class TransformingIterator<S, T> implements Iterator<T> {

  private final Iterator<S> iterator;

  private final Function<S, T> transformer;

  /// The constructor.
  ///
  /// @param iterator the [Iterator] to transform.
  /// @param transformer the [Function] to [map][Function#apply(Object)] from source to target type.
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
