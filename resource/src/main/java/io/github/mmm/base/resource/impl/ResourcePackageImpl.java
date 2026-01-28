package io.github.mmm.base.resource.impl;

import java.util.Objects;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourcePackage;

/**
 * Implementation of {@link ResourcePackage}.
 */
public final class ResourcePackageImpl extends ResourcePathImpl implements ResourcePackage {

  private Package javaPackage;

  ResourcePackageImpl(ModuleAccess moduleAccess, String path, String name, ResourcePackage parent) {

    this(moduleAccess, path, name, parent, null);
  }

  /**
   * The constructor.
   *
   * @param moduleAccess the {@link #getModuleAccess() module access}.
   * @param path the {@link #getPath() path}.
   * @param name the {@link #getName() package name}.
   * @param javaPackage the {@link #getPackage() package} or {@code null} for lazy init.
   */
  ResourcePackageImpl(ModuleAccess moduleAccess, String path, String name, ResourcePackage parent,
      Package javaPackage) {

    super(moduleAccess, path, name, parent);
    Objects.requireNonNull(this.name);
    this.javaPackage = javaPackage;
  }

  @Override
  public boolean isPackage() {

    return true;
  }

  @Override
  public boolean isFile() {

    return false;
  }

  @Override
  public boolean isType() {

    return false;
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public Package getPackage() {

    if (this.javaPackage == null) {
      this.javaPackage = this.moduleAccess.get().getClassLoader().getDefinedPackage(this.name);
    }
    return this.javaPackage;
  }

}
