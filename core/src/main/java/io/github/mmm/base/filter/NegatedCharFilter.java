/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

import java.util.Objects;

/**
 * Implementation of {@link CharFilter} resulting in the negation of a given {@link CharFilter} so it filters what the
 * given {@link CharFilter} {@link #accept(char) accepts} and vice versa.
 */
public class NegatedCharFilter extends AbstractCharFilter {

  private final CharFilter filter;

  /**
   * The constructor.
   *
   * @param filter the {@link CharFilter} to negate (invert).
   */
  public NegatedCharFilter(CharFilter filter) {

    super(null);
    Objects.requireNonNull(filter, "filter");
    this.filter = filter;
  }

  @Override
  public boolean accept(char c) {

    return !this.filter.accept(c);
  }

  @Override
  public CharFilter negate() {

    return this.filter;
  }

  @Override
  protected String computeDescription() {

    return "!" + this.filter.getDescription();
  }

}
