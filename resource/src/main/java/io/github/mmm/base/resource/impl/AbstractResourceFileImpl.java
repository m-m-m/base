package io.github.mmm.base.resource.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import io.github.mmm.base.resource.AbstractResourceFile;
import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourceFile;
import io.github.mmm.base.resource.ResourcePackage;

/// Implementation of [ResourceFile].
public abstract class AbstractResourceFileImpl extends ResourcePathImpl implements AbstractResourceFile {

  /// The constructor.
  ///
  /// @param moduleAccess the [module access][#getModuleAccess()].
  /// @param path the [path][#getPath()].
  /// @param simpleName the [simple name][#getSimpleName()].
  /// @param parent the [parent][#getParent()].
  public AbstractResourceFileImpl(ModuleAccess moduleAccess, String path, String simpleName, ResourcePackage parent) {

    super(moduleAccess, path, simpleName, parent);
    assert (!path.endsWith("/")) : "invalid file path:" + path;
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
