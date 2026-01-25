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
   * @param <T> type of the expected {@link ResourcePath}. E.g. {@link ResourceSimpleFile} or {@link ResourceType}.
   * @param path the {@link ResourcePath#getPath() path} of the requested {@link ResourcePath resource}.
   * @return the {@link ResourcePath} with the given {@link ResourcePath#getPath() path}.
   * @see #getPaths()
   */
  <T extends ResourcePath> T get(String path);

  /**
   * @return a {@link Collection} with all {@link ResourcePath resources}.
   */
  Collection<ResourcePath> getAll();

  /**
   * @return a {@link Stream} with all {@link ResourceType types} (aka {@link Class}es).
   */
  default Stream<ResourceType> getTypes() {

    return getAll().stream().filter(r -> r.isFile() && r.isJava()).map(ResourceType.class::cast);
  }

  /**
   * @return a {@link Stream} with all {@link ResourceSimpleFile simple files}.
   */
  default Stream<ResourceSimpleFile> getSimpleFiles() {

    return getAll().stream().filter(r -> r.isFile() && r.isSimple()).map(ResourceSimpleFile.class::cast);
  }

  /**
   * @return a {@link Stream} with all {@link ResourcePackage packages}.
   */
  default Stream<ResourcePackage> getPackages() {

    return getAll().stream().filter(r -> r.isFolder() && r.isJava()).map(ResourcePackage.class::cast);
  }

  /**
   * @return a {@link Stream} with all {@link ResourceSimpleFolder simple folders}.
   */
  default Stream<ResourceSimpleFolder> getSimpleFolders() {

    return getAll().stream().filter(r -> r.isFolder() && r.isSimple()).map(ResourceSimpleFolder.class::cast);
  }

}
