/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Provides fundamental APIs and helper classes.
 *
 * @provides io.github.mmm.base.temporal.TemporalConverter
 * @provides io.github.mmm.base.exception.GlobalExceptionHandler
 * @uses io.github.mmm.base.exception.GlobalExceptionHandler
 * @uses io.github.mmm.base.temporal.TemporalConverter
 */
module io.github.mmm.base {

  requires static org.slf4j;

  uses io.github.mmm.base.temporal.TemporalConverter;

  provides io.github.mmm.base.temporal.TemporalConverter with //
      io.github.mmm.base.temporal.impl.TemporalConverterImpl;

  uses io.github.mmm.base.exception.GlobalExceptionHandler;

  provides io.github.mmm.base.exception.GlobalExceptionHandler
      with io.github.mmm.base.exception.GlobalExceptionHandlerSysout;

  exports io.github.mmm.base.collection;

  exports io.github.mmm.base.compare;

  exports io.github.mmm.base.config;

  exports io.github.mmm.base.exception;

  exports io.github.mmm.base.filter;

  exports io.github.mmm.base.i18n;

  exports io.github.mmm.base.io;

  exports io.github.mmm.base.justification;

  exports io.github.mmm.base.lang;

  exports io.github.mmm.base.number;

  exports io.github.mmm.base.properties;

  exports io.github.mmm.base.range;

  exports io.github.mmm.base.sort;

  exports io.github.mmm.base.temporal;

  exports io.github.mmm.base.text;

  exports io.github.mmm.base.uuid;

  exports io.github.mmm.base.variable;

}
