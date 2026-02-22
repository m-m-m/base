/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.version;

import java.util.Objects;

/**
 * Represents a single segment of a {@link VersionNumber}.
 */
public class VersionSegment implements VersionObject<VersionSegment> {

  static final VersionSegment EMPTY = new VersionSegment("", "", "");

  private final String separator;

  private final VersionLetters letters;

  private final String digits;

  private final int number;

  private VersionSegment next;

  /**
   * The constructor.
   *
   * @param separator the {@link #getSeparator() separator}.
   * @param letters the {@link #getLettersString() letters}.
   * @param digits the {@link #getDigits() digits}.
   */
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

  /**
   * @return the separator {@link String} (e.g. "." or "-") or the empty {@link String} ("") for none.
   */
  public String getSeparator() {

    return this.separator;
  }

  /**
   * @return the letters or the empty {@link String} ("") for none. In canonical {@link VersionNumber}s letters indicate
   *         the development phase (e.g. "pre", "rc", "alpha", "beta", "milestone", "test", "dev", "SNAPSHOT", etc.).
   *         However, letters are technically any {@link Character#isLetter(char) letter characters} and may also be
   *         something like a code-name (e.g. "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb", "Ice
   *         Cream Sandwich", "Jelly Bean" in case of Android internals). Please note that in such case it is impossible
   *         to properly decide which version is greater than another versions. To avoid mistakes, the comparison
   *         supports a strict mode that will let the comparison fail in such case. However, by default (e.g. for
   *         {@link Comparable#compareTo(Object)}) the default {@link String#compareTo(String) string comparison}
   *         (lexicographical) is used to ensure a natural order.
   * @see #getPhase()
   */
  public String getLettersString() {

    return this.letters.getLetters();
  }

  /**
   * @return the {@link VersionLetters}.
   */
  public VersionLetters getLetters() {

    return this.letters;
  }

  /**
   * @return the {@link VersionPhase} for the {@link #getLettersString() letters}. Will be
   *         {@link VersionPhase#UNDEFINED} if unknown and hence never {@code null}.
   * @see #getLettersString()
   */
  public VersionPhase getPhase() {

    return this.letters.getPhase();
  }

  /**
   * @return the digits or the empty {@link String} ("") for none. This is the actual {@link #getNumber() number} part
   *         of this {@link VersionSegment}. So the {@link VersionNumber} "1.0.001" will have three segments: The first
   *         one with "1" as digits, the second with "0" as digits, and a third with "001" as digits. You can get the
   *         same value via {@link #getNumber()} but this {@link String} representation will preserve leading zeros.
   */
  public String getDigits() {

    return this.digits;
  }

  /**
   * @return the {@link #getDigits() digits} and integer number. Will be {@code -1} if no {@link #getDigits() digits}
   *         are present.
   */
  public int getNumber() {

    return this.number;
  }

  /**
   * @return the next {@link VersionSegment} or {@code null} if this is the tail of the {@link VersionNumber}.
   */
  public VersionSegment getNext() {

    return this.next;
  }

  /**
   * @return the next {@link VersionSegment} or the empty segment if this is the tail of the {@link VersionNumber}.
   */
  VersionSegment getNextOrEmpty() {

    if (this.next == null) {
      return EMPTY;
    }
    return this.next;
  }

  /**
   * A valid {@link VersionSegment} has to meet the following requirements:
   * <ul>
   * <li>The {@link #getSeparator() separator} may not be {@link String#length() longer} than a single character (e.g.
   * ".-_1" or "--1" are not considered valid).</li>
   * <li>The {@link #getSeparator() separator} may only contain the characters '.', '-', or '_' (e.g. " 1" or "ö1" are
   * not considered valid).</li>
   * <li>The combination of {@link #getPhase() phase} and {@link #getNumber() number} has to be
   * {@link VersionPhase#isValid(int) valid} (e.g. "pineapple-pen1" or "donut" are not considered valid).</li>
   * </ul>
   */
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

  /**
   * @return the {@link VersionLetters} that represent a {@link VersionLetters#isDevelopmentPhase() development phase}
   *         searching from this {@link VersionSegment} to all {@link #getNext() next segments}. Will be
   *         {@link VersionPhase#NONE} if no {@link VersionPhase#isDevelopmentPhase() development phase} was found and
   *         {@link VersionPhase#UNDEFINED} if multiple {@link VersionPhase#isDevelopmentPhase() development phase}s
   *         have been found.
   * @see VersionNumber#getDevelopmentPhase()
   */
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

  /**
   * {@link VersionNumber#incrementSegment(int, boolean)} Increments a version} recursively per {@link VersionSegment}.
   *
   * @param digitKeepCount the number of leading {@link VersionSegment}s with {@link VersionSegment#getDigits() digits}
   *        to keep untouched. Will be {@code 0} for the segment to increment and negative for the segments to set to
   *        zero.
   * @param keepLetters {@code true} to keep {@link VersionSegment#getLetters() letters} from modified segments,
   *        {@code false} to drop them.
   * @return the new {@link VersionSegment}.
   */
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

  /**
   * @return the number of {@link VersionSegment}s with {@link VersionSegment#getDigits() digits}.
   */
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
