/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.text;

import java.util.Locale;

/**
 * Simple helper for case conversion like {@link #toLowerCase(String)} or {@link #toUpperCase(String)}. This seems
 * pointless in the first place but prevents typical programming mistakes. Many developers will expect that
 * <code>"HI".{@link String#toLowerCase() toLowerCase()}</code> returns {@code "hi"}. However, this is not always the
 * case! {@link String#toLowerCase()} will actually do {@link String#toLowerCase(Locale)
 * toLowerCase}({@link Locale#getDefault()}). The latter operation is obviously a {@link Locale} sensitive operation and
 * can behave differently depending on the given {@link Locale}. So e.g. if your {@link Locale} is actually Turkish
 * ({@code tr_TR} resp. {@code tr-TR}) the result of <code>"HI".{@link String#toLowerCase() toLowerCase()}</code> will
 * actually be {@code "hı"} (notice the missing dot on the i character!). This can lead to serious bugs when creating
 * filenames, URLs or the like via case conversion. Due to such bug nobody with Turkish locale could install Lotus Notes
 * 8.5.1 when it was released.<br>
 * Simply use this small helper as indirection to the standard {@link String} case operations to prevent such mistakes
 * and also to document this fact.
 */
public final class CaseHelper {

  /** The standard locale to use instead of {@link Locale#getDefault()}. */
  static final Locale STANDARD_LOCALE = Locale.US;

  /**
   * Construction prohibited.
   */
  private CaseHelper() {

    super();
  }

  /**
   * Indirection for {@link String#toLowerCase(Locale)} with a standard {@link Locale} to prevent accidents. See
   * {@link CaseHelper} type description for details.
   *
   * @param string is the {@link String}.
   * @return the result of {@link String#toLowerCase(Locale)}.
   * @since 4.0.0
   */
  public static String toLowerCase(String string) {

    return string.toLowerCase(STANDARD_LOCALE);
  }

  /**
   * Indirection for {@link String#toLowerCase(Locale)}.
   *
   * @param string is the {@link String}.
   * @param locale is the {@link Locale}.
   * @return the result of {@link String#toLowerCase(Locale)}.
   */
  public static String toLowerCase(String string, Locale locale) {

    if (locale == null) {
      return toLowerCase(string, STANDARD_LOCALE);
    }
    return string.toLowerCase(locale);
  }

  /**
   * Indirection for {@link String#toUpperCase(Locale)} with a standard {@link Locale#US} to prevent accidents. See
   * {@link CaseHelper} type description for details.
   *
   * @param string is the {@link String}.
   * @return the result of {@link String#toUpperCase(Locale)}.
   */
  public static String toUpperCase(String string) {

    return string.toUpperCase(STANDARD_LOCALE);
  }

  /**
   * Indirection for {@link String#toUpperCase(Locale)}.
   *
   * @param string is the {@link String}.
   * @param locale is the {@link Locale}.
   * @return the result of {@link String#toUpperCase(Locale)}.
   */
  public static String toUpperCase(String string, Locale locale) {

    if (locale == null) {
      return toUpperCase(string, STANDARD_LOCALE);
    }
    return string.toUpperCase(locale);
  }

}
