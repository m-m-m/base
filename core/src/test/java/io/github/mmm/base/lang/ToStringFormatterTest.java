package io.github.mmm.base.lang;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link ToStringFormatter}.
 */
public class ToStringFormatterTest extends Assertions {

  /** Tests {@link ToStringFormatter#apply(Object)}. */
  @Test
  public void test() {

    ToStringFormatter<Object> formatter = ToStringFormatter.get();
    assertThat(formatter.apply("hello world")).isEqualTo("hello world");
    assertThat(formatter.apply(Integer.valueOf(42))).isEqualTo("42");
    assertThat(formatter.apply(Double.valueOf(42.42))).isEqualTo("42.42");
    assertThat(formatter.apply(null)).isEqualTo("null");
  }

}
