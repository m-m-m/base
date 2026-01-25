package io.github.mmm.base.resource;

/**
 * {@link ResourceFile} pointing to a Java type as {@link Class}.
 */
public interface ResourceType extends ResourceFile, ResourceJavaPath {

  /**
   * @return the {@link Class#forName(String) loaded} {@link Class}.
   * @throws RuntimeException if class-loading failed.
   */
  Class<?> loadClass();

  /**
   * @return {@code true} if this is a {@code package-info.class} file, {@code false} otherwise.
   */
  default boolean isPackageInfo() {

    return getPath().endsWith("package-info.class");
  }

  /**
   * @return {@code true} if this is an inner type file, {@code false} otherwise (top-level type).
   */
  default boolean isInnerType() {

    return getPath().contains("$");
  }

}
