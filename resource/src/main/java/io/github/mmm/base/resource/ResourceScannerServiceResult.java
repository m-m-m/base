/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource;

import java.util.stream.Stream;

/**
 * Interface for the result of {@link ResourceScannerService}.
 *
 * @since 1.0.0
 */
public interface ResourceScannerServiceResult {

  /**
   * @return the {@link Class#getName() qualified name} of the {@link ResourceScannerService}.
   */
  String getServiceClassName();

  /**
   * @param serviceName the {@link Class#getName() qualified name} of the {@link ResourceScannerService}.
   * @return the {@link Stream} of {@link ResourcePath#getPath() resource paths} provided to the specified
   *         {@link ResourceScannerService}.
   */
  Stream<String> getResourcePaths();

  /**
   * @return the {@link Stream} of {@link Class}es provided to the specified {@link ResourceScannerService}.
   */
  Stream<Class<?>> getClasses();

}
