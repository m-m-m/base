package io.github.mmm.base.resource.impl;

import java.util.Objects;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourcePackage;
import io.github.mmm.base.resource.ResourceType;
import io.github.mmm.base.type.JavaType;
import io.github.mmm.base.type.impl.JavaTypeReader;

/**
 * Implementation of {@link ResourceType}.
 */
public final class ResourceTypeImpl extends AbstractResourceFileImpl implements ResourceType {

  private Class<?> javaClass;

  private JavaType javaType;

  /**
   * The constructor.
   *
   * @param moduleAccess the {@link #getModuleAccess() module access}.
   * @param path the {@link #getPath() path}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param parent the {@link #getParent() package folder}.
   */
  public ResourceTypeImpl(ModuleAccess moduleAccess, String path, String simpleName, ResourcePackage parent) {

    super(moduleAccess, path, simpleName, parent);
    assert (path.endsWith(".class")) : "invalid class file path:" + path;
    Objects.requireNonNull(parent);
  }

  @Override
  public Class<?> loadClass() {

    if (this.javaClass == null) {
      try {
        if (isModuleInfo()) {
          return null;
        }
        this.javaClass = this.moduleAccess.get().getClassLoader().loadClass(this.name);
      } catch (ClassNotFoundException e) {
        throw new IllegalStateException(e);
      }
    }
    return this.javaClass;
  }

  @Override
  public JavaType loadType() {

    if (this.javaType == null) {
      this.javaType = processAsStream(JavaTypeReader.INSTANCE);
    }
    return this.javaType;
  }

}
