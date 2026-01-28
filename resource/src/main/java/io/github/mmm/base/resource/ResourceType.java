package io.github.mmm.base.resource;

import io.github.mmm.base.type.JavaType;

/**
 * {@link ResourceFile} pointing to a Java type as {@link Class}.
 */
public interface ResourceType extends ResourcePath {

  /** {@link #getPath() Path} of {@link #isModuleInfo() module-info}. */
  public static final String MODULE_INFO_CLASS = "module-info.class";

  /** {@link #getPath() Path} of {@link #isPackageInfo() package-info}. */
  public static final String PACKAGE_INFO_CLASS = "package-info.class";

  @Override
  default boolean isType() {

    return true;
  }

  /**
   * @return the {@link Class#forName(String) loaded} {@link Class}. Will be {@code null} for {@link #isModuleInfo()
   *         module-info}.
   * @throws RuntimeException if class-loading failed.
   * @see #loadType()
   */
  Class<?> loadClass();

  /**
   * @return the {@link JavaType} as "preview" of {@link #loadClass()}. Allows to see if the class is "relevant" before
   *         triggering actual {@link #loadClass() class-loading} while scanning classes. Here "relevant" could be all
   *         classes implementing a specific interface or all records, etc.
   * @throws RuntimeException if loading class-file failed.
   * @see #loadClass()
   */
  JavaType loadType();

  /**
   * @return {@code true} if this is a {@code package-info.class} file, {@code false} otherwise.
   */
  default boolean isPackageInfo() {

    return getPath().endsWith(PACKAGE_INFO_CLASS);
  }

  /**
   * @return {@code true} if this is the {@code module-info.class} file, {@code false} otherwise.
   */
  default boolean isModuleInfo() {

    return getPath().equals(MODULE_INFO_CLASS);
  }

  /**
   * @return {@code true} if this is an inner type file, {@code false} otherwise (top-level type).
   */
  default boolean isInnerType() {

    return getPath().contains("$");
  }

}
