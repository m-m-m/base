/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.temporal;

import java.util.function.BiFunction;

import io.github.mmm.base.temporal.impl.TemporalConverterProvider;

/**
 * Interface for a converter of temporal values.
 *
 * @see io.github.mmm.base.compare.CompareOperator
 * @see #get()
 */
public interface TemporalConverter {

  /**
   * @param <T> type of the result.
   * @param temporal1 the first temporal value (with date and/or time).
   * @param temporal2 the second temporal value (with date and/or time).
   * @param function the function to apply on the two given temporal values after assuring they are of same type. This
   *        method will therefore convert one of the given temporal values so it has the same type as the other one.
   * @return the result of the given {@link BiFunction} with the given temporal values applied or {@code null} if the a
   *         temporal value has an unknown or unsupported type.
   */
  <T> T convertAndEvaluate(Object temporal1, Object temporal2, BiFunction<?, ?, T> function);

  /**
   * @return the instance of this {@link TemporalConverter}. By default returns {@link TemporalConverterDefault}. In
   *         order to replace, simply define your own implementation (e.g. extending {@link TemporalConverterLegacy} if
   *         you want support for {@link java.util.Date} and {@link java.util.Calendar}) and declare
   *         <code>provides {@link io.github.mmm.base.temporal.TemporalConverter} with </code> followed by your own
   *         implementation in your {@code module-info.java} file.
   *
   */
  static TemporalConverter get() {

    return TemporalConverterProvider.CONVERTER;
  }

}
