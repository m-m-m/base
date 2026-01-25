package io.github.mmm.base.resource;

import java.lang.module.ModuleDescriptor;

/**
 * {@link ResourceFolder} pointing to a {@link Package}.
 */
public interface ResourcePackage extends ResourceFolder, ResourceJavaPath {

  /**
   * @return the Java {@link Package}.
   */
  Package getPackage();

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
