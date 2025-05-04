package io.github.mmm.base.range;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.github.mmm.base.number.NumberType;

/**
 * Extends {@link Range} for {@link Number} types.
 *
 * @param <N> type of the contained {@link Number} values.
 */
public interface NumberRange<N extends Number & Comparable<?>> extends Range<N> {

  /**
   * @return the {@link NumberType} of this range. May be {@code null} if unbounded.
   */
  @SuppressWarnings("unchecked")
  default NumberType<N> getType() {

    N min = getMin();
    if (min != null) {
      return (NumberType<N>) NumberType.of(min.getClass());
    } else {
      N max = getMax();
      if (max != null) {
        return (NumberType<N>) NumberType.of(max.getClass());
      }
    }
    return null;
  }

  /**
   * This method {@link NumberType#wrap(Number, Number, Number) wraps} the given {@code value} so the result is
   * {@link #contains(Comparable) contained} in this {@link Range} unless the given {@code value} is {@code null}.
   *
   * @param value is the vale to clip. May be {@code null}.
   * @return the given {@code value} {@link NumberType#wrap(Number, Number, Number) wrapped} to this range.
   */
  default N wrap(N value) {

    if (value == null) {
      return null;
    }
    N min = getMin();
    N max = getMax();
    if ((min == null) && (max == null)) {
      return value;
    }
    @SuppressWarnings("unchecked")
    NumberType<N> type = (NumberType<N>) NumberType.of(value.getClass());
    if (type == null) {
      return clip(value);
    }
    return type.wrap(value, min, max);
  }

  /**
   * @param value is the vale to clip. May be {@code null}.
   * @param wrap - {@code true} for {@link #wrap(Number)}, {@code false} for {@link #clip(Comparable)}.
   * @return {@link #clip(Comparable) clip} if {@code wrap} was {@code false} and {@link #wrap(Number) wrap} otherwise
   *         (if {@code wrap} was {@code true}).
   */
  default N clip(N value, boolean wrap) {

    if (wrap) {
      return wrap(value);
    }
    return clip(value);
  }

  /**
   * @param factor the scaled value in the range from {@code 0} to {@code 1}.
   * @return the value within this range scaled by the given {@code factor}. It will be {@link #getMin() min} in case
   *         the given {@code factor} is {@code 0}, while {@code 0} is used in case {@link #getMin() min} is
   *         {@code null}. In case the {@code factor} is {@code 1}, it will return {@link #getMax() max} or the maximum
   *         value instead of {@code null}. Any other factor is interpolated between {@link #getMin() min} and
   *         {@link #getMax() max}.
   */
  default N fromFactor(double factor) {

    N minimum = getMin();
    NumberType<N> type = getType();
    if (type == null) {
      return null;
    }
    if (minimum == null) {
      minimum = type.getZero();
    }
    if (factor <= 0) {
      return minimum;
    }
    N maximum = getMax();
    if (maximum == null) {
      maximum = type.getMax();
      if (maximum == null) {
        maximum = type.valueOf(NumberType.INTEGER.getMax());
      }
    }
    if (factor >= 1) {
      return maximum;
    }
    N delta = type.subtract(maximum, minimum);
    N scaled = type.multiply(delta, Double.valueOf(factor));
    return type.add(scaled, minimum);
  }

  /**
   * @param value the value within this range.
   * @return the scaled value. It will be {@link #getMin() min} in case the given {@code factor} is {@code 0}, while
   *         {@code 0} is used in case {@link #getMin() min} is {@code null}. In case the {@code factor} is {@code 1},
   *         it will return {@link #getMax() max} or the maximum value instead of {@code null}. Any other factor is
   *         interpolated between {@link #getMin() min} and {@link #getMax() max}.
   */
  default double toFactor(N value) {

    NumberType<N> type = getType();
    if (type == null) {
      return Double.NaN;
    }
    N minimum = getMin();
    if (minimum == null) {
      minimum = type.getZero();
    }
    N maximum = getMax();
    if (maximum == null) {
      maximum = type.getMax();
      if (maximum == null) {
        maximum = type.valueOf(NumberType.INTEGER.getMax());
      }
    }
    N delta = type.subtract(maximum, minimum);
    N offset = type.subtract(value, minimum);
    if (type.isDecimal()) {
      return type.divide(offset, delta).doubleValue();
    } else if (type.getExactness() == NumberType.BIG_INTEGER.getExactness()) {
      return new BigDecimal((BigInteger) offset).divide(new BigDecimal((BigInteger) delta)).doubleValue();
    } else {
      return offset.doubleValue() / delta.doubleValue();
    }
  }

}
