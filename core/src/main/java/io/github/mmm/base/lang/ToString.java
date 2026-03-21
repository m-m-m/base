/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.lang;

import io.github.mmm.base.io.UncheckedAppendable;

/**
 * Interface for an object with advanced {@link #toString(UncheckedAppendable)} support. This also allows to implement
 * {@link #toString()} behaviour already in interfaces as default method. Further, it reduces waste of resources
 * avoiding to create {@link String} instances with underlying {@code char} arrays only for the purpose to append that
 * again to a {@link StringBuilder} of a higher-level object composed out of it.<br>
 * Implementations should implement {@link #toString()} by delegation to {@link #toString(UncheckedAppendable)} and
 * declare the method as final. The easiest way to to extend {@link AbstractToString}. If not possible, simply copy the
 * pattern to your class.
 *
 * @see AbstractToString
 * @since 1.0.0
 */
public interface ToString {

  /**
   * @param sb the {@link UncheckedAppendable} where to append the {@link #toString() string-representation}.
   */
  default void toString(UncheckedAppendable sb) {

    toString(sb, 0);
  }

  /**
   * @param sb the {@link UncheckedAppendable} where to append the {@link #toString() string-representation}.
   * @param mode the mode of the string format. The value {@code 0} represents the standard {@link #toString()} format.
   *        Other modes e.g. with different levels of detail may or may not be supported. Unsupported modes may be
   *        ignored or asserted but should not cause an error without assertions for robustness but fallback to the
   *        default ({@code 0}). Implementations using this specific feature may define an {@link Enum} explaining
   *        supported modes and use their {@link Enum#ordinal() ordinal} as mode value.
   */
  void toString(UncheckedAppendable sb, int mode);
}
