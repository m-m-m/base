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
   * @return the qualified name of the {@link Class} or {@link Package} a the {@link #getPath() path} without the
   *         trailing suffix ".class" or "/" and with dots (".") replaced by slashes ("/"). If not representing a
   *         {@link Class} or (parent) {@link Package} this the result of this method may be non-sense. E.g. a
   *         {@link ResourceFile} with the {@link #getPath() path} "foo.bar/some.ext" will return "foo.bar.some.ext".
   * @see #getSimpleName()
   */
  String getName();

  /**
   * Examples:
   * <table border="1">
   * <tr>
   * <th>{@link #getName() Name}</th>
   * <th>{@link #getSimpleName() Simple name}</th>
   * </tr>
   * <tr>
   * <td>java.lang</td>
   * <td>lang</td>
   * <tr>
   * <tr>
   * <td>java.lang.String</td>
   * <td>String</td>
   * <tr>
   * <tr>
   * <td>META-INF/services/com.example.service</td>
   * <td>com.example.service</td>
   * <tr>
   * </table>
   *
   * @return the simple name without the {@link #getParent() parent}.
   */
  String getSimpleName();

  /**
   * @return the parent {@link ResourcePackage} containing this resource.
   */
  ResourcePackage getParent();

  /**
   * @return {@code true} if this is a regular {@link ResourceFile}, {@code false} otherwise (type or package).
   */
  default boolean isFile() {

    return false;
  }

  /**
   * @return {@code true} if this is a {@link ResourcePackage}, {@code false} otherwise (file or type).
   */
  default boolean isPackage() {

    return false;
  }

  /**
   * @return {@code true} if this is a {@link ResourceType}, {@code false} otherwise (file or package).
   */
  default boolean isType() {

    return false;
  }

}
