package io.github.mmm.base.resource.impl;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourceFile;
import io.github.mmm.base.resource.ResourcePackage;

/// Implementation of [ResourceFile].
public final class ResourceFileImpl extends AbstractResourceFileImpl implements ResourceFile {

  /// The constructor.
  ///
  /// @param moduleAccess the [module access][#getModuleAccess()].
  /// @param path the [path][#getPath()].
  /// @param simpleName the [simple name][#getSimpleName()].
  /// @param parent the [folder][#getParent()].
  public ResourceFileImpl(ModuleAccess moduleAccess, String path, String simpleName, ResourcePackage parent) {

    super(moduleAccess, path, simpleName, parent);
    assert (!path.endsWith(".class")) : "invalid file path:" + path;
  }

}
