/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

/**
 * Annotation to define {@link MetaInfo}.
 *
 * @see MetaInfo#get(String)
 * @since 1.0.0
 */
@Documented
@Retention(RUNTIME)
public @interface MetaInfos {

  /**
   * @return the {@link MetaInfo} as {@link String}s in the form {@code «key»=«value»}. Each {@link String} needs to
   *         contain an equals sign. The left-hand side to the (first) equals sign will be the key of the
   *         meta-information and the right-hand side will be the {@link MetaInfo#get(String) value} for that key.
   */
  String[] value();

}
