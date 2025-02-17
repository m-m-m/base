/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.text;

import java.util.Locale;

/**
 * Represents the different character cases and allows {@link #convert(String) conversion}.
 *
 * @since 1.0.0
 */
public enum CaseConversion {

  /** Convert {@link String#toLowerCase() to lower case}. */
  LOWER_CASE {
    @Override
    public void append(StringBuilder sb, String text) {

      int len = text.length();
      int i = 0;
      while (i < len) {
        int cp = text.codePointAt(i);
        if ((cp >= 'A') && (cp <= 'Z')) {
          cp += 32; // A = 65, a = 97 (65+32)
        } else if ((cp < 0) || (cp > 256)) {
          cp = Character.toLowerCase(cp);
        }
        sb.appendCodePoint(cp);
        if (cp > 0x010000) {
          i = i + 2;
        } else {
          i++;
        }
      }
    }
  },

  /** Convert {@link String#toUpperCase() to upper case}. */
  UPPER_CASE {
    @Override
    public void append(StringBuilder sb, String text) {

      int len = text.length();
      int i = 0;
      while (i < len) {
        int cp = text.codePointAt(i);
        if ((cp >= 'a') && (cp <= 'z')) {
          cp -= 32; // a = 97, A = 65 (97-32)
        } else if ((cp < 0) || (cp > 256)) {
          cp = Character.toUpperCase(cp);
        }
        sb.appendCodePoint(cp);
        if (cp > 0x010000) {
          i = i + 2;
        } else {
          i++;
        }
      }
    }
  },

  /** Do not convert (keep original case). */
  ORIGINAL_CASE {
    @Override
    public void append(StringBuilder sb, String text) {

      sb.append(text);
    }
  };

  /**
   * The character representing {@link #ORIGINAL_CASE} in {@link #ofExample(char, boolean) examples}. Was chosen so it
   * can be used in java names (including packages), paths and URIs but is unlikely to conflict with other semantical
   * usage. However, this choice is arbitrarily.
   */
  public static final char EXAMPLE_CHAR_FOR_ORIGINAL_CASE = '$';

  /**
   * @param string the original {@link String}.
   * @return the converted {@link String}.
   */
  public String convert(String string) {

    return convert(string, null);
  }

  /**
   * @param string the original {@link String}.
   * @param locale the explicit {@link Locale} to use. In most cases you want to use {@link #convert(String)} instead.
   * @return the converted {@link String}.
   */
  public String convert(String string, Locale locale) {

    if (string == null) {
      return null;
    }
    switch (this) {
      case LOWER_CASE:
        return CaseHelper.toLowerCase(string, locale);
      case UPPER_CASE:
        return CaseHelper.toUpperCase(string, locale);
      case ORIGINAL_CASE:
      default:
        return string;
    }
  }

  /**
   * @param c the original character.
   * @return the converted character.
   */
  public char convert(char c) {

    switch (this) {
      case LOWER_CASE:
        return Character.toLowerCase(c);
      case UPPER_CASE:
        return Character.toUpperCase(c);
      case ORIGINAL_CASE:
      default:
        return c;
    }
  }

  /**
   * @param sb the {@link StringBuilder} to {@link StringBuilder#append(String) append} to.
   * @param text the text to append.
   */
  public abstract void append(StringBuilder sb, String text);

  /**
   * @param c the character to {@link #convert(char) convert}.
   * @return the {@link #convert(char) converted} character {@code c} but {@link #EXAMPLE_CHAR_FOR_ORIGINAL_CASE} if
   *         this is {@link #ORIGINAL_CASE}.
   */
  public char asExample(char c) {

    if (this == CaseConversion.ORIGINAL_CASE) {
      return EXAMPLE_CHAR_FOR_ORIGINAL_CASE;
    } else {
      return convert(c);
    }
  }

  /**
   * @param c the example character.
   * @param strict if the character has to be an upper/lower case letter or {@link #EXAMPLE_CHAR_FOR_ORIGINAL_CASE}.
   * @return {@link #UPPER_CASE} if the given character is {@link Character#isUpperCase(char) upper case},
   *         {@link #LOWER_CASE} if the given character is {@link Character#isLowerCase(char) lower case}, and
   *         {@link #ORIGINAL_CASE} otherwise (might need to be remapped or handled specially).
   */
  public static CaseConversion ofExample(char c, boolean strict) {

    if (Character.isUpperCase(c)) {
      return UPPER_CASE;
    } else if (Character.isLowerCase(c)) {
      return LOWER_CASE;
    } else {
      if (strict && (c != EXAMPLE_CHAR_FOR_ORIGINAL_CASE)) {
        throw new IllegalArgumentException(Character.toString(c));
      }
      return ORIGINAL_CASE;
    }
  }

  /**
   * @param caseConversion the {@link CaseConversion} to test. May be {@code null}.
   * @return {@code true} if the given {@link CaseConversion} is {@link #LOWER_CASE} or {@link #UPPER_CASE}.
   */
  public static boolean isCaseChange(CaseConversion caseConversion) {

    return (caseConversion != null) && (caseConversion != ORIGINAL_CASE);
  }

  /**
   * @param case1 the first {@link CaseConversion}.
   * @param case2 the second {@link CaseConversion}.
   * @return {@code true} if both {@link CaseConversion}s are incompatible to each other. This means they are not same
   *         and are both {@link #isCaseChange(CaseConversion) case changing}. Otherwise {@code false}.
   */
  public static boolean areIncompatible(CaseConversion case1, CaseConversion case2) {

    return (case1 != case2) && isCaseChange(case1) && isCaseChange(case2);
  }

}
