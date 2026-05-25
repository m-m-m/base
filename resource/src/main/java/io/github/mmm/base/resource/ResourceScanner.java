/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource;

import io.github.mmm.base.resource.impl.ResourceScannerImpl;

/**
 * Interface for a component that actually triggers the resource scanning. Multiple invocations of {@link #get()} or
 * {@link #getResult()} will only return what is already there. The actual scanning will be performed only once.
 *
 * @see ResourceScannerService
 * @see ModuleScanner
 * @see ResourcePath
 */
public interface ResourceScanner {

  /**
   * @return the {@link ResourceScannerResult result} of the resource scanning.
   */
  ResourceScannerResult getResult();

  /**
   * @return the instance of {@link ResourceScanner}.
   */
  static ResourceScanner get() {

    return ResourceScannerImpl.INSTANCE;
  }

}
