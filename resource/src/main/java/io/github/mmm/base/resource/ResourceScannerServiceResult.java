/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource;

import java.util.stream.Stream;

/// Interface for the result of [ResourceScannerService].
///
/// @since 1.0.0
public interface ResourceScannerServiceResult {

  /// @return the [qualified name][Class#getName()] of the [ResourceScannerService].
  String getServiceClassName();

  /// @param serviceName the [qualified name][Class#getName()] of the [ResourceScannerService].
  /// @return the [Stream] of [resource paths][ResourcePath#getPath()] provided to the specified
  ///         [ResourceScannerService].
  Stream<String> getResourcePaths();

  /// @return the [Stream] of [Class]es provided to the specified [ResourceScannerService].
  Stream<Class<?>> getClasses();

}
