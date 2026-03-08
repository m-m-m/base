/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.container;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link ContainerMap}.
 */
class ContainerMapTest extends Assertions {

  private static final Class<?>[] ELEMENTS = { Container.class, ContainerMap.class, ContainerMapTest.class };

  @Test
  void testEmpty() {

    // arrange
    // act
    ContainerMap<?> container = ContainerMap.getEmpty();
    // assert
    assertThat(container).isEmpty();
    assertThat(container.getFirst()).isNull();
    assertThat(container).hasToString("");
  }

  @Test
  void testSingle() {

    // arrange
    Class<?> element = ContainerMapTest.class;
    // act
    ContainerMap<Class<?>> container = ContainerMap.of(Class::getName, element);
    // assert
    assertThat(container).containsExactly(element);
    assertThat(container.getFirst()).isSameAs(element);
    assertThat(container.get(element.getName())).isSameAs(element);
    assertThat(container).hasToString(element.getName());
  }

  @Test
  void testMultiple() {

    // arrange
    Function<Class<?>, String> nameFunction = Class::getName;
    // act
    ContainerMap<Class<?>> container = ContainerMap.of(nameFunction, ELEMENTS);
    // assert
    check3ClassMap(container);
  }

  @Test
  void testMultipleMap() {

    // arrange
    Map<String, Class<?>> map = new TreeMap<>();
    for (Class<?> type : ELEMENTS) {
      map.put(type.getName(), type);
    }
    Function<Class<?>, String> nameFunction = Class::getName;
    // act
    ContainerMap<Class<?>> container = ContainerMap.of(nameFunction, map);
    // assert
    check3ClassMap(container);
  }

  @Test
  void testBuilderList() {

    // arrange
    Function<Class<?>, String> nameFunction = Class::getName;
    // act
    ContainerMapBuilder<Class<?>> builder = ContainerMap.builder(nameFunction, false);
    for (Class<?> element : ELEMENTS) {
      builder.add(element);
    }
    ContainerMap<Class<?>> container = builder.build();
    // assert
    check3ClassMap(container);
  }

  @Test
  void testBuilderMap() {

    // arrange
    Function<Class<?>, String> nameFunction = Class::getName;
    // act
    ContainerMapBuilder<Class<?>> builder = ContainerMap.builder(nameFunction, true);
    for (Class<?> element : ELEMENTS) {
      builder.add(element);
    }
    ContainerMap<Class<?>> container = builder.build();
    // assert
    check3ClassMap(container);
  }

  private void check3ClassMap(ContainerMap<Class<?>> container) {

    assertThat(container).containsExactlyInAnyOrder(ELEMENTS);
    assertThat(container.getFirst()).isSameAs(ELEMENTS[0]);
    for (int i = 0; i < ELEMENTS.length; i++) {
      assertThat(container.get(ELEMENTS[i].getName())).isSameAs(ELEMENTS[i]);
    }
    assertThat(container).hasToString(
        "io.github.mmm.base.container.Container,io.github.mmm.base.container.ContainerMap,io.github.mmm.base.container.ContainerMapTest");
  }

}
