package io.github.mmm.base.resource;

import java.util.Collection;
import java.util.stream.Stream;

/// Map containing all [resources][ResourcePath] of a module.
public interface ResourceMap {

  /// @return a [Collection] with all [paths][ResourcePath#getPath()] contained in this module.
  Collection<String> getPaths();

  /// @param <T> type of the expected [ResourcePath]. Should be one of [ResourceType],
  ///        [ResourcePackage], [ResourceFile], or [ResourcePath].
  /// @param path the [path][ResourcePath#getPath()] of the requested [resource][ResourcePath].
  /// @return the [ResourcePath] with the given [path][ResourcePath#getPath()].
  /// @see #getPaths()
  <T extends ResourcePath> T getByPath(String path);

  /// @param name the [name][ResourcePackage#getName()] of the requested [package][ResourcePackage].
  /// @return the [ResourcePackage] with the given [name][ResourcePackage#getName()].
  /// @see #getByPath(String)
  default ResourcePackage getPackageByName(String name) {

    return getByPath(name.replace('.', '/'));
  }

  /// @param name the [name][ResourceType#getName()] of the requested [type][ResourceType].
  /// @return the [ResourceType] with the given [name][ResourceType#getName()].
  /// @see #getByPath(String)
  default ResourceType getTypeByName(String name) {

    return getByPath(name.replace('.', '/') + ".class");
  }

  /// @return the [ResourceType] for the [module-info][ResourceType#MODULE_INFO_CLASS].
  default ResourceType getModuleInfo() {

    return getByPath(ResourceType.MODULE_INFO_CLASS);
  }

  /// @return a [Collection] with all [resources][ResourcePath].
  Collection<ResourcePath> getAll();

  /// @return a [Stream] with all [types][ResourceType] (aka [Class]es).
  default Stream<ResourceType> getTypes() {

    return getAll().stream().filter(r -> r.isType()).map(ResourceType.class::cast);
  }

  /// @return a [Stream] with all (regular) [files][ResourceFile].
  default Stream<ResourceFile> getFiles() {

    return getAll().stream().filter(r -> r.isFile()).map(ResourceFile.class::cast);
  }

  /// @return a [Stream] with all [packages][ResourcePackage].
  default Stream<ResourcePackage> getPackages() {

    return getAll().stream().filter(r -> r.isPackage()).map(ResourcePackage.class::cast);
  }

}
