package io.github.mmm.base.resource.impl;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourceFile;
import io.github.mmm.base.resource.ResourceFolder;

/**
 * Implementation of {@link ResourceFile}.
 */
public abstract class AbstractResourceFolder extends AbstractResourcePath implements ResourceFolder {

  /**
   * The constructor.
   *
   * @param moduleAccess the {@link #getModuleAccess() module access}.
   * @param path the {@link #getPath() path}.
   */
  public AbstractResourceFolder(ModuleAccess moduleAccess, String path) {

    super(moduleAccess, path);
    assert (path.endsWith("/")) : "invalid folder path:" + path;
  }

  @Override
  public final boolean isFile() {

    return false;
  }

  @Override
  public final boolean isFolder() {

    return true;
  }

}
