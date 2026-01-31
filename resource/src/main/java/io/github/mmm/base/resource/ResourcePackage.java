package io.github.mmm.base.resource;

/**
 * Interface for a {@link ResourcePath} pointing to a folder ({@link #getPackage() likely a package}).
 */
public interface ResourcePackage extends ResourcePath {

  /**
   * @return the Java {@link Package} or {@code null} if this is a simple folder that has no {@link Package}. Examples
   *         are the {@link #isRoot() root} folder, parent packages of modules not containing types, or folders like
   *         "META-INF" that do not have a corresponding {@link Package} in Java.
   */
  Package getPackage();

  /**
   * @return {@code true} if this is the root folder, {@code false} otherwise.
   */
  default boolean isRoot() {

    return getParent() == null;
  }

  /**
   * @return {@code true} if the {@link Package} is open unconditionally (the entire module is open, automatic or
   *         exports the package unconditionally), {@code false} otherwise.
   */
  default boolean isOpen() {

    return getModuleAccess().isOpen(getName());
  }

}
