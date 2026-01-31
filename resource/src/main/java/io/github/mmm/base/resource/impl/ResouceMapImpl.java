package io.github.mmm.base.resource.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.module.ModuleReader;
import java.lang.module.ResolvedModule;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourceMap;
import io.github.mmm.base.resource.ResourcePackage;
import io.github.mmm.base.resource.ResourcePath;

/**
 * Implementation of {@link ResourceMap}.
 */
public class ResouceMapImpl implements ResourceMap {

  private static final Logger LOG = LoggerFactory.getLogger(ResouceMapImpl.class);

  private final ModuleAccess moduleAccess;

  private final Map<String, ResourcePath> map;

  ResouceMapImpl(ModuleAccess moduleAccess) {

    super();
    this.moduleAccess = moduleAccess;
    Map<String, ResourcePath> path2resourceMap = new HashMap<>();
    final ResourcePackage root = new ResourcePackageImpl(this.moduleAccess, "/", "", null);
    ResolvedModule resolved = this.moduleAccess.getResolved();
    LOG.trace("Scanning module {}...", resolved.name());
    try (ModuleReader reader = resolved.reference().open(); //
        Stream<String> entriesStream = reader.list()) {

      Iterator<String> pathIterator = entriesStream.iterator();
      while (pathIterator.hasNext()) {
        String path = pathIterator.next();
        LOG.trace("Found {}@{}", resolved.name(), path);
        if ((path == null) || path.isEmpty()) {
          continue; // robustness: ignore non-sense
        }
        getOrCreateResource(path, root, path2resourceMap);
      }
      this.map = Map.copyOf(path2resourceMap);
      LOG.trace("Scanned module {} and found {} resources...", resolved.name(), this.map.size());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private ResourcePackage getOrCreateResource(String path, ResourcePackage root,
      Map<String, ResourcePath> path2resourceMap) {

    ResourcePath resource = path2resourceMap.get(path);
    if (resource != null) {
      if (resource instanceof ResourcePackage pkg) {
        return pkg;
      }
      return null;
    }
    int end = path.length();
    boolean folder = false;
    if (path.charAt(end - 1) == '/') {
      folder = true;
      end--;
    }
    int lastSlash = path.lastIndexOf('/', end - 1);
    String simpleName;
    ResourcePackage parent;
    ResourcePackage pkg = null;
    if (lastSlash < 0) {
      simpleName = path.substring(0, end);
      parent = root;
    } else {
      simpleName = path.substring(lastSlash + 1, end);
      String parentPath = path.substring(0, lastSlash + 1);
      parent = getOrCreateResource(parentPath, root, path2resourceMap);
    }
    if (folder) {
      pkg = new ResourcePackageImpl(this.moduleAccess, path, simpleName, parent);
      resource = pkg;
    } else {
      if (path.endsWith(".class")) {
        resource = new ResourceTypeImpl(this.moduleAccess, path, simpleName.substring(0, simpleName.length() - 6),
            parent);
      } else {
        resource = new ResourceFileImpl(this.moduleAccess, path, simpleName, parent);
      }
    }
    path2resourceMap.put(path, resource);
    return pkg;
  }

  @Override
  public Collection<String> getPaths() {

    return this.map.keySet();
  }

  @Override
  public Collection<ResourcePath> getAll() {

    return this.map.values();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends ResourcePath> T getByPath(String path) {

    return (T) this.map.get(path);
  }

}
