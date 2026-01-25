package io.github.mmm.base.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import io.github.mmm.base.io.IoProcessor;

/**
 * Interface for a {@link ResourcePath} pointing to a file.
 */
public interface ResourceFile extends ResourcePath {

  /**
   * Opens an {@link InputStream} with the file content. Consider to use {@link #processAsStream(IoProcessor)} if
   * possible to prevent resource leaks.
   *
   * @return the {@link InputStream} to read the content of this file.
   */
  InputStream asStream();

  /**
   * @param <T> type of the returned result. Use {@link Void} and return {@code null} if not needed.
   * @param processor the {@link IoProcessor} to process the file as {@link InputStream}.
   * @return the result of the {@link IoProcessor}.
   */
  default <T> T processAsStream(IoProcessor<InputStream, T> processor) {

    try (InputStream in = asStream()) {
      return processor.process(in);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to read file " + getPath() + " from module " + getModuleAccess(), e);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to process file " + getPath() + " from module " + getModuleAccess(), e);
    }
  }

  /**
   * @param <T> type of the returned result. Use {@link Void} and return {@code null} if not needed.
   * @param processor the {@link IoProcessor} to process the file as {@link BufferedReader}.
   * @return the result of the {@link IoProcessor}.
   */
  default <T> T processAsReader(IoProcessor<BufferedReader, T> processor) {

    return processAsStream(in -> {
      try (Reader reader = new InputStreamReader(in); //
          BufferedReader br = new BufferedReader(reader)) {
        return processor.process(br);
      }
    });
  }

}
