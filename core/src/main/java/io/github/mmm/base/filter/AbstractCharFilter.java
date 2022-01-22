package io.github.mmm.base.filter;

/**
 * Abstract base implementation of {@link CharFilter}.
 *
 * @since 1.0.0
 */
public abstract class AbstractCharFilter implements CharFilter {

  private String description;

  /**
   * The constructor.
   *
   * @param description the {@link #getDescription() description}.
   */
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

  /**
   * @return the computed {@link #getDescription() description}. Will be called once on the first call of
   *         {@link #getDescription()} in case {@code null} was given at {@link #AbstractCharFilter(String) construction
   *         time} for lazy initialization. In such case this method has to be overridden.
   */
  protected String computeDescription() {

    throw new IllegalStateException("No description provided and computeDescription not implemented.");
  }

  @Override
  public String toString() {

    return getDescription();
  }

}
