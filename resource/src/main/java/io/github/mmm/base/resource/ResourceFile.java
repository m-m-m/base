package io.github.mmm.base.resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import io.github.mmm.base.io.IoProcessor;

/// Interface for a [ResourcePath] pointing to a file.
public interface ResourceFile extends AbstractResourceFile {

  @Override
  default boolean isFile() {

    return true;
  }

  /// @param <T> type of the returned result. Use [Void] and return `null` if not needed.
  /// @param processor the [IoProcessor] to process the file as [BufferedReader].
  /// @return the result of the [IoProcessor].
  default <T> T processAsReader(IoProcessor<BufferedReader, T> processor) {

    return processAsStream(in -> {
      try (Reader reader = new InputStreamReader(in); //
          BufferedReader br = new BufferedReader(reader)) {
        return processor.process(br);
      }
    });
  }

  /// @return reads all lines of the file assuming it is a small, textual file.
  /// @see BufferedReader#readAllLines()
  default List<String> readAllLines() {

    return processAsReader(BufferedReader::readAllLines);
  }

}
