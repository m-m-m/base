package io.github.mmm.base.resource.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourceFile;

/**
 * Implementation of {@link ResourceFile}.
 */
public abstract class AbstractResourceFile extends AbstractResourcePath implements ResourceFile {

  /**
   * The constructor.
   *
   * @param moduleAccess the {@link #getModuleAccess() module access}.
   * @param path the {@link #getPath() path}.
   */
  public AbstractResourceFile(ModuleAccess moduleAccess, String path) {

    super(moduleAccess, path);
    assert (!path.endsWith("/")) : "invalid file path:" + path;
  }

  @Override
  public final boolean isFile() {

    return true;
  }

  @Override
  public final boolean isFolder() {

    return false;
  }

  @Override
  public InputStream asStream() {

    try {
      return this.moduleAccess.get().getResourceAsStream(this.path);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to open file " + this.path + " from module " + this.moduleAccess, e);
    }
  }

}
