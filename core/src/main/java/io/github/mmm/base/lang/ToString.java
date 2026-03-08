/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.lang;

/**
 * Interface for an object with advanced {@link #toString(StringBuilder)} support. Implementations should implement
 * {@link #toString()} by delegation to {@link #toString(StringBuilder)} and declare the method as final.
 *
 * @since 1.0.0
 */
public interface ToString {

  /**
   * @param sb the {@link StringBuilder} where to append the {@link #toString() string-representation}.
   */
  default void toString(StringBuilder sb) {

    toString(sb, 0);
  }

  /**
   * @param sb the {@link StringBuilder} where to append the {@link #toString() string-representation}.
   * @param mode an mode of the string format. The value {@code 0} represents the standard {@link #toString()} format.
   *        Other modes e.g. with different levels of detail may or may not be supported. Unsupported modes may be
   *        ignored or asserted but should not cause an error without assertions for robustness but fallback to the
   *        default ({@code 0}). Implementations using this specific feature may define an {@link Enum} explaining
   *        supported modes and use their {@link Enum#ordinal() ordinal} as mode value.
   */
  void toString(StringBuilder sb, int mode);
}
