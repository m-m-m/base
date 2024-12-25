package io.github.mmm.base.exception;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link ObjectNotFoundException}.
 */
public class ObjectNotFoundExceptionTest extends Assertions {

  /** Test {@link ObjectNotFoundException#ObjectNotFoundException(Object)}. */
  @Test
  public void testSimple() {

    // arrange
    String name = "io.github.mmm.UndefinedObject";
    // act
    ObjectNotFoundException exception = new ObjectNotFoundException(name);
    // assert
    assertThat(exception.getLocalizedMessage()).isEqualTo("Could not find " + name);
  }

  /** Test {@link ObjectNotFoundException#ObjectNotFoundException(Object, Object)}. */
  @Test
  public void testWithKey() {

    // arrange
    String name = "Entity";
    String key = "MagicKey";
    // act
    ObjectNotFoundException exception = new ObjectNotFoundException(name, key);
    // assert
    assertThat(exception.getLocalizedMessage()).isEqualTo("Could not find " + name + " for key '" + key + "'");
  }

  /** Test {@link ObjectNotFoundException#ObjectNotFoundException(Object, Object, Object, Throwable)}. */
  @Test
  public void testWithKeyAndOptions() {

    // arrange
    String name = "Entity";
    String key = "MagicKey";
    Collection<String> options = List.of("NormalKey", "Key", "HolyKey");
    // act
    ObjectNotFoundException exception = new ObjectNotFoundException(name, key, options, null);
    // assert
    assertThat(exception.getLocalizedMessage())
        .isEqualTo("Could not find " + name + " for key '" + key + "' in [NormalKey, Key, HolyKey]");
  }

}
