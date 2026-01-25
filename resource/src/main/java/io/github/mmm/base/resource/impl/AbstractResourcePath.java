package io.github.mmm.base.resource.impl;

import java.util.Objects;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourcePath;

/**
 * Implementation of {@link ResourcePath}.
 */
public abstract class AbstractResourcePath implements ResourcePath {

  /** @see #getModuleAccess() */
  protected final ModuleAccess moduleAccess;

  /** @see #getPath() */
  protected final String path;

  /**
   * The constructor.
   *
   * @param moduleAccess the {@link #getModuleAccess() module access}.
   * @param path the {@link #getPath() path}.
   */
  public AbstractResourcePath(ModuleAccess moduleAccess, String path) {

    super();
    Objects.requireNonNull(moduleAccess);
    Objects.requireNonNull(path);
    this.moduleAccess = moduleAccess;
    this.path = path;
  }

  @Override
  public ModuleAccess getModuleAccess() {

    return this.moduleAccess;
  }

  @Override
  public String getPath() {

    return this.path;
  }

  @Override
  public String toString() {

    return this.path;
  }

  static AbstractResourcePath of(String path, ModuleAccess moduleAccess) {

    if (path.endsWith("/")) {
      return new ResourceSimpleFolderImpl(moduleAccess, path);
    } else if (path.endsWith(".class")) {
      return new ResourceTypeImpl(moduleAccess, path);
    } else {
      return new ResourceSimpleFolderImpl(moduleAccess, path);
    }
  }

}
