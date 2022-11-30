package io.github.mmm.base.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link ConfigOption}.
 */
public class ConfigOptionTest extends Assertions {

  /** Test of {@link ConfigOption} with {@link ConfigOption#getDefaultValue()}. */
  @Test
  public void testWithDefault() {

    ConfigOption<String> opt = new ConfigOption<>("option-name", "default-value");
    assertThat(opt.getKey()).isEqualTo("option-name");
    assertThat(opt.getDefaultValue()).isEqualTo("default-value");
    assertThat(opt.getType()).isEqualTo(String.class);
  }

  /** Test of {@link ConfigOption} without {@link ConfigOption#getDefaultValue()}. */
  @Test
  public void testEmpty() {

    ConfigOption<String> opt = new ConfigOption<>("opt-name", String.class);
    assertThat(opt.getKey()).isEqualTo("opt-name");
    assertThat(opt.getDefaultValue()).isNull();
    assertThat(opt.getType()).isEqualTo(String.class);
  }

  /** Test of {@link ConfigOption} without {@link ConfigOption#getType()} (Anti-pattern). */
  @Test
  public void testUntyped() {

    ConfigOption<String> opt = new ConfigOption<>("bad-option", (String) null);
    assertThat(opt.getKey()).isEqualTo("bad-option");
    assertThat(opt.getDefaultValue()).isNull();
    assertThat(opt.getType()).isNull();
  }

}
