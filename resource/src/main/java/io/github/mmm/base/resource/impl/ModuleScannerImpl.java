package io.github.mmm.base.resource.impl;

import java.lang.module.ResolvedModule;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ModuleScanner;
import io.github.mmm.base.resource.ResourceMap;

/**
 * Fast and simple scanner of modules and their content (similar to classpath scanning before JPMS).
 */
public final class ModuleScannerImpl implements ModuleScanner {

  /** @see ModuleScanner#get() */
  public static final ModuleScannerImpl INSTANCE = new ModuleScannerImpl();

  private final ModuleLayer moduleLayer;

  private final Map<String, ModuleAccess> name2ModuleMap;

  private ModuleScannerImpl() {

    super();
    this.moduleLayer = ModuleLayer.boot();
    Map<String, ModuleAccess> map = new HashMap<>();
    try (Stream<ResolvedModule> stream = this.moduleLayer.configuration().modules().stream()) {
      Iterator<ResolvedModule> moduleIterator = stream.iterator();
      while (moduleIterator.hasNext()) {
        ResolvedModule module = moduleIterator.next();
        map.put(module.name(), new ModuleAccessImpl(module));
      }
    }
    for (Module module : this.moduleLayer.modules()) {
      ModuleAccessImpl moduleAccess = (ModuleAccessImpl) map.get(module.getName());
      if (moduleAccess != null) {
        moduleAccess.module = module;
      }
    }
    this.name2ModuleMap = Map.copyOf(map);
  }

  @Override
  public ModuleAccess get(String name) {

    return this.name2ModuleMap.get(name);
  }

  @Override
  public ModuleAccess getRequired(String name) {

    ModuleAccess moduleAccess = get(name);
    if (moduleAccess == null) {
      throw new ObjectNotFoundException("Module", name);
    }
    return moduleAccess;
  }

  @Override
  public Collection<ModuleAccess> getAll() {

    return this.name2ModuleMap.values();
  }

  private static class ModuleAccessImpl implements ModuleAccess {

    private final ResolvedModule resolved;

    private Module module;

    ModuleAccessImpl(ResolvedModule resolved) {

      super();
      this.resolved = resolved;
    }

    @Override
    public ResolvedModule getResolved() {

      return this.resolved;
    }

    @Override
    public Module get() {

      return this.module;
    }

    @Override
    public ResourceMap findResources() {

      return new ResouceMapImpl(this);
    }

    @Override
    public String toString() {

      return this.module.getName();
    }

  }
}
