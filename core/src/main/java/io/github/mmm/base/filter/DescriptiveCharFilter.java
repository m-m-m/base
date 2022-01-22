package io.github.mmm.base.filter;

import java.util.Objects;

/**
 * Wrapper of {@link CharFilter} with custom {@link CharFilter#getDescription() description}.
 *
 * @since 1.0.0
 */
class DescriptiveCharFilter extends AbstractCharFilter {

  private CharFilter filter;

  /**
   * The constructor.
   *
   * @param filter the {@link CharFilter} to {@link #accept(char) wrap} and describe.
   * @param description the {@link #getDescription() description}.
   */
  DescriptiveCharFilter(CharFilter filter, String description) {

    super(description);
    Objects.requireNonNull(filter);
    if ((description == null) || description.isBlank()) {
      throw new IllegalArgumentException();
    }
    this.filter = filter;
  }

  @Override
  public boolean accept(char c) {

    return this.filter.accept(c);
  }

}
