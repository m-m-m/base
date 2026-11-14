/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.version;

import java.util.Objects;

/// Data-type to represent a [VersionNumber] in a structured way and allowing [comparison][#compareVersion(VersionNumber)] of [VersionNumber]s.
public final class VersionNumber implements VersionObject<VersionNumber> {

  private final VersionSegment start;

  private final VersionLetters developmentPhase;

  private final boolean valid;

  private VersionNumber(VersionSegment start) {

    super();
    Objects.requireNonNull(start);
    this.start = start;
    boolean isValid = this.start.getSeparator().isEmpty() && this.start.getLettersString().isEmpty();
    boolean hasPositiveNumber = false;
    VersionLetters dev = VersionLetters.EMPTY;
    VersionSegment segment = this.start;
    while (segment != null) {
      if (!segment.isValid()) {
        isValid = false;
      } else if (segment.getNumber() > 0) {
        hasPositiveNumber = true;
      }
      VersionLetters segmentLetters = segment.getLetters();
      if (segmentLetters.isDevelopmentPhase()) {
        if (dev.isEmpty()) {
          dev = segmentLetters;
        } else {
          dev = VersionLetters.UNDEFINED;
          isValid = false;
        }
      }
      segment = segment.getNext();
    }
    this.developmentPhase = dev;
    this.valid = isValid && hasPositiveNumber;
  }

  /// @return the first [VersionSegment] of this [VersionNumber]. To get other segments use
  ///         [VersionSegment#getNext()].
  public VersionSegment getStart() {

    return this.start;
  }

  /// A valid [VersionNumber] has to meet the following requirements:
  ///
  /// - All [segments][VersionSegment] themselves are [valid][VersionSegment#isValid()].
  /// - The [start][#getStart()] [segment][VersionSegment] shall have an [empty][String#isEmpty()]
  /// [separator][VersionSegment#getSeparator()] (e.g. ".1.0" or "-1-2" are not considered valid).
  /// - The [start][#getStart()] [segment][VersionSegment] shall have an [empty][String#isEmpty()]
  /// [letter-sequence][VersionSegment#getLettersString()] (e.g. "RC1" or "beta" are not considered valid).
  /// - Have at least one [segment][VersionSegment] with a positive [number][VersionSegment#getNumber()]
  /// (e.g. "0.0.0" or "0.alpha" are not considered valid).
  /// - Have at max one [segment][VersionSegment] with a [phase][VersionSegment#getPhase()] that is a real
  /// [development phase][VersionPhase#isDevelopmentPhase()] (e.g. "1.alpha1.beta2" or "1.0.rc1-milestone2" are not
  /// considered valid).
  @Override
  public boolean isValid() {

    return this.valid;
  }

  /// @return `true` if this is a stable version, `false` otherwise.
  /// @see VersionLetters#isStable()
  public boolean isStable() {

    return this.developmentPhase.isStable();
  }

  /// @return the [development phase][VersionLetters#isDevelopmentPhase()] of this [VersionNumber]. Will be
  ///         [VersionLetters#EMPTY] if no development phase is specified in any [VersionSegment] and will be
  ///         [VersionLetters#UNDEFINED] if more than one [development
         phase][VersionLetters#isDevelopmentPhase()] is specified (e.g. "1.0-alpha1.rc2").
  public VersionLetters getDevelopmentPhase() {

    return this.developmentPhase;
  }

  @Override
  public VersionComparisonResult compareVersion(VersionNumber other) {

    if (other == null) {
      return VersionComparisonResult.GREATER_UNSAFE;
    }
    VersionSegment thisSegment = this.start;
    VersionSegment otherSegment = other.start;
    VersionComparisonResult result;
    boolean unsafe = false;
    do {
      result = thisSegment.compareVersion(otherSegment);
      if (result.isEqual()) {
        if (result.isUnsafe()) {
          unsafe = true;
        }
      } else {
        break;
      }
      thisSegment = thisSegment.getNextOrEmpty();
      otherSegment = otherSegment.getNextOrEmpty();
    } while ((thisSegment != VersionSegment.EMPTY) || (otherSegment != VersionSegment.EMPTY));
    if (unsafe) {
      return result.withUnsafe();
    }
    return result;
  }

  /// Increment the specified segment. For examples see `VersionIdentifierTest.testIncrement()`.
  ///
  /// @param digitNumber the index of the [VersionSegment] to increment. All segments before will remain untouched
  ///        and all following segments will be set to zero.
  /// @param keepLetters `true` to keep [letters][VersionSegment#getLetters()] from modified segments,
  ///        `false` to drop them.
  /// @return the incremented [VersionNumber].
  public VersionNumber incrementSegment(int digitNumber, boolean keepLetters) {

    VersionSegment newStart = this.start.increment(digitNumber, keepLetters);
    return new VersionNumber(newStart);
  }

  /// Increment the first digit (major version).
  ///
  /// @param keepLetters `true` to keep [letters][VersionSegment#getLetters()] from modified segments,
  ///        `false` to drop them.
  /// @return the incremented [VersionNumber].
  /// @see #incrementSegment(int, boolean)
  public VersionNumber incrementMajor(boolean keepLetters) {

    return incrementSegment(0, keepLetters);
  }

  /// Increment the second digit (minor version).
  ///
  /// @param keepLetters `true` to keep [letters][VersionSegment#getLetters()] from modified segments,
  ///        `false` to drop them.
  /// @return the incremented [VersionNumber].
  /// @see #incrementSegment(int, boolean)
  public VersionNumber incrementMinor(boolean keepLetters) {

    return incrementSegment(1, keepLetters);
  }

  /// Increment the third digit (patch or micro version).
  ///
  /// @param keepLetters `true` to keep [letters][VersionSegment#getLetters()] from modified segments,
  ///        `false` to drop them.
  /// @return the incremented [VersionNumber].
  /// @see #incrementSegment(int, boolean)
  public VersionNumber incrementPatch(boolean keepLetters) {

    return incrementSegment(2, keepLetters);
  }

  /// Increment the last segment.
  ///
  /// @param keepLetters `true` to keep [letters][VersionSegment#getLetters()] from modified segments,
  ///        `false` to drop them.
  /// @return the incremented [VersionNumber].
  /// @see #incrementSegment(int, boolean)
  public VersionNumber incrementLastDigit(boolean keepLetters) {

    return incrementSegment(this.start.countDigits() - 1, keepLetters);
  }

  @Override
  public int hashCode() {

    VersionSegment segment = this.start;
    int hash = 1;
    while (segment != null) {
      hash = hash * 31 + segment.hashCode();
      segment = segment.getNext();
    }
    return hash;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    } else if (!(obj instanceof VersionNumber)) {
      return false;
    }
    VersionNumber other = (VersionNumber) obj;
    return Objects.equals(this.start, other.start);
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();
    VersionSegment segment = this.start;
    while (segment != null) {
      sb.append(segment.toString());
      segment = segment.getNext();
    }
    return sb.toString();
  }

  /// @param version the [string representation][#toString()] of the [VersionNumber] to parse.
  /// @return the parsed [VersionNumber].
  public static VersionNumber of(String version) {

    if (version == null) {
      return null;
    }
    version = version.trim();
    assert !version.contains(" ") && !version.contains("\n") && !version.contains("\t") : version;
    VersionSegment startSegment = VersionSegment.of(version);
    if (startSegment == null) {
      return null;
    }
    return new VersionNumber(startSegment);
  }

  /// @param v1 the first [VersionNumber].
  /// @param v2 the second [VersionNumber].
  /// @param treatNullAsNegativeInfinity `true` to treat `null` as negative infinity, `false` otherwise
  ///        (positive infinity).
  /// @return the null-safe [comparison][#compareVersion(VersionNumber)] of the two [VersionNumber]s.
  public static VersionComparisonResult compareVersion(VersionNumber v1, VersionNumber v2,
      boolean treatNullAsNegativeInfinity) {

    if (v1 == null) {
      if (v2 == null) {
        return VersionComparisonResult.EQUAL;
      } else if (treatNullAsNegativeInfinity) {
        return VersionComparisonResult.LESS;
      }
      return VersionComparisonResult.GREATER;
    } else if (v2 == null) {
      if (treatNullAsNegativeInfinity) {
        return VersionComparisonResult.GREATER;
      }
      return VersionComparisonResult.LESS;
    }
    return v1.compareVersion(v2);
  }

}
