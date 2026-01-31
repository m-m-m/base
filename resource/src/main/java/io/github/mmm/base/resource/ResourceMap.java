package io.github.mmm.base.resource;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Map containing all {@link ResourcePath resources} of a module.
 */
public interface ResourceMap {

  /**
   * @return a {@link Collection} with all {@link ResourcePath#getPath() paths} contained in this module.
   */
  Collection<String> getPaths();

  /**
   * @param <T> type of the expected {@link ResourcePath}. Should be one of {@link ResourceType},
   *        {@link ResourcePackage}, {@link ResourceFile}, or {@link ResourcePath}.
   * @param path the {@link ResourcePath#getPath() path} of the requested {@link ResourcePath resource}.
   * @return the {@link ResourcePath} with the given {@link ResourcePath#getPath() path}.
   * @see #getPaths()
   */
  <T extends ResourcePath> T getByPath(String path);

  /**
   * @param name the {@link ResourcePackage#getName() name} of the requested {@link ResourcePackage package}.
   * @return the {@link ResourcePackage} with the given {@link ResourcePackage#getName() name}.
   * @see #getByPath(String)
   */
  default ResourcePackage getPackageByName(String name) {

    return getByPath(name.replace('.', '/'));
  }

  /**
   * @param name the {@link ResourceType#getName() name} of the requested {@link ResourceType type}.
   * @return the {@link ResourceType} with the given {@link ResourceType#getName() name}.
   * @see #getByPath(String)
   */
  default ResourceType getTypeByName(String name) {

    return getByPath(name.replace('.', '/') + ".class");
  }

  /**
   * @return the {@link ResourceType} for the {@link ResourceType#MODULE_INFO_CLASS module-info}.
   */
  default ResourceType getModuleInfo() {

    return getByPath(ResourceType.MODULE_INFO_CLASS);
  }

  /**
   * @return a {@link Collection} with all {@link ResourcePath resources}.
   */
  Collection<ResourcePath> getAll();

  /**
   * @return a {@link Stream} with all {@link ResourceType types} (aka {@link Class}es).
   */
  default Stream<ResourceType> getTypes() {

    return getAll().stream().filter(r -> r.isType()).map(ResourceType.class::cast);
  }

  /**
   * @return a {@link Stream} with all (regular) {@link ResourceFile files}.
   */
  default Stream<ResourceFile> getFiles() {

    return getAll().stream().filter(r -> r.isFile()).map(ResourceFile.class::cast);
  }

  /**
   * @return a {@link Stream} with all {@link ResourcePackage packages}.
   */
  default Stream<ResourcePackage> getPackages() {

    return getAll().stream().filter(r -> r.isPackage()).map(ResourcePackage.class::cast);
  }

}
