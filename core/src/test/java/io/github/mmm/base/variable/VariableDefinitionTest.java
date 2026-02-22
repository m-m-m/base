package io.github.mmm.base.variable;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link VariableDefinition}.
 */
class VariableDefinitionTest extends Assertions {

  @Test
  void testOfStringWithDefault() {

    String name = "variable-name";
    String value = "value123";
    String defaultValue = "default-value";
    VariableDefinition<String> var = VariableDefinition.ofString(name, defaultValue);
    assertThat(var.getName()).isEqualTo(name);
    assertThat(var.getDefaultValue()).isEqualTo(defaultValue);
    assertThat(var.getType()).isEqualTo(String.class);
    assertThat(var.parse(null)).isNull();
    assertThat(var.parse(name)).isSameAs(name);
    assertThat(var.format(null)).isNull();
    assertThat(var.format(defaultValue)).isSameAs(defaultValue);
    assertThat(var.get(Map.of(name, value))).isSameAs(value);
    assertThat(var.get(Map.of())).isSameAs(defaultValue);
  }

  @Test
  void testOfStringWithoutDefault() {

    String name = "variable-name";
    String value = "value123";
    VariableDefinition<String> var = VariableDefinition.ofString(name, null);
    assertThat(var.getName()).isEqualTo(name);
    assertThat(var.getDefaultValue()).isNull();
    assertThat(var.getType()).isEqualTo(String.class);
    assertThat(var.get(Map.of(name, value))).isSameAs(value);
    assertThat(var.get(Map.of(), value)).isSameAs(value);
    assertThat(var.get(Map.of())).isNull();
    ;
  }

  @Test
  void testOfLong() {

    String name = "long-variable";
    Long value = 1234L;
    Long defaultValue = 4711L;
    VariableDefinition<Long> var = VariableDefinition.ofLong(name, defaultValue);
    assertThat(var.getName()).isEqualTo(name);
    assertThat(var.getDefaultValue()).isEqualTo(defaultValue);
    assertThat(var.getType()).isEqualTo(Long.class);
    assertThat(var.parse("4711")).isEqualTo(defaultValue);
    assertThat(var.format(defaultValue)).isEqualTo("4711");
    assertThat(var.get(Map.of(name, value))).isSameAs(value);
    assertThat(var.get(Map.of(), value)).isSameAs(value);
    assertThat(var.get(Map.of())).isSameAs(defaultValue);
  }

}
