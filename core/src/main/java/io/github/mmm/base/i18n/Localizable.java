/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.i18n;

import java.util.Locale;

import io.github.mmm.base.impl.NotLocalizable;

/// Interface for an object that can be [translated][#getLocalizedMessage(Locale)] to a given [Locale]. It is
/// the API to abstract from `NlsMessage` provided by `mmm-nls`. To minimize dependencies you can mock it
/// with [#ofStatic(String)] wrapping a regular [String]. For real native language support (NLS) add
/// `mmm-nls` as dependency and require module `io.github.mmm.nls`.
public interface Localizable extends LocalizableObject {

  /// The internal bundle prefix used to for namespace separation and as workaround for limitations of the Java Module
  /// System.
  public static final String BUNDLE_PREFIX = "l10n.";

  /// **ATTENTION:**
  /// In most cases you wand to use [#getLocalizedMessage(Locale)] instead of this method.
  ///
  /// @return the untranslated message with arguments filled in. This results in the message in its original language
  ///         that should typically be English.
  /// @see #getLocalizedMessage()
  default String getMessage() {

    return getLocalizedMessage(Locale.ROOT);
  }

  /// This method tries to get the [localized message][#getLocalizedMessage(Locale)] as [String] using the
  /// [default locale][Locale#getDefault()]. **ATTENTION:**
  /// If possible try to avoid using this method and use [#getLocalizedMessage(Locale)] instead (e.g. using spring
  /// `LocaleContextHolder` to get the users locale).
  ///
  /// @return the localized message.
  default String getLocalizedMessage() {

    return getLocalizedMessage(Locale.getDefault());
  }

  /// This method gets the resolved and localized message.
  ///
  /// @param locale is the locale to translate to.
  /// @return the localized message.
  default String getLocalizedMessage(Locale locale) {

    StringBuilder result = new StringBuilder();
    getLocalizedMessage(locale, result);
    return result.toString();
  }

  /// This method writes the [localized message][#getLocalizedMessage(Locale)] to the given [Appendable].
  ///
  /// @see #getLocalizedMessage(Locale)
  ///
  /// @param locale the [Locale] to translate to.
  /// @param buffer the [Appendable] where to [write][Appendable#append(CharSequence)] the message to.
  void getLocalizedMessage(Locale locale, Appendable buffer);

  @Override
  default Localizable toLocalizable() {

    return this;
  }

  /// This method gets the language independent argument value for the given `key`.
  ///
  /// @param key is the name of the requested argument.
  /// @return the argument value for the given key or `null` if NOT defined.
  default Object getArgument(String key) {

    return null;
  }

  /// @param message the [message][#getMessage()].
  /// @return a dummy implementation of [Localizable] for the given `message` that does not support
  ///         localization and simply ignores any given [Locale].
  static Localizable ofStatic(String message) {

    return new NotLocalizable(message);
  }

  /// @param type the [Class] reflecting the context of the localization (e.g. [Enum] or `NlsMessage`).
  /// @return the derived [bundle name][java.util.ResourceBundle#getBundle(String, Locale)] for the given
  ///         [Class].
  static String createBundleName(Class<?> type) {

    return BUNDLE_PREFIX + type.getName();
  }

}
