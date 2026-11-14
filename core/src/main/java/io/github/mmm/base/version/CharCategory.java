/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.version;

/// Enum with the available categories of a [Character] for a [VersionSegment].
enum CharCategory {

  /// Category for [separator][VersionSegment#getSeparator()] characters.
  SEPARATOR,

  /// Category for [letter][VersionSegment#getLetters()] characters.
  LETTER,

  /// Category for [digit][VersionSegment#getDigits()] characters.
  DIGIT;

  public static CharCategory of(char c) {

    if (isLetter(c)) {
      return LETTER;
    } else if (isDigit(c)) {
      return DIGIT;
    } else {
      return SEPARATOR;
    }
  }

  static boolean isDigit(char c) {

    return (c >= '0') && (c <= '9');
  }

  static boolean isLetter(char c) {

    return ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'));
  }

  static boolean isValidSeparator(char c) {

    return (c == '.') || (c == '-') || (c == '_');
  }

  static boolean isLetterSeparator(char c) {

    return (c == '-') || (c == '_');
  }

}
