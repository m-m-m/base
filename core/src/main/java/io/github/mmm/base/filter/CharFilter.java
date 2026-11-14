/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

/// Interface for a filter of characters that decides if a given character is [accepted][#accept(int)].
/// Unlike [java.util.function.Predicate] it avoids boxing and unboxing between `char` and [Character]
/// for performance reasons. It is especially used by {code io.github.mmm.scanner.CharSequenceScanner}.
///
/// @see #accept(int)
/// @since 1.0.0
@FunctionalInterface
public interface CharFilter {

  /// A filter that only [accepts][#accept(int)] the lower case Latin ASCII letters 'a'-'z'.
  CharFilter LATIN_LOWER_CASE_LETTER = new RangeCharFilter('a', 'z');

  /// A filter that only [accepts][#accept(int)] the upper case Latin ASCII letters 'A'-'Z'.
  CharFilter LATIN_UPPER_CASE_LETTER = new RangeCharFilter('A', 'Z');

  /// A filter that only [accepts][#accept(int)] the Latin ASCII letters 'a'-'z' and 'A'-'Z'.
  CharFilter LATIN_LETTER = LATIN_LOWER_CASE_LETTER.compose(LATIN_UPPER_CASE_LETTER);

  /// A filter that only [accepts][#accept(int)] the Latin digits '0'-'9'.
  CharFilter LATIN_DIGIT = new RangeCharFilter('0', '9');

  /// A filter that only [accepts][#accept(int)] the Latin digits '0'-'9' or ASCII letters 'a'-'z' and 'A'-'Z'.
  CharFilter LATIN_LETTER_OR_DIGIT = LATIN_LETTER.compose(LATIN_DIGIT);

  /// A filter that only [accepts][#accept(int)] characters valid for a technical identifier (e.g. literal oder
  /// variable-name). This means accepted characters are Latin digits, ASCII letters. '.', '_' or '-'.
  CharFilter IDENTIFIER = LATIN_LETTER_OR_DIGIT.compose(new ListCharFilter("._-"));

  /// A filter that only [accepts][#accept(int)] characters valid for a technical segment (e.g. convenient name of
  /// variable, method, field, class, etc.). This means accepted characters are Latin digits, ASCII letters, '_' or '$'.
  CharFilter SEGMENT = LATIN_LETTER_OR_DIGIT.compose(new ListCharFilter("_$"));

  /// A filter that [accepts][#accept(int)] only [whitespaces][Character#isWhitespace(char)].
  CharFilter WHITESPACE = of(c -> Character.isWhitespace(c), "whitespace");

  /// A filter that [accepts][#accept(int)] any charater.
  CharFilter ANY = of(_ -> true, "**");

  /// A filter that only [accepts][#accept(int)] the file separator characters '/' and '\\'.
  CharFilter FILE_SEPARATOR = new ListCharFilter("/\\");

  /// [CharFilter] that [accepts][#accept(int)] only carriage return ('\r') and line feed ('\n').
  CharFilter NEWLINE = new ListCharFilter("\r\n");

  /// [CharFilter] that [accepts][#accept(int)] only [newlines][#NEWLINE] and space (' ').
  CharFilter NEWLINE_OR_SPACE = new ListCharFilter("\r \n");

  /// [CharFilter] that [accepts][#accept(int)] only [newlines, space][#NEWLINE_OR_SPACE] and tab ('\t').
  CharFilter NEWLINE_OR_SPACE_OR_TAB = new ListCharFilter("\r \n\t");

  /// [CharFilter] that [accepts][#accept(int)] only the ocatal digits '0'-'7'.
  CharFilter OCTAL_DIGIT = new RangeCharFilter('0', '7');

  /// [CharFilter] that [accepts][#accept(int)] only the hex digits '0'-'9', 'a'-'f', or 'A'-'F'.
  CharFilter HEX_DIGIT = LATIN_DIGIT.compose(new RangeCharFilter('a', 'f')).compose(new RangeCharFilter('A', 'F'));

  /// Fallback for [description][#getDescription()] if not available.
  String NO_DESCRIPTION = "?";

  /// @param codePoint is the [code-point][String#codePointAt(int)] to check.
  /// @return `true` if the given `codePoint` is acceptable, `false` if it should be filtered.
  boolean accept(int codePoint);

  /// Removes all characters that are not [accepted][#accept(int)] by this filter. Intended only for simple cases or
  /// testing. For real parsing consider using `CharStreamScanner`.
  ///
  /// @param string the [String] to filter.
  /// @return the given [String] with all characters removed that are not [accepted][#accept(int)] by this
  ///         filter.
  default String filter(String string) {

    if ((string == null) || string.isEmpty()) {
      return string;
    }
    StringBuilder sb = new StringBuilder(string.length());
    int len = string.length();
    for (int i = 0; i < len; i++) {
      int cp = string.codePointAt(i);
      if (accept(cp)) {
        sb.appendCodePoint(cp);
      }
    }
    return sb.toString();
  }

  /// @return the negation of this [CharFilter] that returns ![accept(c)][#accept(int)].
  default CharFilter negate() {

    return new NegatedCharFilter(this);
  }

  /// @param filter the [CharFilter] to compose with.
  /// @return a [ComposedCharFilter] that [accepts][#accept(int)] a character that is accepted by `this`
  ///         OR the given [CharFilter].
  default CharFilter compose(CharFilter filter) {

    return new ComposedCharFilter(new CharFilter[] { this, filter });
  }

  /// Appends the given character to the given [StringBuilder]. In addition to [StringBuilder#append(char)]
  /// this method will escape some special characters to make them visible.
  ///
  /// @param codePoint the [code-point][String#codePointAt(int)] to [append][StringBuilder#appendCodePoint(int)].
  /// @param sb the [StringBuilder] where to append.
  static void append(int codePoint, StringBuilder sb) {

    if (codePoint == '\n') {
      sb.append("\\n");
    } else if (codePoint == '\r') {
      sb.append("\\r");
    } else if (codePoint == '\t') {
      sb.append("\\t");
    } else if (isInvisible(codePoint)) {
      sb.append("\\u");
      String hex = Integer.toString(codePoint, 16);
      int len = hex.length();
      int zeros = 4 - len;
      if (zeros < 0) {
        zeros = 8 - len;
      }
      while (zeros > 0) {
        sb.append('0');
        zeros--;
      }
      sb.append(hex);
    } else {
      sb.appendCodePoint(codePoint);
    }
  }

  private static boolean isInvisible(int codePoint) {

    if (codePoint < 20) { // technically space (20) is also invisible
      return true;
    } else if ((codePoint >= 0x07F) && (codePoint <= 0x0A0)) {
      return true;
    } else if ((codePoint == 0x0AD)) {
      return true;
    }
    return false;
  }

  /// @return the description of this filter. May be used in debug or error messages to explain the accepted characters.
  default String getDescription() {

    return NO_DESCRIPTION;
  }

  /// @param filter the [CharFilter] to wrap. Useful for lambdas to add [description][#getDescription()].
  /// @param description the [description][#getDescription()].
  /// @return the Wrapped [CharFilter] with the given [description][#getDescription()].
  static CharFilter of(CharFilter filter, String description) {

    return new DescriptiveCharFilter(filter, description);
  }
}
