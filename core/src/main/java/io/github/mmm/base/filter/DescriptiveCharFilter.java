package io.github.mmm.base.filter;

import java.util.Objects;

/// Wrapper of [CharFilter] with custom [description][CharFilter#getDescription()].
///
/// @since 1.0.0
class DescriptiveCharFilter extends AbstractCharFilter {

  private CharFilter filter;

  /// The constructor.
  ///
  /// @param filter the [CharFilter] to [wrap][#accept(char)] and describe.
  /// @param description the [description][#getDescription()].
  DescriptiveCharFilter(CharFilter filter, String description) {

    super(description);
    Objects.requireNonNull(filter);
    if ((description == null) || description.isBlank()) {
      throw new IllegalArgumentException();
    }
    this.filter = filter;
  }

  @Override
  public boolean accept(int codePoint) {

    return this.filter.accept(codePoint);
  }

}
