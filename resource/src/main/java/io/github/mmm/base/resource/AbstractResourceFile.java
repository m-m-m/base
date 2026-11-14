package io.github.mmm.base.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import io.github.mmm.base.io.IoProcessor;

/// Interface for a [ResourcePath] pointing to a file.
public interface AbstractResourceFile extends ResourcePath {

  /// Opens an [InputStream] with the file content. Consider to use [#processAsStream(IoProcessor)] if
  /// possible to prevent resource leaks.
  ///
  /// @return the [InputStream] to read the content of this file.
  InputStream asStream();

  /// @param <T> type of the returned result. Use [Void] and return `null` if not needed.
  /// @param processor the [IoProcessor] to process the file as [InputStream].
  /// @return the result of the [IoProcessor].
  default <T> T processAsStream(IoProcessor<InputStream, T> processor) {

    try (InputStream in = asStream()) {
      return processor.process(in);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to read file " + getPath() + " from module " + getModuleAccess(), e);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to process file " + getPath() + " from module " + getModuleAccess(), e);
    }
  }

}
