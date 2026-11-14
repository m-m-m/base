package io.github.mmm.base.resource;

import io.github.mmm.base.type.JavaType;

/// [ResourceFile] pointing to a Java type as [Class].
public interface ResourceType extends ResourcePath {

  /// [Path][#getPath()] of [module-info][#isModuleInfo()].
  public static final String MODULE_INFO_CLASS = "module-info.class";

  /// [Path][#getPath()] of [package-info][#isPackageInfo()].
  public static final String PACKAGE_INFO_CLASS = "package-info.class";

  @Override
  default boolean isType() {

    return true;
  }

  /// @return the [loaded][Class#forName(String)] [Class]. Will be `null` for [module-info][#isModuleInfo()].
  /// @throws RuntimeException if class-loading failed.
  /// @see #loadType()
  Class<?> loadClass();

  /// @return the [JavaType] as "preview" of [#loadClass()]. Allows to see if the class is "relevant" before
  ///         triggering actual [class-loading][#loadClass()] while scanning classes. Here "relevant" could be all
  ///         classes implementing a specific interface or all records, etc.
  /// @throws RuntimeException if loading class-file failed.
  /// @see #loadClass()
  JavaType loadType();

  /// @return `true` if this is a `package-info.class` file, `false` otherwise.
  default boolean isPackageInfo() {

    return getPath().endsWith(PACKAGE_INFO_CLASS);
  }

  /// @return `true` if this is the `module-info.class` file, `false` otherwise.
  default boolean isModuleInfo() {

    return getPath().equals(MODULE_INFO_CLASS);
  }

  /// @return `true` if this is an inner type file, `false` otherwise (top-level type).
  default boolean isInnerType() {

    return getPath().contains("$");
  }

  /// @param path the [ResourcePath]. May be `null`.
  /// @return the [loaded][#loadClass()] [Class] or `null` if the given [ResourcePath] is not a
  ///         [ResourceType] or could not be loaded.
  static Class<?> loadClass(ResourcePath path) {

    if (path instanceof ResourceType type) {
      return type.loadClass();
    }
    return null;
  }

}
