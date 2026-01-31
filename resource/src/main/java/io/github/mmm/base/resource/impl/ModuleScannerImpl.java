package io.github.mmm.base.resource.impl;

import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleDescriptor.Requires;
import java.lang.module.ResolvedModule;
import java.lang.reflect.AccessFlag;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ModuleScanner;
import io.github.mmm.base.resource.ResourceMap;

/**
 * Fast and simple scanner of modules and their content (similar to classpath scanning before JPMS).
 */
public final class ModuleScannerImpl implements ModuleScanner {

  private static final Logger LOG = LoggerFactory.getLogger(ModuleScannerImpl.class);

  /** @see ModuleScanner#get() */
  public static final ModuleScannerImpl INSTANCE = new ModuleScannerImpl();

  private final ModuleLayer moduleLayer;

  private final Map<String, ModuleAccess> name2ModuleMap;

  private final Collection<ModuleAccess> all;

  private ModuleScannerImpl() {

    super();
    this.moduleLayer = ModuleLayer.boot();
    this.name2ModuleMap = new HashMap<>();
    Set<ModuleAccess> allModulesSorted = new LinkedHashSet<>();
    try (Stream<ResolvedModule> stream = this.moduleLayer.configuration().modules().stream()) {
      Iterator<ResolvedModule> moduleIterator = stream.iterator();
      while (moduleIterator.hasNext()) {
        ResolvedModule module = moduleIterator.next();
        this.name2ModuleMap.put(module.name(), new ModuleAccessImpl(module));
      }
    }
    for (Module module : this.moduleLayer.modules()) {
      LOG.debug("Found module {}", module.getName());
      ModuleAccessImpl moduleAccess = (ModuleAccessImpl) this.name2ModuleMap.get(module.getName());
      if (moduleAccess != null) {
        moduleAccess.module = module;
        insertModuleSortedByDependencies(moduleAccess, allModulesSorted);
      }
    }
    this.all = Collections.unmodifiableCollection(allModulesSorted); // Set.copyOf does not guarantee to preserve order
  }

  private void insertModuleSortedByDependencies(ModuleAccess module, Set<ModuleAccess> allModulesSorted) {

    if (allModulesSorted.contains(module)) {
      return;
    }
    Set<Requires> requires = module.getResolved().reference().descriptor().requires();
    if (!requires.isEmpty()) {
      for (Requires req : requires) {
        String name = req.name();
        ModuleAccess requiredModule = this.name2ModuleMap.get(name);
        if (requiredModule != null) {
          insertModuleSortedByDependencies(requiredModule, allModulesSorted);
        } else if (req.accessFlags().contains(AccessFlag.STATIC)) { // for modules "requires static" means optional
          LOG.debug("Module {} requires module {} that was not found.", module.getResolved().name(), name);
        }
      }
    }
    allModulesSorted.add(module);
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

    return this.all;
  }

  private static class ModuleAccessImpl implements ModuleAccess {

    private final ResolvedModule resolved;

    private Module module;

    private Set<String> exports;

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
    public boolean isOpen(String packageName) {

      ModuleDescriptor descriptor = this.module.getDescriptor();
      if (descriptor.isOpen() || descriptor.isAutomatic()) {
        return true;
      }
      // module.isOpen(packageName) has a bug in JDK so we need this workaround...
      if (this.exports == null) {
        this.exports = descriptor.exports().stream().map(e -> e.source()).collect(Collectors.toSet());
      }
      return this.exports.contains(packageName);
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
