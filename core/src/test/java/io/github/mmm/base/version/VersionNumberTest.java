package io.github.mmm.base.version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test of {@link VersionNumber}.
 */
class VersionNumberTest extends Assertions {

  /**
   * Test of {@link VersionIdentifier#of(String)}.
   */
  @Test
  void testOf() {

    // test exotic version number
    // given
    String version = "1.0-release-candidate2_-.HF1";
    // when
    VersionNumber vid = VersionNumber.of(version);
    // then
    VersionSegment segment1 = vid.getStart();
    assertThat(segment1.getSeparator()).isEmpty();
    assertThat(segment1.getLettersString()).isEmpty();
    assertThat(segment1.getPhase()).isSameAs(VersionPhase.NONE);
    assertThat(segment1.getNumber()).isEqualTo(1);
    assertThat(segment1).hasToString("1");
    VersionSegment segment2 = segment1.getNext();
    assertThat(segment2.getSeparator()).isEqualTo(".");
    assertThat(segment2.getLettersString()).isEmpty();
    assertThat(segment2.getPhase()).isSameAs(VersionPhase.NONE);
    assertThat(segment2.getNumber()).isEqualTo(0);
    assertThat(segment2).hasToString(".0");
    VersionSegment segment3 = segment2.getNext();
    assertThat(segment3.getSeparator()).isEqualTo("-");
    assertThat(segment3.getLettersString()).isEqualTo("release-candidate");
    assertThat(segment3.getPhase()).isSameAs(VersionPhase.RELEASE_CANDIDATE);
    assertThat(segment3.getNumber()).isEqualTo(2);
    assertThat(segment3).hasToString("-release-candidate2");
    VersionSegment segment4 = segment3.getNext();
    assertThat(segment4.getSeparator()).isEqualTo("_-.");
    assertThat(segment4.getLettersString()).isEqualTo("HF");
    assertThat(segment4.getPhase()).isSameAs(VersionPhase.HOT_FIX);
    assertThat(segment4.getNumber()).isEqualTo(1);
    assertThat(segment4).hasToString("_-.HF1");

    assertThat(vid.getDevelopmentPhase()).isSameAs(VersionLetters.UNDEFINED);
  }

  /**
   * Test of {@link VersionNumber#isValid() valid} versions.
   */
  @ParameterizedTest
  // arrange
  @ValueSource(strings = { "1.0", "0.1", "2023.08.001", "2023-06-M1", "11.0.4_11.4", "5.2.23.RELEASE" })
  void testValid(String version) {

    // act
    VersionNumber vid = VersionNumber.of(version);

    // assert
    assertThat(vid.isValid()).as(version).isTrue();
    assertThat(vid).hasToString(version);
  }

  /**
   * Test of in{@link VersionNumber#isValid() valid} versions.
   */
  @ParameterizedTest
  // arrange
  @ValueSource(strings = { "0", "0.0", "1.0.pineapple-pen", "1.0-rc", ".1.0", "1.-0", "RC1", "Beta1", "donut",
  "8u412b08" })
  void testInvalid(String version) {

    // act
    VersionNumber vid = VersionNumber.of(version);

    // assert
    assertThat(vid.isValid()).as(version).isFalse();
    assertThat(vid).hasToString(version);
  }

  /**
   * Test of {@link VersionNumber} with canonical version numbers and safe order.
   */
  @Test
  void testCompare() {

    String[] versions = { "0.1", "0.2-SNAPSHOT", "0.2-nb5", "0.2-a", "0.2-alpha1", "0.2-beta", "0.2-b2", "0.2.M1",
    "0.2M9", "0.2M10", "0.2-rc1", "0.2-RC2", "0.2", "0.2-fix9", "0.2-hf1", "0.3", "0.3.1", "1", "1.0", "10-alpha1" };
    List<VersionNumber> vids = new ArrayList<>(versions.length);
    for (String version : versions) {
      VersionNumber vid = VersionNumber.of(version);
      vids.add(vid);
      assertThat(vid).hasToString(version);
    }
    Collections.shuffle(vids);
    Collections.sort(vids);
    for (int i = 0; i < versions.length; i++) {
      String version = versions[i];
      VersionNumber vid = vids.get(i);
      assertThat(vid).hasToString(version);
    }
  }

  /**
   * Test of {@link VersionIdentifier#compareVersion(VersionIdentifier)} with {@link VersionComparisonResult#isUnsafe()
   * unsafe results} and other edge-cases.
   */
  @Test
  void testCompareSpecial() {

    assertThat(VersionNumber.of("2").compareVersion(VersionNumber.of("2.0"))).isSameAs(VersionComparisonResult.LESS);
    assertThat(VersionNumber.of("2.0").compareVersion(VersionNumber.of("2"))).isSameAs(VersionComparisonResult.GREATER);
  }

  @Test
  void testCompareJavaVersions() {

    VersionNumber v21_35 = VersionNumber.of("21_35");
    VersionNumber v21_0_2_13 = VersionNumber.of("21.0.2_13");
    VersionNumber v21_0_3_9 = VersionNumber.of("21.0.3_9");
    assertThat(v21_35).isLessThan(v21_0_2_13);
    assertThat(v21_0_2_13).isLessThan(v21_0_3_9);
    assertThat(v21_0_3_9).isGreaterThan(v21_35);
  }

  /** Test of {@link VersionIdentifier#incrementSegment(int, boolean)} and related methods. */
  @Test
  void testIncrement() {

    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 0, false, "2.0.0-0.0");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 0, true, "2.0beta.0-0foo.0bar-SNAPSHOT");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 1, false, "1.3.0-0.0");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 1, true, "1.3beta.0-0foo.0bar-SNAPSHOT");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 2, false, "1.2beta.4-0.0");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 2, true, "1.2beta.4-0foo.0bar-SNAPSHOT");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 3, false, "1.2beta.3-5.0");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 3, true, "1.2beta.3-5foo.0bar-SNAPSHOT");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 4, false, "1.2beta.3-4foo.6");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 4, true, "1.2beta.3-4foo.6bar-SNAPSHOT");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 5, false, "1.2beta.3-4foo.5bar-SNAPSHOT");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 5, true, "1.2beta.3-4foo.5bar-SNAPSHOT");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 6, false, "1.2beta.3-4foo.5bar-SNAPSHOT");
    assertIncrement("1.2beta.3-4foo.5bar-SNAPSHOT", 6, true, "1.2beta.3-4foo.5bar-SNAPSHOT");
    // devonfw/IDEasy (we are not planning to implement semantic detection that in this case we should start new
    // segments with 1 instead of 0)
    VersionNumber versionIdentifier = VersionNumber.of("2025.01.002");
    assertThat(versionIdentifier.incrementMajor(false)).hasToString("2026.00.000");
    assertThat(versionIdentifier.incrementMinor(false)).hasToString("2025.02.000");
    assertThat(versionIdentifier.incrementPatch(false)).hasToString("2025.01.003");
  }

  private static void assertIncrement(String version, int digit, boolean keepLetters, String expectedVersion) {

    VersionNumber identifier = VersionNumber.of(version);
    VersionNumber incremented = switch (digit) {
      case 0 -> identifier.incrementMajor(keepLetters);
      case 1 -> identifier.incrementMinor(keepLetters);
      case 2 -> identifier.incrementPatch(keepLetters);
      default -> identifier.incrementSegment(digit, keepLetters);
    };
    assertThat(incremented).hasToString(expectedVersion);
  }

  /** Test of {@link VersionIdentifier#incrementLastDigit(boolean)}. */
  @Test
  void testIncrementLastDigit() {

    assertIncrementLastDigit("1-beta", false, "2");
    assertIncrementLastDigit("1-beta", true, "2-beta");
    assertIncrementLastDigit("1.0-beta", false, "1.1");
    assertIncrementLastDigit("1.0-alpha", true, "1.1-alpha");
    assertIncrementLastDigit("3.2.1_rc-SNAPSHOT", false, "3.2.2");
    assertIncrementLastDigit("3.2.1_rc-SNAPSHOT", true, "3.2.2_rc-SNAPSHOT");
  }

  private static void assertIncrementLastDigit(String version, boolean keepLetters, String expectedVersion) {

    VersionNumber identifier = VersionNumber.of(version);
    VersionNumber incremented = identifier.incrementLastDigit(keepLetters);
    assertThat(incremented).hasToString(expectedVersion);
  }

  /** Test of {@link VersionIdentifier#isStable()}. */
  @Test
  void testIsStable() {

    assertThat(VersionNumber.of("2025.01.002").isStable()).isTrue();
    assertThat(VersionNumber.of("1.0-rc1").isStable()).isFalse();
    assertThat(VersionNumber.of("1.0-alpha1.rc2").isStable()).isFalse();
  }

}
