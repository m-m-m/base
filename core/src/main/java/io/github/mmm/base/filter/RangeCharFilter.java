/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

/**
 * {@link CharFilter} that only {@link #accept(char) accepts} characters in a range from {@link #getMin() min} to
 * {@link #getMax() max}.
 *
 * @since 1.0.0
 */
public class RangeCharFilter extends AbstractCharFilter {

  private final char min;

  private final char max;

  /**
   * The constructor.
   *
   * @param min the {@link #getMin() minimum} {@link #accept(char) accepted} character.
   * @param max the {@link #getMax() maximum} {@link #accept(char) accepted} character.
   */
  public RangeCharFilter(char min, char max) {

    super(null);
    this.min = min;
    this.max = max;
    if (min > max) {
      throw new IllegalArgumentException(getDescription());
    }
  }

  @Override
  protected String computeDescription() {

    StringBuilder sb = new StringBuilder(8);
    sb.append('[');
    CharFilter.append(this.min, sb);
    sb.append('-');
    CharFilter.append(this.max, sb);
    sb.append(']');
    return sb.toString();
  }

  @Override
  public boolean accept(char c) {

    return (c >= this.min) && (c <= this.max);
  }

  /**
   * @return the minimum {@link #accept(char) accepted} character.
   */
  public char getMin() {

    return this.min;
  }

  /**
   * @return the maximum {@link #accept(char) accepted} character.
   */
  public char getMax() {

    return this.max;
  }

  @Override
  public CharFilter compose(CharFilter filter) {

    if (filter instanceof RangeCharFilter) {
      RangeCharFilter range = (RangeCharFilter) filter;
      char newMin = this.min;
      if (range.min < newMin) {
        newMin = range.min;
      }
      char newMax = this.max;
      if (range.max > newMax) {
        newMax = range.max;
      }
      long thisLength = this.max - this.min;
      long rangeLength = range.max - range.min;
      long newLength = newMax - newMin;
      if (newLength <= (thisLength + rangeLength + 1)) {
        if ((newMin == this.min) && (newMax == this.max)) {
          return this;
        } else if ((newMin == range.min) && (newMax == range.max)) {
          return range;
        }
        return new RangeCharFilter(newMin, newMax);
      }
    }
    return super.compose(filter);
  }

}
