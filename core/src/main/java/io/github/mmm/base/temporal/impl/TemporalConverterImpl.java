/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.temporal.impl;

import java.util.function.BiFunction;

import io.github.mmm.base.temporal.TemporalConverter;
import io.github.mmm.base.temporal.TemporalConverterDefault;

/**
 * Implementation of {@link TemporalConverter}.
 *
 * @since 1.0.0
 */
public class TemporalConverterImpl implements TemporalConverter {

  @Override
  public <T> T convertAndEvaluate(Object temporal1, Object temporal2, BiFunction<?, ?, T> function) {

    return TemporalConverterDefault.get().convertAndEvaluate(temporal1, temporal2, function);
  }

}
