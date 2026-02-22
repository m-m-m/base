/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.version;

import java.util.Objects;

/**
 * Data-type to represent a {@link VersionNumber} in a structured way and allowing {@link #compareVersion(VersionNumber)
 * comparison} of {@link VersionNumber}s.
 */
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

  /**
   * @return the first {@link VersionSegment} of this {@link VersionNumber}. To get other segments use
   *         {@link VersionSegment#getNext()}.
   */
  public VersionSegment getStart() {

    return this.start;
  }

  /**
   * A valid {@link VersionNumber} has to meet the following requirements:
   * <ul>
   * <li>All {@link VersionSegment segments} themselves are {@link VersionSegment#isValid() valid}.</li>
   * <li>The {@link #getStart() start} {@link VersionSegment segment} shall have an {@link String#isEmpty() empty}
   * {@link VersionSegment#getSeparator() separator} (e.g. ".1.0" or "-1-2" are not considered valid).</li>
   * <li>The {@link #getStart() start} {@link VersionSegment segment} shall have an {@link String#isEmpty() empty}
   * {@link VersionSegment#getLettersString() letter-sequence} (e.g. "RC1" or "beta" are not considered valid).</li>
   * <li>Have at least one {@link VersionSegment segment} with a positive {@link VersionSegment#getNumber() number}
   * (e.g. "0.0.0" or "0.alpha" are not considered valid).</li>
   * <li>Have at max one {@link VersionSegment segment} with a {@link VersionSegment#getPhase() phase} that is a real
   * {@link VersionPhase#isDevelopmentPhase() development phase} (e.g. "1.alpha1.beta2" or "1.0.rc1-milestone2" are not
   * considered valid).</li>
   * </ul>
   */
  @Override
  public boolean isValid() {

    return this.valid;
  }

  /**
   * @return {@code true} if this is a stable version, {@code false} otherwise.
   * @see VersionLetters#isStable()
   */
  public boolean isStable() {

    return this.developmentPhase.isStable();
  }

  /**
   * @return the {@link VersionLetters#isDevelopmentPhase() development phase} of this {@link VersionNumber}. Will be
   *         {@link VersionLetters#EMPTY} if no development phase is specified in any {@link VersionSegment} and will be
   *         {@link VersionLetters#UNDEFINED} if more than one {@link VersionLetters#isDevelopmentPhase() development
   *         phase} is specified (e.g. "1.0-alpha1.rc2").
   */
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

  /**
   * Increment the specified segment. For examples see {@code VersionIdentifierTest.testIncrement()}.
   *
   * @param digitNumber the index of the {@link VersionSegment} to increment. All segments before will remain untouched
   *        and all following segments will be set to zero.
   * @param keepLetters {@code true} to keep {@link VersionSegment#getLetters() letters} from modified segments,
   *        {@code false} to drop them.
   * @return the incremented {@link VersionNumber}.
   */
  public VersionNumber incrementSegment(int digitNumber, boolean keepLetters) {

    VersionSegment newStart = this.start.increment(digitNumber, keepLetters);
    return new VersionNumber(newStart);
  }

  /**
   * Increment the first digit (major version).
   *
   * @param keepLetters {@code true} to keep {@link VersionSegment#getLetters() letters} from modified segments,
   *        {@code false} to drop them.
   * @return the incremented {@link VersionNumber}.
   * @see #incrementSegment(int, boolean)
   */
  public VersionNumber incrementMajor(boolean keepLetters) {

    return incrementSegment(0, keepLetters);
  }

  /**
   * Increment the second digit (minor version).
   *
   * @param keepLetters {@code true} to keep {@link VersionSegment#getLetters() letters} from modified segments,
   *        {@code false} to drop them.
   * @return the incremented {@link VersionNumber}.
   * @see #incrementSegment(int, boolean)
   */
  public VersionNumber incrementMinor(boolean keepLetters) {

    return incrementSegment(1, keepLetters);
  }

  /**
   * Increment the third digit (patch or micro version).
   *
   * @param keepLetters {@code true} to keep {@link VersionSegment#getLetters() letters} from modified segments,
   *        {@code false} to drop them.
   * @return the incremented {@link VersionNumber}.
   * @see #incrementSegment(int, boolean)
   */
  public VersionNumber incrementPatch(boolean keepLetters) {

    return incrementSegment(2, keepLetters);
  }

  /**
   * Increment the last segment.
   *
   * @param keepLetters {@code true} to keep {@link VersionSegment#getLetters() letters} from modified segments,
   *        {@code false} to drop them.
   * @return the incremented {@link VersionNumber}.
   * @see #incrementSegment(int, boolean)
   */
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

  /**
   * @param version the {@link #toString() string representation} of the {@link VersionNumber} to parse.
   * @return the parsed {@link VersionNumber}.
   */
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

  /**
   * @param v1 the first {@link VersionNumber}.
   * @param v2 the second {@link VersionNumber}.
   * @param treatNullAsNegativeInfinity {@code true} to treat {@code null} as negative infinity, {@code false} otherwise
   *        (positive infinity).
   * @return the null-safe {@link #compareVersion(VersionNumber) comparison} of the two {@link VersionNumber}s.
   */
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
