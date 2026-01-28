package io.github.mmm.base.resource;

import java.lang.module.ModuleDescriptor;

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

    Module module = getModuleAccess().get();
    ModuleDescriptor descriptor = module.getDescriptor();
    if (descriptor.isOpen()) {
      return true;
    } else if (descriptor.isAutomatic()) {
      return true;
    } else if (module.isOpen(getName())) {
      return true;
    }
    return false;
  }

}
