/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource;

/// [ResourceScannerService] interested only in [types][ResourceType] corresponding to a [Class].
///
/// @since 1.0.0
public interface ResourceTypeScannerService extends ResourceScannerService {

  @Override
  default boolean scan(ResourcePath resource, ResourceMap resources) {

    if (resource.isType()) {
      return scanType((ResourceType) resource, resources);
    }
    return false;
  }

  /// @param type the [ResourceType] to introspect. Try to exclude as much as you can based on
  ///        [path][ResourceType#getPath()] or [name][ResourceType#getName()], before calling
  ///        [ResourceType#loadType()] and especially before calling [ResourceType#loadClass()].
  /// @param resources the entire [ResourceMap] for dynamic lookup of other [ResourcePath]s from the same
  ///        module.
  /// @return `true` if the given [ResourceType] was relevant for this service, `false` otherwise.
  boolean scanType(ResourceType type, ResourceMap resources);
}
