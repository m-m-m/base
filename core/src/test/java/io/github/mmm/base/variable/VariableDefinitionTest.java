package io.github.mmm.base.variable;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link VariableDefinition}.
 */
class VariableDefinitionTest extends Assertions {

  @Test
  void testOfStringWithDefault() {

    String name = "option-name";
    String defaultValue = "default-value";
    VariableDefinition<String> var = VariableDefinition.ofString(name, defaultValue);
    assertThat(var.getName()).isEqualTo(name);
    assertThat(var.getDefaultValue()).isEqualTo(defaultValue);
    assertThat(var.getType()).isEqualTo(String.class);
    assertThat(var.parse(null)).isNull();
    assertThat(var.parse(name)).isSameAs(name);
    assertThat(var.format(null)).isNull();
    assertThat(var.format(defaultValue)).isSameAs(defaultValue);
  }

  @Test
  void testOfStringWithoutDefault() {

    VariableDefinition<String> var = VariableDefinition.ofString("opt-name", null);
    assertThat(var.getName()).isEqualTo("opt-name");
    assertThat(var.getDefaultValue()).isNull();
    assertThat(var.getType()).isEqualTo(String.class);
  }

  @Test
  void testOfLong() {

    String name = "long-variable";
    Long defaultValue = 4711L;
    VariableDefinition<Long> var = VariableDefinition.ofLong(name, defaultValue);
    assertThat(var.getName()).isEqualTo(name);
    assertThat(var.getDefaultValue()).isEqualTo(defaultValue);
    assertThat(var.getType()).isEqualTo(Long.class);
    assertThat(var.parse("4711")).isEqualTo(defaultValue);
    assertThat(var.format(defaultValue)).isEqualTo("4711");
  }

}
