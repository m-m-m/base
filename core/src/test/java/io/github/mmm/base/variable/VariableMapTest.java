package io.github.mmm.base.variable;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link VariableMap}.
 */
class VariableMapTest extends Assertions {

  static final VariableDefinition<String> OPT_INDENTATION = VariableDefinition.ofString("indentation", "  ");

  static final VariableDefinition<Boolean> OPT_WRITE_NULL_VALUES = VariableDefinition.ofBoolean("write-null",
      Boolean.TRUE);

  static final VariableDefinition<Integer> OPT_PORT = VariableDefinition.ofInteger("port", null);

  /** Test of {@link VariableMap#get(VariableDefinition)}. */
  @Test
  void test() {

    VariableMap map = new VariableMap();
    assertThat(map.get(OPT_INDENTATION)).isEqualTo("  ");
    map.set(OPT_INDENTATION, "    ");
    assertThat(map.get(OPT_INDENTATION)).isEqualTo("    ");
    assertThat(map.get(OPT_WRITE_NULL_VALUES)).isTrue();
    map.set(OPT_WRITE_NULL_VALUES, Boolean.FALSE);
    assertThat(map.get(OPT_WRITE_NULL_VALUES)).isFalse();
    assertThat(map.get(OPT_PORT)).isNull();
  }

}
