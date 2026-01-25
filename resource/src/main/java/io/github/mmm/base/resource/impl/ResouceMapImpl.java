package io.github.mmm.base.resource.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.module.ModuleReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourceMap;
import io.github.mmm.base.resource.ResourcePath;

/**
 * Implementation of {@link ResourceMap}.
 */
public class ResouceMapImpl implements ResourceMap {

  private final ModuleAccess moduleAccess;

  private final Map<String, ResourcePath> map;

  ResouceMapImpl(ModuleAccess moduleAccess) {

    super();
    this.moduleAccess = moduleAccess;
    Map<String, ResourcePath> path2resourceMap = new HashMap<>();
    Set<String> packages = this.moduleAccess.get().getPackages();
    try (ModuleReader reader = this.moduleAccess.getResolved().reference().open(); //
        Stream<String> entriesStream = reader.list()) {

      Iterator<String> pathIterator = entriesStream.iterator();
      while (pathIterator.hasNext()) {
        String path = pathIterator.next();
        ResourcePath resource;
        if (path.endsWith("/")) {
          String pkgName = path.substring(0, path.length() - 1).replace('/', '.');
          if (packages.contains(pkgName)) {
            resource = new ResourcePackageImpl(this.moduleAccess, path, pkgName);
          } else {
            resource = new ResourceSimpleFolderImpl(this.moduleAccess, path);
          }
        } else if (path.endsWith(".class")) {
          resource = new ResourceTypeImpl(this.moduleAccess, path);
        } else {
          resource = new ResourceSimpleFileImpl(this.moduleAccess, path);
        }
        path2resourceMap.put(path, resource);
      }
      this.map = Map.copyOf(path2resourceMap);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public Collection<String> getPaths() {

    return this.map.keySet();
  }

  @Override
  public Collection<ResourcePath> getAll() {

    return this.map.values();
  }

  @Override
  public ResourcePath get(String path) {

    return this.map.get(path);
  }

}
