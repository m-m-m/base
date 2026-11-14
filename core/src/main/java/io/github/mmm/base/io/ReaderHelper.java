/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.io;

import java.io.IOException;
import java.io.Reader;

import io.github.mmm.base.exception.RuntimeIoException;

/// Simple helper class for dealing with [Reader]. With newer unicode a single code-point may be composed out of
/// two `char` values (surrogates). The [Reader] API lacks support to read into a buffer such that you do not
/// end up in a half code-point at the end of your `char[]` buffer.
public final class ReaderHelper {

  private ReaderHelper() {

  }

  /// Reads from the given [Reader] into the given `buffer` while ensuring that always entire
  /// [code-points][String#codePointAt(int)] are read avoiding to have a single [surrogate][Character#isSurrogate(char)] at the end of the `buffer`.
  ///
  /// @param reader the [Reader] to [read][Reader#read(char[], int, int)] from.
  /// @param buffer the `char` array to fill.
  /// @return the number of `char`s that have been read into the given `buffer` starting from `offset`
  ///         or `-1` if the [Reader] was already at the end of the stream and no more characters are
  ///         available.
  public static int read(Reader reader, char[] buffer) {

    return read(reader, buffer, 0, buffer.length);
  }

  /// Reads from the given [Reader] into the given `buffer` while ensuring that always entire
  /// [code-points][String#codePointAt(int)] are read avoiding to have a single [surrogate][Character#isSurrogate(char)] at the end of the `buffer`.
  ///
  /// @param reader the [Reader] to [read][Reader#read(char[], int, int)] from.
  /// @param buffer the `char` array to fill.
  /// @param offset the start index in the `buffer` where to fill. Typically `0`.
  /// @return the number of `char`s that have been read into the given `buffer` starting from `offset`
  ///         or `-1` if the [Reader] was already at the end of the stream and no more characters are
  ///         available.
  public static int read(Reader reader, char[] buffer, int offset) {

    return read(reader, buffer, offset, buffer.length - offset);
  }

  /// Reads from the given [Reader] into the given `buffer` while ensuring that always entire
  /// [code-points][String#codePointAt(int)] are read avoiding to have a single [surrogate][Character#isSurrogate(char)] at the end of the `buffer`.
  ///
  /// @param reader the [Reader] to [read][Reader#read(char[], int, int)] from.
  /// @param buffer the `char` array to fill.
  /// @param offset the start index in the `buffer` where to fill. Typically `0`.
  /// @param length the maximum number of `char`s to read. Must be at least `2`.
  /// @return the number of `char`s that have been read into the given `buffer` starting from `offset`
  ///         or `-1` if the [Reader] was already at the end of the stream and no more characters are
  ///         available.
  public static int read(Reader reader, char[] buffer, int offset, int length) {

    if (offset < 0) {
      throw new IllegalArgumentException("Offset: " + offset);
    } else if (offset + length > buffer.length) {
      throw new IllegalArgumentException("Offset: " + offset + ", length: " + length + ", capacity: " + buffer.length);
    }
    try {
      int limit = 0;
      while (limit == 0) {
        limit = reader.read(buffer, offset, length - 1);
      }
      if (limit > 0) {
        char last = buffer[offset + limit - 1];
        if (Character.isSurrogate(last)) {
          int next = reader.read();
          if (next >= 0) {
            limit++;
            buffer[offset + limit - 1] = (char) next;
          }
        }
      }
      return limit;
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

}
