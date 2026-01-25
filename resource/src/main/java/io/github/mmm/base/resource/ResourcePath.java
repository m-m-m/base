package io.github.mmm.base.resource;

/**
 * Interface for a path to a resource as a {@link ResourceFile file} or
 */
public interface ResourcePath {

  /**
   * @return the ModuleAccess owning this {@link ResourcePath}.
   */
  ModuleAccess getModuleAccess();

  /**
   * @return the path to the resource. Uses slash as separator and has no leading slash. Folders have a trailing slash.
   *         Examples are "java/lang/" or "java/lang/String.class".
   */
  String getPath();

  /**
   * @return {@code true} if this is a {@link ResourceFile}, {@code false} otherwise (if a {@link ResourceFolder}).
   */
  boolean isFile();

  /**
   * @return {@code true} if this is a {@link ResourceFolder}, {@code false} otherwise (if a {@link ResourceFile}).
   */
  boolean isFolder();

  /**
   * @return {@code true} if this is a {@link ResourceSimpleFile} or {@link ResourceSimpleFolder}, {@code false}
   *         otherwise (if a {@link ResourceType} or {@link ResourcePackage}).
   */
  boolean isSimple();

  /**
   * @return {@code true} if this is a {@link ResourceType} or {@link ResourcePackage}, {@code false} otherwise (if a
   *         {@link ResourceSimpleFile} or {@link ResourceSimpleFolder}).
   */
  boolean isJava();

}
