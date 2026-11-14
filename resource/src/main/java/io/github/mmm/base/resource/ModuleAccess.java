package io.github.mmm.base.resource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleReader;
import java.lang.module.ResolvedModule;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/// Simple wrapper for [ResolvedModule] and [Module].
/// **ATTENTION**: Please consider using [ResourceScanner] over find methods like [#findResources()] if
/// you want to do resource scanning efficiently during application bootstrapping.
public interface ModuleAccess {

  /// @return the [ResolvedModule].
  ResolvedModule getResolved();

  /// @return the corresponding [Module].
  Module get();

  /// @param packageName the [package name][Package#getName()] to check.
  /// @return `true` if the [Module] is [open][ModuleDescriptor#isOpen()],
  ///         [automatic][ModuleDescriptor#isAutomatic()], or [exports the package
         unconditionally][Module#isOpen(String)].
  boolean isOpen(String packageName);

  /// @return `true` if this is a java module (starting with "java." such as "java.base"), `false` otherwise.
  default boolean isJavaModule() {

    return hasPrefix("java.");
  }

  /// @return `true` if this is a java module (starting with "jdk." such as "jdk.net"), `false` otherwise.
  default boolean isJdkModule() {

    return hasPrefix("jdk.");
  }

  /// @return `true` if this is an internal JVM [Module] (a [java module][#isJavaModule()] or a
  ///         [jdk module][#isJdkModule()]), `false` otherwise.
  default boolean isInternalModule() {

    return isJavaModule() || isJdkModule();
  }

  /// @param prefix the expected prefix of the [module name][Module#getName()]. E.g. "org.apache.".
  /// @return `true` if the [module name][Module#getName()] starts with the given `prefix`,
  ///         `false` otherwise.
  default boolean hasPrefix(String prefix) {

    Module module = get();
    if (module.isNamed()) {
      return module.getName().startsWith(prefix);
    }
    return false;
  }

  /// @return the [ResourceMap] with all resources of this module. Will be loaded and created on every call of this
  ///         method to avoid memory leaks. Avoid calling this method multiple times on the same object.
  /// @see #findResources(Predicate)
  ResourceMap findResources();

  /// @param <T> type of the expected [ResourcePath]. If your [Predicate] guarantees to only accept
  ///        [ResourceType], [ResourcePackage], or [ResourceFile] you can directly receive the result
  ///        accordingly.
  /// @param filter the [Predicate] used to [test][Predicate#test(Object)] which entries shall be accepted and
  ///        returned as [resource][ResourcePath]. If you only need few specific [resources][ResourcePath],
  ///        this method is slightly more efficient than [#findResources()] that needs to create all resource
  ///        objects.
  /// @return the [List] of the matching [resources][ResourcePath].
  /// @see #findResources()
  <T extends ResourcePath> List<T> findResources(Predicate<String> filter);

  /// @return the [List] of entries (path of file or folder) from this module. Entries use slash as separator and
  ///         have no leading slash and folders and with a trailing slash. Examples are "java/lang/" or
  ///         "java/lang/String.class".
  /// @see #findResources()
  default List<String> findEntries() {

    return findEntries(e -> e);
  }

  /// @return the [List] of [entries][#findEntries()] that represent a folder (excluding all files).
  /// @see #findResources()
  default List<String> findFolders() {

    return findEntries(e -> (e.endsWith("/")) ? e : null);
  }

  /// @return the [List] of [entries][#findEntries()] that represent a file (excluding all folders).
  /// @see #findResources()
  default List<String> findFiles() {

    return findEntries(e -> (e.endsWith("/")) ? null : e);
  }

  /// @return the [List] of [files][#findFiles()] that represent a [Class] mapped to their
  ///         [class-name][Class#getName()].
  /// @see #findResources()
  default List<String> findClasses() {

    return findEntries(e -> (e.endsWith(".class")) ? e.substring(0, e.length() - 6).replace('/', '.') : null);
  }

  /// @param mapper the [Function] used to [map][Function#apply(Object)] an entry to the desired result or to
  ///        `null` in order to filter and exclude the entry.
  /// @return the [List] of mapped entries (path of file or folder) from this module.
  /// @see #findEntries()
  /// @see #findResources()
  default List<String> findEntries(Function<String, String> mapper) {

    try (ModuleReader reader = getResolved().reference().open(); //
        Stream<String> entriesStream = reader.list()) {

      return entriesStream.map(mapper).filter(x -> (x != null)).toList();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /// @param predicate the [Predicate] used to [test][Predicate#test(Object)] which entries shall be accepted.
  ///        You may always return `false` to visit all entries and potentially collect them internally in your
  ///        [Predicate].
  /// @return `true` if at least one entry was found [accepted][Predicate#test(Object)] by the given
  ///         [Predicate], `false` otherwise.
  /// @see #findEntries()
  /// @see #findResources()
  default boolean hasEntries(Predicate<String> predicate) {

    try (ModuleReader reader = getResolved().reference().open(); //
        Stream<String> entriesStream = reader.list()) {
      return entriesStream.anyMatch(predicate);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
