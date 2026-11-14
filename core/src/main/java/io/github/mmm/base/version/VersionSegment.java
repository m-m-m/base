/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.version;

import java.util.Objects;

/// Represents a single segment of a [VersionNumber].
public class VersionSegment implements VersionObject<VersionSegment> {

  static final VersionSegment EMPTY = new VersionSegment("", "", "");

  private final String separator;

  private final VersionLetters letters;

  private final String digits;

  private final int number;

  private VersionSegment next;

  /// The constructor.
  ///
  /// @param separator the [separator][#getSeparator()].
  /// @param letters the [letters][#getLettersString()].
  /// @param digits the [digits][#getDigits()].
  VersionSegment(String separator, String letters, String digits) {

    super();
    this.separator = separator;
    this.letters = VersionLetters.of(letters);
    this.digits = digits;
    if (this.digits.isEmpty()) {
      this.number = -1;
    } else {
      this.number = Integer.parseInt(this.digits);
    }
  }

  private VersionSegment(VersionSegment next, String separator, VersionLetters letters, String digits, int number) {

    super();
    this.next = next;
    this.separator = separator;
    this.letters = letters;
    this.digits = digits;
    this.number = number;
  }

  /// @return the separator [String] (e.g. "." or "-") or the empty [String] ("") for none.
  public String getSeparator() {

    return this.separator;
  }

  /// @return the letters or the empty [String] ("") for none. In canonical [VersionNumber]s letters indicate
  ///         the development phase (e.g. "pre", "rc", "alpha", "beta", "milestone", "test", "dev", "SNAPSHOT", etc.).
  ///         However, letters are technically any [letter characters][Character#isLetter(char)] and may also be
  ///         something like a code-name (e.g. "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb", "Ice
  ///         Cream Sandwich", "Jelly Bean" in case of Android internals). Please note that in such case it is impossible
  ///         to properly decide which version is greater than another versions. To avoid mistakes, the comparison
  ///         supports a strict mode that will let the comparison fail in such case. However, by default (e.g. for
  ///         [Comparable#compareTo(Object)]) the default [string comparison][String#compareTo(String)]
  ///         (lexicographical) is used to ensure a natural order.
  /// @see #getPhase()
  public String getLettersString() {

    return this.letters.getLetters();
  }

  /// @return the [VersionLetters].
  public VersionLetters getLetters() {

    return this.letters;
  }

  /// @return the [VersionPhase] for the [letters][#getLettersString()]. Will be
  ///         [VersionPhase#UNDEFINED] if unknown and hence never `null`.
  /// @see #getLettersString()
  public VersionPhase getPhase() {

    return this.letters.getPhase();
  }

  /// @return the digits or the empty [String] ("") for none. This is the actual [number][#getNumber()] part
  ///         of this [VersionSegment]. So the [VersionNumber] "1.0.001" will have three segments: The first
  ///         one with "1" as digits, the second with "0" as digits, and a third with "001" as digits. You can get the
  ///         same value via [#getNumber()] but this [String] representation will preserve leading zeros.
  public String getDigits() {

    return this.digits;
  }

  /// @return the [digits][#getDigits()] and integer number. Will be `-1` if no [digits][#getDigits()]
  ///         are present.
  public int getNumber() {

    return this.number;
  }

  /// @return the next [VersionSegment] or `null` if this is the tail of the [VersionNumber].
  public VersionSegment getNext() {

    return this.next;
  }

  /// @return the next [VersionSegment] or the empty segment if this is the tail of the [VersionNumber].
  VersionSegment getNextOrEmpty() {

    if (this.next == null) {
      return EMPTY;
    }
    return this.next;
  }

  /// A valid [VersionSegment] has to meet the following requirements:
  ///
  /// - The [separator][#getSeparator()] may not be [longer][String#length()] than a single character (e.g.
  /// ".-_1" or "--1" are not considered valid).
  /// - The [separator][#getSeparator()] may only contain the characters '.', '-', or '_' (e.g. " 1" or "ö1" are
  /// not considered valid).
  /// - The combination of [phase][#getPhase()] and [number][#getNumber()] has to be
  /// [valid][VersionPhase#isValid(int)] (e.g. "pineapple-pen1" or "donut" are not considered valid).
  @Override
  public boolean isValid() {

    int separatorLen = this.separator.length();
    if (separatorLen > 1) {
      return false;
    } else if (separatorLen == 1) {
      if (!CharCategory.isValidSeparator(this.separator.charAt(0))) {
        return false;
      }
    }
    return this.letters.getPhase().isValid(this.number);
  }

  @Override
  public VersionComparisonResult compareVersion(VersionSegment other) {

    if (other == null) {
      return VersionComparisonResult.GREATER_UNSAFE;
    }
    VersionComparisonResult lettersResult = this.letters.compareVersion(other.letters);
    if (!lettersResult.isEqual()) {
      return lettersResult;
    }
    if (!"_".equals(this.separator) && "_".equals(other.separator)) {
      if ("".equals(this.separator)) {
        return VersionComparisonResult.LESS;
      } else {
        return VersionComparisonResult.GREATER;
      }
    } else if ("_".equals(this.separator) && !"_".equals(other.separator)) {
      if ("".equals(other.separator)) {
        return VersionComparisonResult.GREATER;
      } else {
        return VersionComparisonResult.LESS;
      }
    }

    if (this.number != other.number) {
      if (this.number < other.number) {
        return VersionComparisonResult.LESS;
      } else {
        return VersionComparisonResult.GREATER;
      }
    } else if (this.separator.equals(other.separator)) {
      return VersionComparisonResult.EQUAL;
    } else {
      return VersionComparisonResult.EQUAL_UNSAFE;
    }
  }

  /// @return the [VersionLetters] that represent a [development phase][VersionLetters#isDevelopmentPhase()]
  ///         searching from this [VersionSegment] to all [next segments][#getNext()]. Will be
  ///         [VersionPhase#NONE] if no [development phase][VersionPhase#isDevelopmentPhase()] was found and
  ///         [VersionPhase#UNDEFINED] if multiple [development phase][VersionPhase#isDevelopmentPhase()]s
  ///         have been found.
  /// @see VersionNumber#getDevelopmentPhase()
  protected VersionLetters getDevelopmentPhase() {

    VersionLetters result = VersionLetters.EMPTY;
    VersionSegment segment = this;
    while (segment != null) {
      if (segment.letters.isDevelopmentPhase()) {
        if (result == VersionLetters.EMPTY) {
          result = segment.letters;
        } else {
          result = VersionLetters.UNDEFINED;
        }
      }
      segment = segment.next;
    }
    return result;
  }

  /// [VersionNumber#incrementSegment(int, boolean)] Increments a version} recursively per [VersionSegment].
  ///
  /// @param digitKeepCount the number of leading [VersionSegment]s with [digits][VersionSegment#getDigits()]
  ///        to keep untouched. Will be `0` for the segment to increment and negative for the segments to set to
  ///        zero.
  /// @param keepLetters `true` to keep [letters][VersionSegment#getLetters()] from modified segments,
  ///        `false` to drop them.
  /// @return the new [VersionSegment].
  VersionSegment increment(int digitKeepCount, boolean keepLetters) {

    String newSeparator = this.separator;
    VersionLetters newLetters = this.letters;
    String newDigits = this.digits;
    int newNumber = this.number;
    int nextSegmentKeepCount = digitKeepCount;
    if (this.number >= 0) {
      nextSegmentKeepCount--;
    }
    if ((digitKeepCount < 0) || ((digitKeepCount == 0) && (this.number >= 0))) {
      if (!keepLetters) {
        newLetters = VersionLetters.EMPTY;
      }
      if (newNumber >= 0) {
        if (digitKeepCount == 0) {
          newNumber++;
        } else {
          newNumber = 0;
        }
        int digitsLength = newDigits.length();
        newDigits = Integer.toString(newNumber);
        int leadingZeros = digitsLength - newDigits.length();
        if (leadingZeros > 0) {
          StringBuilder newDigitsBuilder = new StringBuilder(newDigits);
          while (leadingZeros > 0) {
            newDigitsBuilder.insert(0, "0");
            leadingZeros--;
          }
          newDigits = newDigitsBuilder.toString();
        }
      } else if (!keepLetters) {
        if (this.next == null) {
          return null;
        }
        return this.next.increment(nextSegmentKeepCount, false);
      }
    }
    VersionSegment nextSegment = null;
    if (this.next != null) {
      nextSegment = this.next.increment(nextSegmentKeepCount, keepLetters);
    }
    return new VersionSegment(nextSegment, newSeparator, newLetters, newDigits, newNumber);
  }

  /// @return the number of [VersionSegment]s with [digits][VersionSegment#getDigits()].
  int countDigits() {

    int count = 0;
    if (this.number >= 0) {
      count = 1;
    }
    if (this.next != null) {
      count = count + this.next.countDigits();
    }
    return count;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    } else if (obj instanceof VersionSegment segment) {
      if (!Objects.equals(this.digits, segment.digits)) {
        return false;
      } else if (!Objects.equals(this.separator, segment.separator)) {
        return false;
      } else if (!Objects.equals(this.letters, segment.letters)) {
        return false;
      } else if (!Objects.equals(this.next, segment.next)) {
        return false;
      }
      return true;
    }
    return false;
  }

  @Override
  public String toString() {

    return this.separator + this.letters + this.digits;
  }

  static VersionSegment of(String version) {

    CharReader reader = new CharReader(version);
    VersionSegment start = null;
    VersionSegment current = null;
    while (reader.hasNext()) {
      VersionSegment segment = parseSegment(reader);
      if (current == null) {
        start = segment;
      } else {
        current.next = segment;
      }
      current = segment;
    }
    return start;
  }

  private static VersionSegment parseSegment(CharReader reader) {

    String separator = reader.readSeparator();
    String letters = reader.readLetters();
    String digits = reader.readDigits();
    return new VersionSegment(separator, letters, digits);
  }

}
