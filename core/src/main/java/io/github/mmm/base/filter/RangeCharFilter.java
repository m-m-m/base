/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

/**
 * {@link CharFilter} that only {@link #accept(int) accepts} characters in a range from {@link #getMin() min} to
 * {@link #getMax() max}.
 *
 * @since 1.0.0
 */
public class RangeCharFilter extends AbstractCharFilter {

  private final int min;

  private final int max;

  /**
   * The constructor.
   *
   * @param min the {@link #getMin() minimum} {@link #accept(int) accepted} character.
   * @param max the {@link #getMax() maximum} {@link #accept(int) accepted} character.
   */
  public RangeCharFilter(int min, int max) {

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
  public boolean accept(int codePoint) {

    return (codePoint >= this.min) && (codePoint <= this.max);
  }

  /**
   * @return the minimum {@link #accept(int) accepted} character.
   */
  public int getMin() {

    return this.min;
  }

  /**
   * @return the maximum {@link #accept(int) accepted} character.
   */
  public int getMax() {

    return this.max;
  }

  @Override
  public CharFilter compose(CharFilter filter) {

    if (filter instanceof RangeCharFilter) {
      RangeCharFilter range = (RangeCharFilter) filter;
      int newMin = this.min;
      if (range.min < newMin) {
        newMin = range.min;
      }
      int newMax = this.max;
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
