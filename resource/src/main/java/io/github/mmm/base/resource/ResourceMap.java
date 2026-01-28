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
   * @param <T> type of the expected {@link ResourcePath}. Should be on of {@link ResourcePackage},
   *        {@link ResourceType}, or {@link ResourcePath}.
   * @param name the {@link ResourcePath#getName() name} of the requested {@link ResourcePath resource}.
   * @return the {@link ResourcePath} with the given {@link ResourcePath#getName() name}.
   * @see #getPaths()
   */
  default <T extends ResourcePath> T getByName(String name) {

    return getByPath(name.replace('.', '/'));
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
