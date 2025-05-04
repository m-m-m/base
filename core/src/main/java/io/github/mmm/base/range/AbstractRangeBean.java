/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.range;

import java.util.Objects;

/**
 * Implementation of {@link WritableRange} for {@code UiNumericInput}.
 *
 * @param <V> type of the contained values.
 * @since 1.0.0
 */
public abstract class AbstractRangeBean<V extends Comparable<?>> extends AbstractRange<V> implements WritableRange<V> {

  private V min;

  private V max;

  /**
   * The constructor.
   */
  public AbstractRangeBean() {

    super();
  }

  /**
   * The constructor.
   *
   * @param min the {@link #getMin() minimum}.
   * @param max the {@link #getMax() maximum}.
   */
  public AbstractRangeBean(V min, V max) {

    super();
    this.min = min;
    this.max = max;
  }

  @Override
  public V getMin() {

    return this.min;
  }

  @Override
  public void setMin(V min) {

    if (Objects.equals(min, this.min)) {
      return;
    }
    this.min = min;
    onValueChange();
  }

  @Override
  public V getMax() {

    return this.max;
  }

  @Override
  public void setMax(V max) {

    if (Objects.equals(max, this.max)) {
      return;
    }
    this.max = max;
    onValueChange();
  }

  /**
   * Called whenever {@link #getMin() min} or {@link #getMax() max} changes.
   */
  protected void onValueChange() {

  }

  @Override
  public final Range<V> with(V minimum, V maximum) {

    return create(minimum, maximum);
  }

  @Override
  protected final Range<V> create(V minimum, V maximum) {

    setMin(minimum);
    setMax(maximum);
    return this;
  }

}
