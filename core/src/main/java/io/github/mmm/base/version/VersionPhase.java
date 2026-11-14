/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.version;

/// Enum with the available development phases of a [letter-sequence][VersionSegment#getLettersString()] from a
/// [VersionSegment].
public enum VersionPhase implements AbstractVersionPhase {

  /// An undefined [VersionPhase] such as "apple" or "banana".
  UNDEFINED,

  /// A revision number (actually not a development phase like [#UNDEFINED]).
  REVISION(Boolean.TRUE, "revision", "rev"),

  /// A snapshot version from development (e.g. "-SNAPSHOT" suffix in maven).
  SNAPSHOT(Boolean.FALSE, "beta-snapshot", "snapshot", "dev"),

  /// A nightly build version from continuous-integration (CI) process.
  NIGHTLY("nightly", "nb", "ci"),

  /// A version for alpha testing (unstable).
  ALPHA(Boolean.TRUE, "alpha", "alph", "alp", "a", "unstable"),

  /// A version for beta testing (should not have fundamental bugs like maybe in [#ALPHA] but still needs more
  /// testing).
  BETA(Boolean.TRUE, "beta", "bet", "test"),

  /// A "b" can be [#BETA] or [#BUILD] depending on the version scheme of the product. Please avoid such
  /// ambiguous [letter-sequence][VersionSegment#getLetters()].
  BETA_OR_BUILD(Boolean.TRUE, "b"),

  /// A build number (actually not a development phase like [#UNDEFINED]).
  BUILD(Boolean.TRUE, "build"),

  /// A version that has passed quality gates but is in a phase for further end-user testing to collect feedback.
  MILESTONE(Boolean.TRUE, "m", "milestone"),

  /// A version close to the [#RELEASE] with expected good quality for final end-user testing and only relevant
  /// bugs will be fixed if found to come to the final release (typically within a predefined time-frame).
  RELEASE_CANDIDATE(Boolean.TRUE, "rc", "release-candidate", "candidate"),

  /// No phase specified.
  NONE(Boolean.TRUE, ""),

  /// An official release that should have good quality.
  RELEASE("release", "ga", "rel"),

  /// A bug-fix version from the previous release including important fix(es).
  BUG_FIX(Boolean.TRUE, "bugfix", "fix", "quickfix"),

  /// An fix release, similar to [#BUG_FIX] but more urgent.
  HOT_FIX(Boolean.TRUE, "hotfix", "hf");

  private final Boolean hasNumber;

  private final String[] ids;

  private VersionPhase(String... ids) {

    this.hasNumber = null;
    this.ids = ids;
  }

  private VersionPhase(Boolean hasNumber, String... ids) {

    this.hasNumber = hasNumber;
    this.ids = ids;
  }

  @Override
  public boolean isDevelopmentPhase() {

    return (this != UNDEFINED) && (this != NONE) && (this != REVISION) && (this != BUILD);
  }

  /// A valid combination of [VersionPhase] and [segment number][VersionSegment#getNumber()] has to meet the
  /// following requirements:
  ///
  /// - [VersionPhase] is not [#UNDEFINED].
  /// - [VersionPhase] is not [#BETA_OR_BUILD] ("b" is ambiguous and therefore discouraged).
  /// - The [VersionPhase]s [#REVISION], [#ALPHA], [#BETA], [#BUILD], [#MILESTONE],
  /// [#RELEASE_CANDIDATE], [#NONE], [#BUG_FIX], or [#HOT_FIX] have to be followed by
  /// [digits][VersionSegment#getDigits()] ([segment number][VersionSegment#getNumber()] >= 0).
  /// - The [VersionPhase] [#SNAPSHOT] must not be followed by [digits][VersionSegment#getDigits()]
  /// ([segment number][VersionSegment#getNumber()] == -1).
  ///
  /// @param number the [segment number][VersionSegment#getNumber()].
  /// @return `true` if the combination of this [VersionPhase] with the given `number` is a valid
  ///         according to version scheme best-practices.
  public boolean isValid(int number) {

    if ((this == UNDEFINED) || (this == BETA_OR_BUILD)) {
      return false;
    }
    if (this.hasNumber != null) {
      if (this.hasNumber.booleanValue()) {
        return (number >= 0);
      } else {
        return (number == -1);
      }
    }
    return true;
  }

  @Override
  public boolean isUnstable() {

    if (isDevelopmentPhase()) {
      if (ordinal() < NONE.ordinal()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isStable() {

    if (ordinal() >= NONE.ordinal()) {
      return true;
    }
    return false;

  }

  /// @param letters the [letter-sequence][VersionSegment#getLettersString()] of a [VersionSegment] in
  ///        [lower-case][String#toLowerCase(java.util.Locale)].
  /// @return the corresponding [VersionPhase]. Will be `#UNDEFINED` if undefined (e.g. "apple" or "banana").
  public static VersionPhase of(String letters) {

    for (VersionPhase phase : values()) {
      for (String id : phase.ids) {
        if (id.equals(letters)) {
          return phase;
        }
      }
    }
    return UNDEFINED;
  }

}
