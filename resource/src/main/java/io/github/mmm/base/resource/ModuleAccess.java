package io.github.mmm.base.resource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.module.ModuleReader;
import java.lang.module.ResolvedModule;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Simple wrapper for {@link ResolvedModule} and {@link Module}.
 */
public interface ModuleAccess {

  /**
   * @return the {@link ResolvedModule}.
   */
  ResolvedModule getResolved();

  /**
   * @return the corresponding {@link Module}.
   */
  Module get();

  /**
   * @return {@code true} if this is a java module (starting with "java." such as "java.base"), {@code false} otherwise.
   */
  default boolean isJavaModule() {

    return hasPrefix("java.");
  }

  /**
   * @return {@code true} if this is a java module (starting with "jdk." such as "jdk.net"), {@code false} otherwise.
   */
  default boolean isJdkModule() {

    return hasPrefix("jdk.");
  }

  /**
   * @return {@code true} if this is an internal JVM {@link Module} (a {@link #isJavaModule() java module} or a
   *         {@link #isJdkModule() jdk module}), {@code false} otherwise.
   */
  default boolean isInternalModule() {

    return isJavaModule() || isJdkModule();
  }

  /**
   * @param prefix the expected prefix of the {@link Module#getName() module name}. E.g. "org.apache.".
   * @return {@code true} if the {@link Module#getName() module name} starts with the given {@code prefix},
   *         {@code false} otherwise.
   */
  default boolean hasPrefix(String prefix) {

    Module module = get();
    if (module.isNamed()) {
      return module.getName().startsWith(prefix);
    }
    return false;
  }

  /**
   * @return the {@link ResourceMap} with all resources of this module. Will be loaded and created on every call of this
   *         method to avoid memory leaks. Avoid calling this method multiple times on the same object.
   */
  ResourceMap findResources();

  /**
   * @return the {@link List} of entries (path of file or folder) from this module. Entries use slash as separator and
   *         have no leading slash and folders and with a trailing slash. Examples are "java/lang/" or
   *         "java/lang/String.class".
   * @see #findResources()
   */
  default List<String> findEntries() {

    return findEntries(e -> e);
  }

  /**
   * @return the {@link List} of {@link #findEntries() entries} that represent a folder (excluding all files).
   * @see #findResources()
   */
  default List<String> findFolders() {

    return findEntries(e -> (e.endsWith("/")) ? e : null);
  }

  /**
   * @return the {@link List} of {@link #findEntries() entries} that represent a file (excluding all folders).
   * @see #findResources()
   */
  default List<String> findFiles() {

    return findEntries(e -> (e.endsWith("/")) ? null : e);
  }

  /**
   * @return the {@link List} of {@link #findFiles() files} that represent a {@link Class} mapped to their
   *         {@link Class#getName() class-name}.
   * @see #findResources()
   */
  default List<String> findClasses() {

    return findEntries(e -> (e.endsWith(".class")) ? e.substring(0, e.length() - 6).replace('/', '.') : null);
  }

  /**
   * @param mapper the {@link Function} used to {@link Function#apply(Object) map} an entry to the desired result or to
   *        {@code null} in order to filter and exclude the entry.
   * @return the {@link List} of mapped entries (path of file or folder) from this module.
   * @see #findEntries()
   * @see #findResources()
   */
  default List<String> findEntries(Function<String, String> mapper) {

    try (ModuleReader reader = getResolved().reference().open(); //
        Stream<String> entriesStream = reader.list()) {

      return entriesStream.map(mapper).filter(x -> (x != null)).toList();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
