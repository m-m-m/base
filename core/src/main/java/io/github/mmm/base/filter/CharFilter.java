/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

/**
 * Interface for a filter of characters that decides if a given character is {@link #accept(char) accepted}. <br>
 * Unlike {@link java.util.function.Predicate} it avoids boxing and unboxing between {@code char} and {@link Character}
 * for performance reasons. It is especially used by {code io.github.mmm.scanner.CharSequenceScanner}.
 *
 * @see #accept(char)
 * @since 1.0.0
 */
@FunctionalInterface
public interface CharFilter {

  /** A filter that only {@link #accept(char) accepts} the lower case Latin ASCII letters 'a'-'z'. */
  CharFilter LATIN_LOWER_CASE_LETTER = new RangeCharFilter('a', 'z');

  /** A filter that only {@link #accept(char) accepts} the upper case Latin ASCII letters 'A'-'Z'. */
  CharFilter LATIN_UPPER_CASE_LETTER = new RangeCharFilter('A', 'Z');

  /** A filter that only {@link #accept(char) accepts} the Latin ASCII letters 'a'-'z' and 'A'-'Z'. */
  CharFilter LATIN_LETTER = LATIN_LOWER_CASE_LETTER.compose(LATIN_UPPER_CASE_LETTER);

  /** A filter that only {@link #accept(char) accepts} the Latin digits '0'-'9'. */
  CharFilter LATIN_DIGIT = new RangeCharFilter('0', '9');

  /** A filter that only {@link #accept(char) accepts} the Latin digits '0'-'9' or ASCII letters 'a'-'z' and 'A'-'Z'. */
  CharFilter LATIN_LETTER_OR_DIGIT = LATIN_LETTER.compose(LATIN_DIGIT);

  /**
   * A filter that only {@link #accept(char) accepts} characters valid for a technical identifier (e.g. literal oder
   * variable-name). This means accepted characters are Latin digits, ASCII letters. '.', '_' or '-'.
   */
  CharFilter IDENTIFIER = LATIN_LETTER_OR_DIGIT.compose(new ListCharFilter("._-"));

  /**
   * A filter that only {@link #accept(char) accepts} characters valid for a technical segment (e.g. convenient name of
   * variable, method, field, class, etc.). This means accepted characters are Latin digits, ASCII letters, '_' or '$'.
   */
  CharFilter SEGMENT = LATIN_LETTER_OR_DIGIT.compose(new ListCharFilter("_$"));

  /** A filter that {@link #accept(char) accepts} only {@link Character#isWhitespace(char) whitespaces}. */
  CharFilter WHITESPACE = of(c -> Character.isWhitespace(c), "whitespace");

  /** A filter that {@link #accept(char) accepts} any charater. */
  CharFilter ANY = of(c -> true, "**");

  /** A filter that only {@link #accept(char) accepts} the file separator characters '/' and '\\'. */
  CharFilter FILE_SEPARATOR = new ListCharFilter("/\\");

  /** {@link CharFilter} that {@link #accept(char) accepts} only carriage return ('\r') and line feed ('\n'). */
  CharFilter NEWLINE = new ListCharFilter("\r\n");

  /** {@link CharFilter} that {@link #accept(char) accepts} only {@link #NEWLINE newlines} and space (' '). */
  CharFilter NEWLINE_OR_SPACE = new ListCharFilter("\r \n");

  /**
   * {@link CharFilter} that {@link #accept(char) accepts} only {@link #NEWLINE_OR_SPACE newlines, space} and tab
   * ('\t').
   */
  CharFilter NEWLINE_OR_SPACE_OR_TAB = new ListCharFilter("\r \n\t");

  /** {@link CharFilter} that {@link #accept(char) accepts} only the ocatal digits '0'-'7'. */
  CharFilter OCTAL_DIGIT = new RangeCharFilter('0', '7');

  /** {@link CharFilter} that {@link #accept(char) accepts} only the hex digits '0'-'9', 'a'-'f', or 'A'-'F'. */
  CharFilter HEX_DIGIT = LATIN_DIGIT.compose(new RangeCharFilter('a', 'f'))
      .compose(new RangeCharFilter('A', 'F'));

  /** Fallback for {@link #getDescription() description} if not available. */
  String NO_DESCRIPTION = "?";

  /**
   * @param c is the character to check.
   * @return {@code true} if the given character {@code c} is acceptable, {@code false} if it should be filtered.
   */
  boolean accept(char c);

  /**
   * Removes all characters that are not {@link #accept(char) accepted} by this filter. Intended only for simple cases
   * or testing. For real parsing consider using {@code CharStreamScanner}.
   *
   * @param string the {@link String} to filter.
   * @return the given {@link String} with all characters removed that are not {@link #accept(char) accepted} by this
   *         filter.
   */
  default String filter(String string) {

    if ((string == null) || string.isEmpty()) {
      return string;
    }
    StringBuilder sb = new StringBuilder(string.length());
    int len = string.length();
    for (int i = 0; i < len; i++) {
      char c = string.charAt(i);
      if (accept(c)) {
        sb.append(c);
      }
    }
    return sb.toString();
  }

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

  /**
   * Appends the given character to the given {@link StringBuilder}. In addition to {@link StringBuilder#append(char)}
   * this method will escape some special characters to make them visible.
   *
   * @param c the character to {@link StringBuilder#append(char) append}.
   * @param sb the {@link StringBuilder} where to append.
   */
  static void append(char c, StringBuilder sb) {

    if (c == '\n') {
      sb.append("\\n");
    } else if (c == '\r') {
      sb.append("\\r");
    } else if (c == '\t') {
      sb.append("\\t");
    } else if (isInvisible(c)) {
      sb.append("\\u");
      String hex = Integer.toString(c, 16);
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
      sb.append(c);
    }
  }

  private static boolean isInvisible(char c) {

    if (c < 20) { // technically space (20) is also invisible
      return true;
    } else if ((c >= 0x07F) && (c <= 0x0A0)) {
      return true;
    } else if ((c == 0x0AD)) {
      return true;
    }
    return false;
  }

  /**
   * @return the description of this filter. May be used in debug or error messages to explain the accepted characters.
   */
  default String getDescription() {

    return NO_DESCRIPTION;
  }

  /**
   * @param filter the {@link CharFilter} to wrap. Useful for lambdas to add {@link #getDescription() description}.
   * @param description the {@link #getDescription() description}.
   * @return the Wrapped {@link CharFilter} with the given {@link #getDescription() description}.
   */
  static CharFilter of(CharFilter filter, String description) {

    return new DescriptiveCharFilter(filter, description);
  }
}
