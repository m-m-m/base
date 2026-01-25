package io.github.mmm.base.resource.impl;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourceSimpleFolder;

/**
 * Implementation of {@link ResourceSimpleFolder}.
 */
public final class ResourceSimpleFolderImpl extends AbstractResourceFolder implements ResourceSimpleFolder {

  /**
   * The constructor.
   *
   * @param moduleAccess the {@link #getModuleAccess() module access}.
   * @param path the {@link #getPath() path}.
   */
  public ResourceSimpleFolderImpl(ModuleAccess moduleAccess, String path) {

    super(moduleAccess, path);
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
