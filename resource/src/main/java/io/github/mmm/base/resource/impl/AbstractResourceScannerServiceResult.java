/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource.impl;

import java.util.Iterator;

import io.github.mmm.base.resource.ResourceScannerServiceResult;

/**
 * Abstract base implementation of {@link ResourceScannerServiceResult}.
 */
public abstract class AbstractResourceScannerServiceResult implements ResourceScannerServiceResult {

  private final String serviceClassName;

  /**
   * The constructor.
   *
   * @param serviceClassName the {@link #getServiceClassName() serviceClassName}
   */
  public AbstractResourceScannerServiceResult(String serviceClassName) {

    super();
    this.serviceClassName = serviceClassName;
  }

  @Override
  public String getServiceClassName() {

    return this.serviceClassName;
  }

  /**
   * Ensures this object is read-only (immutable).
   */
  public abstract void makeReadOnly();

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder("Result of ");
    sb.append(this.serviceClassName);
    sb.append(":");
    Iterator<String> iterator = getResourcePaths().iterator();
    String separator = " ";
    while (iterator.hasNext()) {
      String resourceName = iterator.next();
      sb.append(separator);
      sb.append(resourceName);
      separator = ", ";
    }
    return sb.toString();
  }
}
