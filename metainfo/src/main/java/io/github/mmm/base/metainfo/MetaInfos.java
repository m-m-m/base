/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

/// Annotation to define [MetaInfo].
///
/// @see MetaInfo#get(String)
/// @since 1.0.0
@Documented
@Retention(RUNTIME)
public @interface MetaInfos {

  /// @return the [MetaInfo] as [String]s in the form `«key»=«value»`. Each [String] needs to
  ///         contain an equals sign. The left-hand side to the (first) equals sign will be the key of the
  ///         meta-information and the right-hand side will be the [value][MetaInfo#get(String)] for that key.
  String[] value();

}
