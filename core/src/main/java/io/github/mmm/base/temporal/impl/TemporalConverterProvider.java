/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.temporal.impl;

import java.util.ServiceLoader;

import io.github.mmm.base.service.ServiceHelper;
import io.github.mmm.base.temporal.TemporalConverter;

/**
 * Provides the {@link TemporalConverter}.
 */
public class TemporalConverterProvider {

  /** The {@link TemporalConverter} instance. */
  public static final TemporalConverter CONVERTER = ServiceHelper.singleton(ServiceLoader.load(TemporalConverter.class),
      false);

}
