/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.lang;

/**
 * Abstract base implementation of {@link ToString}.
 *
 * @since 1.0.0
 */
public abstract class AbstractToString implements ToString {

  @Override
  public final String toString() {

    StringBuilder sb = new StringBuilder();
    toString(sb);
    return sb.toString();
  }

}
