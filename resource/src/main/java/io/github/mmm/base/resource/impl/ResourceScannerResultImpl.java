/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.github.mmm.base.collection.ReadOnlyIterator;
import io.github.mmm.base.resource.ResourceScannerResult;
import io.github.mmm.base.resource.ResourceScannerService;
import io.github.mmm.base.resource.ResourceScannerServiceResult;

/**
 * Implementation of {@link ResourceScannerResult}
 */
public class ResourceScannerResultImpl implements ResourceScannerResult {

  private Map<String, AbstractResourceScannerServiceResult> map;

  /**
   * The constructor.
   */
  public ResourceScannerResultImpl() {

    this.map = new HashMap<>();
  }

  @Override
  public Iterator<ResourceScannerServiceResult> iterator() {

    return new ReadOnlyIterator<>(this.map.values().iterator());
  }

  @Override
  public AbstractResourceScannerServiceResult get(String serviceName) {

    return this.map.get(serviceName);
  }

  /**
   * @param serviceName the {@link Class#getName() qualified name} of the {@link ResourceScannerService}.
   * @return the corresponding {@link ResourceScannerServiceResult}. Will be created if not found.
   */
  public ResourceScannerServiceResultResources getOrCreate(String serviceName) {

    return (ResourceScannerServiceResultResources) this.map.computeIfAbsent(serviceName,
        ResourceScannerServiceResultResources::new);
  }

  /**
   * Ensures this object is read-only (immutable).
   */
  public void makeReadOnly() {

    this.map.values().forEach(AbstractResourceScannerServiceResult::makeReadOnly);
    this.map = Map.copyOf(this.map);
  }

  @Override
  public String toString() {

    return this.map.toString();
  }

}
