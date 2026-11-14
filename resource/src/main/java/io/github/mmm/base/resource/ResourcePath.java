package io.github.mmm.base.resource;

/// Interface for a path to a resource as a [file][ResourceFile] or
public interface ResourcePath extends Comparable<ResourcePath> {

  /// The [path][#getPath()] of the root [package/folder][ResourcePackage].
  String PATH_ROOT = "/";

  /// @return the ModuleAccess owning this [ResourcePath].
  ModuleAccess getModuleAccess();

  /// @return the path to the resource. Uses slash as separator and has no leading slash. Folders have a trailing slash.
  ///         Examples are "java/lang/" or "java/lang/String.class".
  String getPath();

  /// @return the qualified name of the [Class] or [Package] a the [path][#getPath()] without the
  ///         trailing suffix ".class" or "/" and with dots (".") replaced by slashes ("/"). If not representing a
  ///         [Class] or (parent) [Package] this the result of this method may be non-sense. E.g. a
  ///         [ResourceFile] with the [path][#getPath()] "foo.bar/some.ext" will return "foo.bar.some.ext".
  /// @see #getSimpleName()
  String getName();

  /// Examples:
  /// <table border="1">
  /// <tr>
  /// <th>[Name][#getName()]</th>
  /// <th>[Simple name][#getSimpleName()]</th>
  /// </tr>
  /// <tr>
  /// <td>java.lang</td>
  /// <td>lang</td>
  /// <tr>
  /// <tr>
  /// <td>java.lang.String</td>
  /// <td>String</td>
  /// <tr>
  /// <tr>
  /// <td>META-INF/services/com.example.service</td>
  /// <td>com.example.service</td>
  /// <tr>
  /// </table>
  ///
  /// @return the simple name without the [parent][#getParent()].
  String getSimpleName();

  /// @return the parent [ResourcePackage] containing this resource.
  ResourcePackage getParent();

  /// @return `true` if this is a regular [ResourceFile], `false` otherwise (type or package).
  default boolean isFile() {

    return false;
  }

  /// @return `true` if this is a [ResourcePackage], `false` otherwise (file or type).
  default boolean isPackage() {

    return false;
  }

  /// @return `true` if this is a [ResourceType], `false` otherwise (file or package).
  default boolean isType() {

    return false;
  }

  @Override
  default int compareTo(ResourcePath other) {

    if (other == null) {
      return 1;
    }
    return getName().compareTo(other.getName());
  }

}
