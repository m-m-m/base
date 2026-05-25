/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.base.resource.impl.NoResourceScannerService;

/**
 * Test of {@link ResourceScanner}.
 */
class ResourceScannerTest extends Assertions {

  @Test
  void testGetResult() {

    // arrange

    // act
    ResourceScanner scanner = ResourceScanner.get();
    ResourceScannerResult result = scanner.getResult();

    // assert
    assertThat(result).isNotNull();
    assertThat(result.get(NoResourceScannerService.class.getName())).isNull();
    // here we cannot easily test more since additional test services would need to be registered in the
    // module-info.java and maven does not support adding a test module-info.java in the same module.
    // Therefore we have to test it in a separate module.
  }
}
