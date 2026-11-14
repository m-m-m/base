/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.lang;

/// Interface to [parse][#parse(String)] a [String] in an expected syntax to a specific type.
///
/// @param <T> type of the handled type to parse.
/// @since 1.0.0
public interface FromStringParser<T> {

  /// Parses the given [String] to the type handled by this parser but efficiently returns `null` if the
  /// syntax does not match. This allows to detect the format without wasting much performance.
  ///
  /// @param string the [String] to parse. May or may not be in the format expected for the type of this parser.
  /// @return the given [String] parsed to the type handled by this parser or `null` if the syntax does not
  ///         match.
  T parse(String string);

}
