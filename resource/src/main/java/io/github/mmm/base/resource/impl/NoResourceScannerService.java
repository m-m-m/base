/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource.impl;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourceMap;
import io.github.mmm.base.resource.ResourcePath;
import io.github.mmm.base.resource.ResourceScannerService;

/**
 * Implementation of {@link ResourceScannerService} that does nothing and ignores all modules and resources. Used so
 * service loader will not fail if no other implementation of {@link ResourceScannerService} is registered.
 *
 * @since 1.0.0
 */
public class NoResourceScannerService implements ResourceScannerService {

  @Override
  public boolean includeModule(ModuleAccess module) {

    return false;
  }

  @Override
  public boolean scan(ResourcePath resource, ResourceMap resources) {

    return false;
  }

}
