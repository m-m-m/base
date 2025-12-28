package io.github.mmm.base.variable;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link VariableDefinition}.
 */
class VariableDefinitionTest extends Assertions {

  /** Test of {@link VariableDefinition} with {@link VariableDefinition#getDefaultValue()}. */
  @Test
  void testWithDefault() {

    VariableDefinition<String> var = new VariableDefinition<>("option-name", "default-value");
    assertThat(var.getName()).isEqualTo("option-name");
    assertThat(var.getDefaultValue()).isEqualTo("default-value");
    assertThat(var.getType()).isEqualTo(String.class);
  }

  /** Test of {@link VariableDefinition} without {@link VariableDefinition#getDefaultValue()}. */
  @Test
  void testEmpty() {

    VariableDefinition<String> var = new VariableDefinition<>("opt-name", String.class);
    assertThat(var.getName()).isEqualTo("opt-name");
    assertThat(var.getDefaultValue()).isNull();
    assertThat(var.getType()).isEqualTo(String.class);
  }

  /** Test of {@link VariableDefinition} without {@link VariableDefinition#getType()} (Anti-pattern). */
  @Test
  void testUntyped() {

    VariableDefinition<String> var = new VariableDefinition<>("bad-option", (String) null);
    assertThat(var.getName()).isEqualTo("bad-option");
    assertThat(var.getDefaultValue()).isNull();
    assertThat(var.getType()).isNull();
  }

}
