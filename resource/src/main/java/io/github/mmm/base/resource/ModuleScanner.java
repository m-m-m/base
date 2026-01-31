package io.github.mmm.base.resource;

import java.util.Collection;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.base.resource.impl.ModuleScannerImpl;

/**
 * Fast and simple scanner of modules and their content (similar to classpath scanning before JPMS).
 */
public interface ModuleScanner {

  /**
   * Gets {@link ModuleAccess access to a module} by name. This is a cheap operation that only returns what is already
   * there.
   *
   * @param name the {@link Module#getName() module name} of the requested {@link ModuleAccess}.
   * @return the {@link ModuleAccess} or {@code null} if no such module exists.
   */
  ModuleAccess get(String name);

  /**
   * Gets {@link ModuleAccess access to a module} by name. This is a cheap operation that only returns what is already
   * there.
   *
   * @param name the {@link Module#getName() module name} of the requested {@link ModuleAccess}.
   * @return the {@link ModuleAccess} or {@code null} if no such module exists.
   */
  default ModuleAccess getRequired(String name) {

    ModuleAccess moduleAccess = get(name);
    if (moduleAccess == null) {
      throw new ObjectNotFoundException("Module", name);
    }
    return moduleAccess;
  }

  /**
   * @return the unmodifiable {@link Collection} of {@link ModuleAccess} for all modules on your current module-path.
   *         The sort order will be such that modules will always be before other modules that require it.
   */
  Collection<ModuleAccess> getAll();

  /**
   * @return the singleton instance of {@link ModuleScanner}.
   */
  public static ModuleScanner get() {

    return ModuleScannerImpl.INSTANCE;
  }

}
