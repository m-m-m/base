/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

import java.util.Objects;

/**
 * Implementation of {@link CharFilter} that combines multiple given {@link CharFilter}s with a logical OR so this
 * {@link ComposedCharFilter} will {@link #accept(int) accept} a {@link String#codePointAt(int) code-point} if any of
 * the given {@link CharFilter}s {@link #accept(int) accepts}.
 */
public class ComposedCharFilter extends AbstractCharFilter {

  private final CharFilter[] filters;

  /**
   * The constructor.
   *
   * @param filters the {@link CharFilter}s to compose using logical OR.
   */
  public ComposedCharFilter(CharFilter... filters) {

    super(null);
    Objects.requireNonNull(filters, "filters");
    this.filters = filters;
  }

  @Override
  public boolean accept(int codePoint) {

    for (CharFilter filter : this.filters) {
      if (filter.accept(codePoint)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected String computeDescription() {

    StringBuilder sb = new StringBuilder(this.filters.length * 4);
    for (CharFilter filter : this.filters) {
      sb.append(filter.getDescription());
    }
    return sb.toString();
  }

}
