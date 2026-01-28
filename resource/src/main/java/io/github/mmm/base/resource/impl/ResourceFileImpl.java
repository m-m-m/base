package io.github.mmm.base.resource.impl;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourceFile;
import io.github.mmm.base.resource.ResourcePackage;

/**
 * Implementation of {@link ResourceFile}.
 */
public final class ResourceFileImpl extends AbstractResourceFileImpl implements ResourceFile {

  /**
   * The constructor.
   *
   * @param moduleAccess the {@link #getModuleAccess() module access}.
   * @param path the {@link #getPath() path}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param parent the {@link #getParent() folder}.
   */
  public ResourceFileImpl(ModuleAccess moduleAccess, String path, String simpleName, ResourcePackage parent) {

    super(moduleAccess, path, simpleName, parent);
    assert (!path.endsWith(".class")) : "invalid file path:" + path;
  }

}
