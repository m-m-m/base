package io.github.mmm.base.resource.impl;

import java.util.Objects;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourcePackage;

/**
 * Implementation of {@link ResourcePackage}.
 */
public final class ResourcePackageImpl extends AbstractResourceFolder implements ResourcePackage {

  private final String name;

  private Package javaPackage;

  /**
   * The constructor.
   *
   * @param moduleAccess the {@link #getModuleAccess() module access}.
   * @param path the {@link #getPath() path}.
   * @param name the {@link #getName() package name}.
   */
  public ResourcePackageImpl(ModuleAccess moduleAccess, String path, String name) {

    super(moduleAccess, path);
    Objects.requireNonNull(name);
    this.name = name;
  }

  @Override
  public boolean isSimple() {

    return false;
  }

  @Override
  public boolean isJava() {

    return true;
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
