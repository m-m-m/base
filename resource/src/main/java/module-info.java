/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Provides fundamental APIs and helpers to find and load resources or classes dynamically.
 *
 * @uses io.github.mmm.base.resource.ResourceScannerService
 * @provides io.github.mmm.base.resource.ResourceScannerService
 */
module io.github.mmm.base.resource {

  requires io.github.mmm.base;

  requires org.slf4j;

  uses io.github.mmm.base.resource.ResourceScannerService;

  provides io.github.mmm.base.resource.ResourceScannerService //
      with io.github.mmm.base.resource.impl.NoResourceScannerService;

  exports io.github.mmm.base.resource;

  exports io.github.mmm.base.type;

}
