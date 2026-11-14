/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.text;

import java.util.Locale;

/// Simple helper for case conversion like [#toLowerCase(String)] or [#toUpperCase(String)]. This seems
/// pointless in the first place but prevents typical programming mistakes. Many developers will expect that
/// `"HI".[toLowerCase()][String#toLowerCase()]` returns `"hi"`. However, this is not always the
/// case! [String#toLowerCase()] will actually do [toLowerCase][String#toLowerCase(Locale)]([Locale#getDefault()]). The latter operation is obviously a [Locale] sensitive operation and
/// can behave differently depending on the given [Locale]. So e.g. if your [Locale] is actually Turkish
/// (`tr_TR` resp. `tr-TR`) the result of `"HI".[toLowerCase()][String#toLowerCase()]` will
/// actually be `"hı"` (notice the missing dot on the i character!). This can lead to serious bugs when creating
/// filenames, URLs or the like via case conversion. Due to such bug nobody with Turkish locale could install Lotus Notes
/// 8.5.1 when it was released.
/// Simply use this small helper as indirection to the standard [String] case operations to prevent such mistakes
/// and also to document this fact.
///
/// @since 1.0.0
public final class CaseHelper {

  /// The standard locale to use instead of [Locale#getDefault()].
  static final Locale STANDARD_LOCALE = Locale.ROOT;

  /// Construction prohibited.
  private CaseHelper() {

    super();
  }

  /// Indirection for [String#toLowerCase(Locale)] with a standard [Locale] to prevent accidents. See
  /// [CaseHelper] type description for details.
  ///
  /// @param string is the [String].
  /// @return the result of [String#toLowerCase(Locale)].
  /// @since 4.0.0
  public static String toLowerCase(String string) {

    return string.toLowerCase(STANDARD_LOCALE);
  }

  /// Indirection for [String#toLowerCase(Locale)].
  ///
  /// @param string is the [String].
  /// @param locale is the [Locale].
  /// @return the result of [String#toLowerCase(Locale)].
  public static String toLowerCase(String string, Locale locale) {

    if (locale == null) {
      return toLowerCase(string, STANDARD_LOCALE);
    }
    return string.toLowerCase(locale);
  }

  /// Indirection for [String#toUpperCase(Locale)] with a standard [Locale#US] to prevent accidents. See
  /// [CaseHelper] type description for details.
  ///
  /// @param string is the [String].
  /// @return the result of [String#toUpperCase(Locale)].
  public static String toUpperCase(String string) {

    return string.toUpperCase(STANDARD_LOCALE);
  }

  /// Indirection for [String#toUpperCase(Locale)].
  ///
  /// @param string is the [String].
  /// @param locale is the [Locale].
  /// @return the result of [String#toUpperCase(Locale)].
  public static String toUpperCase(String string, Locale locale) {

    if (locale == null) {
      return toUpperCase(string, STANDARD_LOCALE);
    }
    return string.toUpperCase(locale);
  }

  /// @param string is the [String]. May be `null`.
  /// @return the capitalized [String].
  public static String capitalize(String string) {

    if ((string != null) && !string.isEmpty()) {
      char first = string.charAt(0);
      char upper = Character.toUpperCase(first);
      if (upper != first) {
        return upper + string.substring(1);
      }
    }
    return string;
  }

  /// @param string is the [String]. May be `null`.
  /// @return the capitalized [String].
  public static String uncapitalize(String string) {

    if ((string != null) && !string.isEmpty()) {
      char first = string.charAt(0);
      char lower = Character.toLowerCase(first);
      if (lower != first) {
        return lower + string.substring(1);
      }
    }
    return string;
  }

}
