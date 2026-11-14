/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/// Implementation of [AbstractResourceScannerServiceResult] for [Class]es.
///
/// @since 1.0.0
public class ResourceScannerServiceResultClasses extends AbstractResourceScannerServiceResult {

  private List<Class<?>> classes;

  /// The constructor.
  ///
  /// @param serviceClassName the [serviceClassName][#getServiceClassName()]
  public ResourceScannerServiceResultClasses(String serviceClassName) {

    super(serviceClassName);
    this.classes = new ArrayList<>();
  }

  @Override
  public Stream<Class<?>> getClasses() {

    return this.classes.stream();
  }

  @Override
  public Stream<String> getResourcePaths() {

    return this.classes.stream().map(Class::getName);
  }

  /// @param type the [Class] to add. This method will fail if [read-only][#makeReadOnly()].
  public void add(Class<?> type) {

    this.classes.add(type);
  }

  @Override
  public void makeReadOnly() {

    this.classes = List.copyOf(this.classes);
  }

}
