package io.github.mmm.base.collection;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link TransformingIterator}.
 */
class TransformingOperatorTest extends Assertions {

  @Test
  void testAll() {

    // arrange
    String[] dateTimeStrings = { "1999-12-31T23:59:59", "2000-01-01T00:00:00.100", "2001-12-31T23:59:59" };
    List<LocalDateTime> dateTimeList = new ArrayList<>();
    for (String dateTimeString : dateTimeStrings) {
      dateTimeList.add(LocalDateTime.parse(dateTimeString));
    }

    // act
    Iterator<String> it = new TransformingIterator<>(dateTimeList.iterator(), LocalDateTime::toString);

    // assert
    for (String dateTimeString : dateTimeStrings) {
      assertThat(it.hasNext());
      assertThat(it.next()).isEqualTo(dateTimeString);
    }
    assertThat(dateTimeList).hasSize(dateTimeStrings.length);
    it.remove();
    assertThat(dateTimeList).hasSize(dateTimeStrings.length - 1);
    assertThat(it.hasNext()).isFalse();
    assertThrows(NoSuchElementException.class, it::next);
  }

}
