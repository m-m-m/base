package io.github.mmm.base.resource;

import java.util.Collection;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.base.resource.impl.ModuleScannerImpl;

/// Fast and simple scanner of modules and their content (similar to classpath scanning before JPMS).
/// **ATTENTION**: Please consider using [ResourceScanner] over using [#getAll()] and calling methods like
/// [ModuleAccess#findResources()] if you want to do resource scanning efficiently during application
/// bootstrapping.
///
/// @since 1.0.0
public interface ModuleScanner {

  /// Gets [access to a module][ModuleAccess] by name. This is a cheap operation that only returns what is already
  /// there.
  ///
  /// @param name the [module name][Module#getName()] of the requested [ModuleAccess].
  /// @return the [ModuleAccess] or `null` if no such module exists.
  ModuleAccess get(String name);

  /// Gets [access to a module][ModuleAccess] by name. This is a cheap operation that only returns what is already
  /// there.
  ///
  /// @param name the [module name][Module#getName()] of the requested [ModuleAccess].
  /// @return the [ModuleAccess] or `null` if no such module exists.
  default ModuleAccess getRequired(String name) {

    ModuleAccess moduleAccess = get(name);
    if (moduleAccess == null) {
      throw new ObjectNotFoundException("Module", name);
    }
    return moduleAccess;
  }

  /// @return the unmodifiable [Collection] of [ModuleAccess] for all modules on your current module-path.
  ///         The sort order will be such that modules will always be before other modules that require it.
  Collection<ModuleAccess> getAll();

  /// @return the singleton instance of [ModuleScanner].
  public static ModuleScanner get() {

    return ModuleScannerImpl.INSTANCE;
  }

}
