/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

import java.util.Objects;

/// Implementation of [CharFilter] resulting in the negation of a given [CharFilter] so it rejects what the
/// given [CharFilter] [accepts][#accept(int)] and vice versa.
public class NegatedCharFilter extends AbstractCharFilter {

  private final CharFilter filter;

  /// The constructor.
  ///
  /// @param filter the [CharFilter] to negate (invert).
  public NegatedCharFilter(CharFilter filter) {

    super(null);
    Objects.requireNonNull(filter, "filter");
    this.filter = filter;
  }

  @Override
  public boolean accept(int codePoint) {

    return !this.filter.accept(codePoint);
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
