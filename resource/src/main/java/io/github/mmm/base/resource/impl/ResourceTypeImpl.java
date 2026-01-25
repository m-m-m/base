package io.github.mmm.base.resource.impl;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourceType;

/**
 * Implementation of {@link ResourceType}.
 */
public final class ResourceTypeImpl extends AbstractResourceFile implements ResourceType {

  private final String name;

  private Class<?> type;

  /**
   * The constructor.
   *
   * @param moduleAccess the {@link #getModuleAccess() module access}.
   * @param path the {@link #getPath() path}.
   */
  public ResourceTypeImpl(ModuleAccess moduleAccess, String path) {

    super(moduleAccess, path);
    assert (path.endsWith(".class")) : "invalid class file path:" + path;
    this.name = path.substring(0, path.length() - 6).replace('/', '.');
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
  public Class<?> loadClass() {

    if (this.type == null) {
      try {
        this.type = this.moduleAccess.get().getClassLoader().loadClass(this.name);
      } catch (ClassNotFoundException e) {
        throw new IllegalStateException(e);
      }
    }
    return this.type;
  }

}
