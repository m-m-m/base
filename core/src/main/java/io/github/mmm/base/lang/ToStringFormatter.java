/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.lang;

import java.util.Objects;
import java.util.function.Function;

/// Formatter [Function] that delegates to [Object#toString()]. Use [this formatter][#get()] as default
/// with option to override in order to give flexiblity.
///
/// @param <V> type of the value to [format][#apply(Object)].
/// @since 1.0.0
public final class ToStringFormatter<V> implements Function<V, String> {

  @SuppressWarnings("rawtypes")
  private static final ToStringFormatter INSTANCE = new ToStringFormatter<>();

  private ToStringFormatter() {

    super();
  }

  @Override
  public String apply(V t) {

    return Objects.toString(t);
  }

  /// @param <V> type of the value to [format][#apply(Object)].
  /// @return the singleton instance of [ToStringFormatter].
  public static <V> ToStringFormatter<V> get() {

    return INSTANCE;
  }

}
