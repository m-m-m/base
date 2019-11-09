/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

import java.util.Objects;

/**
 * Implementation of {@link CharFilter} that combines multiple given {@link CharFilter}s with a logical OR so that if
 * any of the given {@link CharFilter}s {@link #accept(char) matches} then this {@link ComposedCharFilter} will match.
 */
public class ComposedCharFilter implements CharFilter {

  private final CharFilter[] filters;

  /**
   * The constructor.
   *
   * @param filters the {@link CharFilter}s to compose using logical OR.
   */
  public ComposedCharFilter(CharFilter... filters) {

    super();
    Objects.requireNonNull(filters, "filters");
    this.filters = filters;
  }

  @Override
  public boolean accept(char c) {

    for (CharFilter filter : this.filters) {
      if (filter.accept(c)) {
        return true;
      }
    }
    return false;
  }

}
