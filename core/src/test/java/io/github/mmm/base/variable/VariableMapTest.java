package io.github.mmm.base.variable;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link VariableMap}.
 */
public class VariableMapTest extends Assertions {

  static final VariableDefinition<String> OPT_INDENTATION = new VariableDefinition<>("indentation", "  ");

  static final VariableDefinition<Boolean> OPT_WRITE_NULL_VALUES = new VariableDefinition<>("write-null", Boolean.TRUE);

  static final VariableDefinition<Integer> OPT_PORT = new VariableDefinition<>("port", Integer.class);

  /** Test of {@link VariableMap#get(VariableDefinition)}. */
  @Test
  public void test() {

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
