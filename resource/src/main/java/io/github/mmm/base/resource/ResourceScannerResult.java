/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource;

import io.github.mmm.base.exception.ObjectNotFoundException;

/// Interface for the result of [ResourceScanner].
///
/// @since 1.0.0
public interface ResourceScannerResult extends Iterable<ResourceScannerServiceResult> {

  /// @param serviceName the [qualified name][Class#getName()] of the [ResourceScannerService].
  /// @return the corresponding [ResourceScannerServiceResult] or `null` the given `serviceName` was
  ///         not provided.
  ResourceScannerServiceResult get(String serviceName);

  /// @param service the [Class] reflecting the [ResourceScannerService].
  /// @return the corresponding [ResourceScannerServiceResult] or `null` the given `service` was not
  ///         provided.
  default ResourceScannerServiceResult get(Class<? extends ResourceScannerService> service) {

    return get(service.getName());
  }

  /// @param service the [Class] reflecting the [ResourceScannerService].
  /// @return the corresponding [ResourceScannerServiceResult] or `null` the given `service` was not
  ///         provided.
  default ResourceScannerServiceResult getRequired(Class<? extends ResourceScannerService> service) {

    ResourceScannerServiceResult result = get(service);
    if (result == null) {
      throw new ObjectNotFoundException(ResourceScannerServiceResult.class.getSimpleName(), service);
    }
    return result;
  }

}
