/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.io;

import java.io.IOException;

/// Interface for a streaming processor that reads or writes data.
///
/// @param <S> type of the stream such as [java.io.InputStream], [java.io.OutputStream],
///        [java.io.Reader], [java.io.Writer].
/// @param <R> the result of the processing. Use [Void] and return `null` if not needed.
public interface IoProcessor<S, R> {

  /// @param stream the stream or reader/writer to process.
  /// @return the result of the processing. May be `null`.
  /// @throws Exception if something went wrong (typically an [IOException]).
  R process(S stream) throws Exception;

}
