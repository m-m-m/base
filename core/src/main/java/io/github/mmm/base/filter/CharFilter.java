/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

/**
 * This is the interface for a filter that {@link #accept(char) decides} if a given character is acceptable or should be
 * filtered. <br>
 * Unlike {@link java.util.function.Predicate} it avoids conversion of {@code char} to {@link Character} for performance
 * reasons. It is especially used by {code net.sf.mmm.scanner.CharSequenceScanner}.
 */
public interface CharFilter {

  /**
   * A filter that only {@link #accept(char) accepts} characters valid for a technical identifier-string (e.g. literal
   * oder variable-name). This means accepted characters are Latin digits, ASCII letters or one of '.', '_' or '-'.
   */
  CharFilter IDENTIFIER_FILTER = (c) -> ((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'z'))
      || ((c >= 'A') && (c <= 'Z')) || (c == '_') || (c == '-') || (c == '.');

  /** A filter that only {@link #accept(char) accepts} the Latin digits '0'-'9' or ASCII letters 'a'-'z' and 'A'-'Z'. */
  CharFilter LATIN_DIGIT_OR_LETTER_FILTER = (c) -> ((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'z'))
      || ((c >= 'A') && (c <= 'Z'));

  /** A filter that only {@link #accept(char) accepts} the Latin digits '0'-'9'. */
  CharFilter LATIN_DIGIT_FILTER = (c) -> (c >= '0') && (c <= '9');

  /** A filter that only {@link #accept(char) accepts} the Latin ASCII letters 'a'-'z' and 'A'-'Z'. */
  CharFilter ASCII_LETTER_FILTER = (c) -> ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'));

  /** A filter that only {@link #accept(char) accepts} the lower case Latin ASCII letters 'a'-'z'. */
  CharFilter ASCII_LOWER_CASE_LETTER_FILTER = (c) -> (c >= 'a') && (c <= 'z');

  /** A filter that only {@link #accept(char) accepts} the upper case Latin ASCII letters 'A'-'Z'. */
  CharFilter ASCII_UPPER_CASE_LETTER_FILTER = (c) -> (c >= 'A') && (c <= 'Z');

  /** A filter that {@link #accept(char) accepts} only {@link Character#isWhitespace(char) whitespaces}. */
  CharFilter WHITESPACE_FILTER = (c) -> Character.isWhitespace(c);

  /** A filter that {@link #accept(char) accepts} any charater. */
  CharFilter ACCEPT_ALL_FILTER = (c) -> true;

  /** A filter that only {@link #accept(char) accepts} the file separator characters '/' and '\\'. */
  CharFilter FILE_SEPARATOR_FILTER = (c) -> (c == '/') || (c == '\\');

  /** {@link CharFilter} that {@link #accept(char) accepts} only carriage return ('\r') and line feed ('\n'). */
  CharFilter NEWLINE_FILTER = (c) -> (c == '\r') || (c == '\n');

  /** {@link CharFilter} that {@link #accept(char) accepts} only the ocatal digits '0'-'7'. */
  CharFilter OCTAL_DIGIT_FILTER = (c) -> (c >= '0') && (c <= '7');

  /** {@link CharFilter} that {@link #accept(char) accepts} only the hex digits '0'-'9', 'a'-'f', or 'A'-'F'. */
  CharFilter HEX_DIGIT_FILTER = (c) -> ((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'f'))
      || ((c >= 'A') && (c <= 'F'));

  /**
   * @param c is the character to check.
   * @return {@code true} if the given character {@code c} is acceptable, {@code false} if it should be filtered.
   */
  boolean accept(char c);

  /**
   * @return the negation of this {@link CharFilter} that returns !{@link #accept(char) accept(c)}.
   */
  default CharFilter negate() {

    return new NegatedCharFilter(this);
  }

  /**
   * @param filter the {@link CharFilter} to compose with.
   * @return a {@link ComposedCharFilter} that {@link #accept(char) accepts} a character that is accepted by
   *         {@code this} OR the given {@link CharFilter}.
   */
  default CharFilter compose(CharFilter filter) {

    return new ComposedCharFilter(new CharFilter[] { this, filter });
  }

}
