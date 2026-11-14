/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.io;

import io.github.mmm.base.impl.UncheckedAppendableWrapper;

/// [Appendable] without checked [java.io.IOException] anti-pattern in signature for advanced usage.
///
/// @since 1.0.0
public interface UncheckedAppendable extends Appendable {

  @Override
  UncheckedAppendable append(CharSequence csq);

  @Override
  UncheckedAppendable append(CharSequence csq, int start, int end);

  @Override
  UncheckedAppendable append(char c);

  /// @param o the object to append as [string representation][Object#toString()].
  /// @return this [UncheckedAppendable] for fluent API calls.
  default UncheckedAppendable append(Object o) {

    return append(String.valueOf(o));
  }

  /// @return the wrapped [Appendable] instance or `this` instance itself if not wrapped.
  Appendable unwrap();

  /// @return a new [UncheckedAppendable] acting like [StringBuilder].
  static UncheckedAppendable of() {

    return new UncheckedAppendableWrapper(new StringBuilder());
  }

  /// @param capacity the initial [capacity][StringBuilder#capacity()] of the underlying [StringBuilder].
  /// @return a new [UncheckedAppendable] acting like [StringBuilder].
  static UncheckedAppendable of(int capacity) {

    return new UncheckedAppendableWrapper(new StringBuilder(capacity));
  }

  /// @param appendable the [Appendable] to wrap.
  /// @return a new [UncheckedAppendable] that delegates to the given [Appendable].
  static UncheckedAppendable of(Appendable appendable) {

    return new UncheckedAppendableWrapper(appendable);
  }
}
