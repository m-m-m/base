/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource;

/**
 * {@link ResourceScannerService} interested only in {@link ResourceType types} corresponding to a {@link Class}.
 *
 * @since 1.0.0
 */
public interface ResourceTypeScannerService extends ResourceScannerService {

  @Override
  default boolean scan(ResourcePath resource, ResourceMap resources) {

    if (resource.isType()) {
      return scanType((ResourceType) resource, resources);
    }
    return false;
  }

  /**
   * @param type the {@link ResourceType} to introspect. Try to exclude as much as you can based on
   *        {@link ResourceType#getPath() path} or {@link ResourceType#getName() name}, before calling
   *        {@link ResourceType#loadType()} and especially before calling {@link ResourceType#loadClass()}.
   * @param resources the entire {@link ResourceMap} for dynamic lookup of other {@link ResourcePath}s from the same
   *        module.
   * @return {@code true} if the given {@link ResourceType} was relevant for this service, {@code false} otherwise.
   */
  boolean scanType(ResourceType type, ResourceMap resources);
}
