/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.resource.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ModuleScanner;
import io.github.mmm.base.resource.ResourceMap;
import io.github.mmm.base.resource.ResourcePath;
import io.github.mmm.base.resource.ResourceScanner;
import io.github.mmm.base.resource.ResourceScannerResult;
import io.github.mmm.base.resource.ResourceScannerService;
import io.github.mmm.base.service.ServiceHelper;

/**
 * Implementation of {@link ResourceScanner}.
 *
 * @since 1.0.0
 */
public class ResourceScannerImpl implements ResourceScanner {

  private static final Logger LOG = LoggerFactory.getLogger(ResourceScannerImpl.class);

  /** The singleton instance. */
  public static final ResourceScannerImpl INSTANCE = new ResourceScannerImpl();

  private final List<ResourceScannerService> scanners;

  private final ResourceScannerResult result;

  /**
   * The constructor.
   */
  public ResourceScannerImpl() {

    this.scanners = new ArrayList<>();
    ServiceHelper.all(ServiceLoader.load(ResourceScannerService.class), this.scanners);
    ResourceScannerResultImpl resultImpl = new ResourceScannerResultImpl();
    for (ModuleAccess module : ModuleScanner.get().getAll()) {
      ResourceMap resources = null;
      for (ResourceScannerService scanner : this.scanners) {
        if (scanner.includeModule(module)) {
          if (resources == null) {
            LOG.debug("Scanning module {}...", module);
            resources = module.findResources();
          }
          String serviceName = scanner.getClass().getName();
          ResourceScannerServiceResultResources serviceResult = resultImpl.getOrCreate(serviceName);
          for (ResourcePath resource : resources.getAll()) {
            boolean accepted = scanner.scan(resource, resources);
            if (accepted) {
              LOG.trace("Accepted resource {} by {}...", resource, serviceName);
              serviceResult.add(resource);
            }
          }
        }
      }
    }
    resultImpl.makeReadOnly();
    this.result = resultImpl;
  }

  @Override
  public ResourceScannerResult getResult() {

    return this.result;
  }

}
