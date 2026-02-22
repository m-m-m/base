package io.github.mmm.base.version;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link VersionSegment}.
 */
class VersionSegmentTest extends Assertions {

  /**
   * Test of constructor and getters with "01".
   */
  @Test
  void testOne() {

    VersionSegment segment = new VersionSegment("", "", "01");
    assertThat(segment.getSeparator()).isEmpty();
    assertThat(segment.getLettersString()).isEmpty();
    assertThat(segment.getPhase()).isSameAs(VersionPhase.NONE);
    assertThat(segment.getDigits()).isEqualTo("01");
    assertThat(segment.getNumber()).isEqualTo(1);
    assertThat(segment.getNext()).isNull();
  }

  /**
   * Test of constructor and getters with ".rc1".
   */
  @Test
  void testDotRc2() {

    VersionSegment segment = new VersionSegment(".", "rc", "1");
    assertThat(segment.getSeparator()).isEqualTo(".");
    assertThat(segment.getLettersString()).isEqualTo("rc");
    assertThat(segment.getPhase()).isSameAs(VersionPhase.RELEASE_CANDIDATE);
    assertThat(segment.getDigits()).isEqualTo("1");
    assertThat(segment.getNumber()).isEqualTo(1);
    assertThat(segment.getNext()).isNull();
  }

}
