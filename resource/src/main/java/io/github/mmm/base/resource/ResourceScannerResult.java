/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource;

import io.github.mmm.base.exception.ObjectNotFoundException;

/**
 * Interface for the result of {@link ResourceScanner}.
 *
 * @since 1.0.0
 */
public interface ResourceScannerResult extends Iterable<ResourceScannerServiceResult> {

  /**
   * @param serviceName the {@link Class#getName() qualified name} of the {@link ResourceScannerService}.
   * @return the corresponding {@link ResourceScannerServiceResult} or {@code null} the given {@code serviceName} was
   *         not provided.
   */
  ResourceScannerServiceResult get(String serviceName);

  /**
   * @param service the {@link Class} reflecting the {@link ResourceScannerService}.
   * @return the corresponding {@link ResourceScannerServiceResult} or {@code null} the given {@code service} was not
   *         provided.
   */
  default ResourceScannerServiceResult get(Class<? extends ResourceScannerService> service) {

    return get(service.getName());
  }

  /**
   * @param service the {@link Class} reflecting the {@link ResourceScannerService}.
   * @return the corresponding {@link ResourceScannerServiceResult} or {@code null} the given {@code service} was not
   *         provided.
   */
  default ResourceScannerServiceResult getRequired(Class<? extends ResourceScannerService> service) {

    ResourceScannerServiceResult result = get(service);
    if (result == null) {
      throw new ObjectNotFoundException(ResourceScannerServiceResult.class.getSimpleName(), service);
    }
    return result;
  }

}
