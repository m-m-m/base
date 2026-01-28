package io.github.mmm.base.resource.impl;

import java.util.Objects;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourcePackage;
import io.github.mmm.base.resource.ResourcePath;
import io.github.mmm.base.resource.ResourceType;

/**
 * Implementation of {@link ResourcePath}.
 */
public abstract class ResourcePathImpl implements ResourcePath {

  /** @see #getModuleAccess() */
  protected final ModuleAccess moduleAccess;

  /** @see #getPath() */
  protected final String path;

  /** @see #getName() */
  protected final String name;

  /** @see #getSimpleName() */
  protected final String simpleName;

  private final ResourcePackage parent;

  /**
   * The constructor.
   *
   * @param moduleAccess the {@link #getModuleAccess() module access}.
   * @param path the {@link #getPath() path}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param parent the {@link #getParent() parent}.
   */
  public ResourcePathImpl(ModuleAccess moduleAccess, String path, String simpleName, ResourcePackage parent) {

    super();
    Objects.requireNonNull(moduleAccess);
    Objects.requireNonNull(path);
    this.moduleAccess = moduleAccess;
    this.path = path;
    this.simpleName = simpleName;
    this.parent = parent;
    if ((parent == null) || parent.isRoot()) {
      this.name = simpleName;
    } else {
      this.name = parent.getName() + "." + simpleName;
    }
    assert verify();
  }

  private boolean verify() {

    String prefix = "";
    String suffix = "";
    if (this instanceof ResourceType) {
      suffix = ".class";
    } else if (this instanceof ResourcePackage) {
      suffix = "/";
    }
    if ((this.parent != null) && !this.parent.isRoot()) {
      prefix = this.parent.getPath();
    }
    String expectedPath = prefix + this.simpleName + suffix;
    assert this.path.equals(expectedPath) : "Invalid path '" + this.path + "' expected '" + expectedPath + "'.";
    return true;
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
  public String getName() {

    return this.name;
  }

  @Override
  public String getSimpleName() {

    return this.simpleName;
  }

  @Override
  public ResourcePackage getParent() {

    return this.parent;
  }

  @Override
  public String toString() {

    return this.path;
  }

}
