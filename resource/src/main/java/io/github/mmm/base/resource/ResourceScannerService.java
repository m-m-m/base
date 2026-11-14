/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource;

/// Interface for a service (of [java.util.ServiceLoader]) allowing you to implement your own "plugin" for
/// [resource scanning][ResourceScanner]. Since scanning [all modules][ModuleScanner#getAll()] especially with
/// their classes can be very expensive, this interface allows other modules interested in dynamic scanning to register
/// as a service to a central scanning process performed only once during the bootstrapping of your Java application.
/// Ideally you even support AOT compilation for your productive usage so this only happens during development and build
/// time but can be omitted in production usage to speed up your startup times.
///
/// @since 1.0.0
public interface ResourceScannerService {

  /// This method can be used to ignore specific modules such as [internal ones][ModuleAccess#isInternalModule()] or
  /// only consider specific modules. This allows to speed up the scanning process significantly if you can ignore many
  /// modules entirely.
  ///
  /// @param module the [ModuleAccess].
  /// @return `true` if this service is interested to scan the given [module][ModuleAccess], `false`
  ///         otherwise.
  default boolean includeModule(ModuleAccess module) {

    return true;
  }

  /// @param resource the [ResourcePath] to introspect.
  /// @param resources the entire [ResourceMap] for dynamic lookup of other [ResourcePath]s from the same
  ///        module.
  /// @return `true` if the given [ResourcePath] was relevant for this service, `false` otherwise.
  boolean scan(ResourcePath resource, ResourceMap resources);

}
