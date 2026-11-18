package io.github.mmm.base.filter;

/// Abstract base implementation of [CharFilter].
///
/// @since 1.0.0
public abstract class AbstractCharFilter implements CharFilter {

  private String description;

  /// The constructor.
  ///
  /// @param description the [description][#getDescription()].
  public AbstractCharFilter(String description) {

    super();
    this.description = description;
  }

  @Override
  public String getDescription() {

    if (this.description == null) {
      this.description = computeDescription();
    }
    return this.description;
  }

  /// @return the computed [description][#getDescription()]. Will be called once on the first call of
  ///         [#getDescription()] in case `null` was given at [construction time][#AbstractCharFilter(String)] for lazy
  ///         initialisation. In such case this method has to be overridden.
  protected String computeDescription() {

    throw new IllegalStateException("No description provided and computeDescription not implemented.");
  }

  @Override
  public String toString() {

    return getDescription();
  }

}
