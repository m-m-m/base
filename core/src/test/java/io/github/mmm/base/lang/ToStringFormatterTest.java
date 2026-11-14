package io.github.mmm.base.lang;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/// Test of [ToStringFormatter].
class ToStringFormatterTest extends Assertions {

  /// Tests [ToStringFormatter#apply(Object)].
  @Test
  void test() {

    ToStringFormatter<Object> formatter = ToStringFormatter.get();
    assertThat(formatter.apply("hello world")).isEqualTo("hello world");
    assertThat(formatter.apply(Integer.valueOf(42))).isEqualTo("42");
    assertThat(formatter.apply(Double.valueOf(42.42))).isEqualTo("42.42");
    assertThat(formatter.apply(null)).isEqualTo("null");
  }

}
