package io.github.mmm.base.range;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.github.mmm.base.number.NumberType;

/// Extends [Range] for [Number] types.
///
/// @param <N> type of the contained [Number] values.
public interface NumberRange<N extends Number & Comparable<?>> extends Range<N> {

  /// @return the [NumberType] of this range. May be `null` if unbounded.
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

  /// This method [wraps][NumberType#wrap(Number, Number, Number)] the given `value` so the result is
  /// [contained][#contains(Comparable)] in this [Range] unless the given `value` is `null`.
  ///
  /// @param value is the vale to clip. May be `null`.
  /// @return the given `value` [wrapped][NumberType#wrap(Number, Number, Number)] to this range.
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

  /// @param value is the vale to clip. May be `null`.
  /// @param wrap - `true` for [#wrap(Number)], `false` for [#clip(Comparable)].
  /// @return [clip][#clip(Comparable)] if `wrap` was `false` and [wrap][#wrap(Number)] otherwise
  ///         (if `wrap` was `true`).
  default N clip(N value, boolean wrap) {

    if (wrap) {
      return wrap(value);
    }
    return clip(value);
  }

  /// @param factor the scaled value in the range from `0` to `1`.
  /// @return the value within this range scaled by the given `factor`. It will be [min][#getMin()] in case
  ///         the given `factor` is `0`, while `0` is used in case [min][#getMin()] is
  ///         `null`. In case the `factor` is `1`, it will return [max][#getMax()] or the maximum
  ///         value instead of `null`. Any other factor is interpolated between [min][#getMin()] and
  ///         [max][#getMax()].
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

  /// @param value the value within this range.
  /// @return the scaled value. It will be [min][#getMin()] in case the given `factor` is `0`, while
  ///         `0` is used in case [min][#getMin()] is `null`. In case the `factor` is `1`,
  ///         it will return [max][#getMax()] or the maximum value instead of `null`. Any other factor is
  ///         interpolated between [min][#getMin()] and [max][#getMax()].
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
