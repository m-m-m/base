package io.github.mmm.base.resource.impl;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourceSimpleFile;

/**
 * Implementation of {@link ResourceSimpleFile}.
 */
public final class ResourceSimpleFileImpl extends AbstractResourceFile implements ResourceSimpleFile {

  /**
   * The constructor.
   *
   * @param moduleAccess the {@link #getModuleAccess() module access}.
   * @param path the {@link #getPath() path}.
   */
  public ResourceSimpleFileImpl(ModuleAccess moduleAccess, String path) {

    super(moduleAccess, path);
    assert (!path.endsWith(".class")) : "invalid simple file path:" + path;
  }

  @Override
  public boolean isSimple() {

    return true;
  }

  @Override
  public boolean isJava() {

    return false;
  }

}
