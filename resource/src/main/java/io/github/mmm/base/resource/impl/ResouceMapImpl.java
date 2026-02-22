package io.github.mmm.base.resource.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.module.ModuleReader;
import java.lang.module.ResolvedModule;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
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
    final ResourcePackage root = ResourcePackageImpl.ofRoot(this.moduleAccess);
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
    if (resource == null) {
      if (ResourcePath.PATH_ROOT.equals(path)) {
        return root;
      }
      resource = createResource(path, parentPath -> getOrCreateResource(parentPath, root, path2resourceMap),
          this.moduleAccess);
      path2resourceMap.put(path, resource);
    }
    if (resource instanceof ResourcePackage pkg) {
      return pkg;
    }
    return null;
  }

  static ResourcePath getOrCreateResource(String path, Map<String, ResourcePath> path2resourceMap,
      ModuleAccess moduleAccess) {

    ResourcePath resource = path2resourceMap.get(path);
    if (resource == null) {
      resource = createResource(path,
          parentPath -> (ResourcePackage) getOrCreateResource(parentPath, path2resourceMap, moduleAccess),
          moduleAccess);
      path2resourceMap.put(path, resource);
    }
    return resource;
  }

  static ResourcePath createResource(String path, Function<String, ResourcePackage> packageProvider,
      ModuleAccess moduleAccess) {

    int end = path.length();
    boolean folder = false;
    if (path.charAt(end - 1) == '/') {
      folder = true;
      end--;
    }
    int lastSlash = path.lastIndexOf('/', end - 1);
    String simpleName;
    String parentPath = ResourcePath.PATH_ROOT;
    if (lastSlash < 0) {
      simpleName = path.substring(0, end);
    } else {
      simpleName = path.substring(lastSlash + 1, end);
      parentPath = path.substring(0, lastSlash + 1);
    }
    ResourcePackage parent = packageProvider.apply(parentPath);
    if (folder) {
      return new ResourcePackageImpl(moduleAccess, path, simpleName, parent);
    } else {
      if (path.endsWith(".class")) {
        return new ResourceTypeImpl(moduleAccess, path, simpleName.substring(0, simpleName.length() - 6), parent);
      } else {
        return new ResourceFileImpl(moduleAccess, path, simpleName, parent);
      }
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

  @SuppressWarnings("unchecked")
  @Override
  public <T extends ResourcePath> T getByPath(String path) {

    return (T) this.map.get(path);
  }

}
