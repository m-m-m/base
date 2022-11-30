package io.github.mmm.base.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link ConfigMap}.
 */
public class ConfigMapTest extends Assertions {

  static final ConfigOption<String> OPT_INDENTATION = new ConfigOption<>("indentation", "  ");

  static final ConfigOption<Boolean> OPT_WRITE_NULL_VALUES = new ConfigOption<>("write-null", Boolean.TRUE);

  static final ConfigOption<Integer> OPT_PORT = new ConfigOption<>("port", Integer.class);

  /** Test of {@link ConfigMap#get(ConfigOption)}. */
  @Test
  public void test() {

    ConfigMap map = new ConfigMap();
    assertThat(map.get(OPT_INDENTATION)).isEqualTo("  ");
    map.set(OPT_INDENTATION, "    ");
    assertThat(map.get(OPT_INDENTATION)).isEqualTo("    ");
    assertThat(map.get(OPT_WRITE_NULL_VALUES)).isTrue();
    map.set(OPT_WRITE_NULL_VALUES, Boolean.FALSE);
    assertThat(map.get(OPT_WRITE_NULL_VALUES)).isFalse();
    assertThat(map.get(OPT_PORT)).isNull();
  }

}
