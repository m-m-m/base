/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import io.github.mmm.base.resource.ResourcePath;
import io.github.mmm.base.resource.ResourceType;

/**
 * Implementation of {@link AbstractResourceScannerServiceResult} for {@link ResourcePath}s.
 *
 * @since 1.0.0
 */
public class ResourceScannerServiceResultResources extends AbstractResourceScannerServiceResult {

  private List<ResourcePath> resources;

  /**
   * The constructor.
   *
   * @param serviceClassName the {@link #getServiceClassName() serviceClassName}
   */
  public ResourceScannerServiceResultResources(String serviceClassName) {

    super(serviceClassName);
    this.resources = new ArrayList<>();
  }

  @Override
  public Stream<String> getResourcePaths() {

    return this.resources.stream().map(ResourcePath::getPath);
  }

  @Override
  public Stream<Class<?>> getClasses() {

    // type interference bug in EJC?
    Stream<Class<?>> map = this.resources.stream().map(r -> ResourceType.loadClass(r));
    return map.filter(c -> c != null);
  }

  /**
   * @param resource the {@link ResourcePath} to add. This method will fail if {@link #makeReadOnly() read-only}.
   */
  public void add(ResourcePath resource) {

    this.resources.add(resource);
  }

  @Override
  public void makeReadOnly() {

    this.resources = List.copyOf(this.resources);
  }

}
