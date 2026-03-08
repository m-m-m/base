/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link Container}.
 */
class ContainerTest extends Assertions {

  @Test
  void testEmpty() {

    // arrange
    // act
    Container<?> container = Container.getEmpty();
    // assert
    assertThat(container).isEmpty();
    assertThat(container.getFirst()).isNull();
    assertThat(container).hasToString("");
  }

  @Test
  void testSingle() {

    // arrange
    String element = "Foo";
    // act
    Container<String> container = Container.of(element);
    // assert
    assertThat(container).containsExactly(element);
    assertThat(container.getFirst()).isSameAs(element);
    assertThat(container).hasToString(element);
  }

  @Test
  void testMultiple() {

    // arrange
    String[] elements = { "Foo", "Bar", "Some" };
    // act
    Container<String> container = Container.of(elements);
    // assert
    assertThat(container).containsExactly(elements);
    assertThat(container.getFirst()).isSameAs(elements[0]);
    assertThat(container).hasToString("Foo,Bar,Some");
  }

  @Test
  void testBuilder() {

    // arrange
    Integer[] elements = { 1, 2, 3 };
    // act
    ContainerBuilder<Integer> builder = Container.builder();
    for (Integer element : elements) {
      builder.add(element);
    }
    Container<Integer> container = builder.build();
    // assert
    assertThat(container).containsExactly(elements);
    assertThat(container.getFirst()).isSameAs(elements[0]);
    assertThat(container).hasToString("1,2,3");
  }

}
