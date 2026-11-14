/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.lang;

import io.github.mmm.base.io.UncheckedAppendable;

/// Interface for an object with advanced [#toString(UncheckedAppendable)] support. This also allows to implement
/// [#toString()] behaviour already in interfaces as default method. Further, it reduces waste of resources
/// avoiding to create [String] instances with underlying `char` arrays only for the purpose to append that
/// again to a [StringBuilder] of a higher-level object composed out of it.
/// Implementations should implement [#toString()] by delegation to [#toString(UncheckedAppendable)] and
/// declare the method as final. The easiest way to to extend [AbstractToString]. If not possible, simply copy the
/// pattern to your class.
///
/// @see AbstractToString
/// @since 1.0.0
public interface ToString {

  /// @param sb the [UncheckedAppendable] where to append the [string-representation][#toString()].
  default void toString(UncheckedAppendable sb) {

    toString(sb, 0);
  }

  /// @param sb the [UncheckedAppendable] where to append the [string-representation][#toString()].
  /// @param mode the mode of the string format. The value `0` represents the standard [#toString()] format.
  ///        Other modes e.g. with different levels of detail may or may not be supported. Unsupported modes may be
  ///        ignored or asserted but should not cause an error without assertions for robustness but fallback to the
  ///        default (`0`). Implementations using this specific feature may define an [Enum] explaining
  ///        supported modes and use their [ordinal][Enum#ordinal()] as mode value.
  void toString(UncheckedAppendable sb, int mode);
}
