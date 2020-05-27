/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.i18n;

import java.util.Locale;

import io.github.mmm.base.impl.NotLocalizable;

/**
 * Interface for an object that can be {@link #getLocalizedMessage(Locale) translated} to a given {@link Locale}. It is
 * the API to abstract from {@code NlsMessage} provided by {@code mmm-nls}. To minimize dependencies you can mock it
 * with {@link #ofStatic(String)} wrapping a regular {@link String}. For real native language support (NLS) add
 * {@code mmm-nls} as dependency and require module {@code io.github.mmm.nls}.
 */
public interface Localizable extends LocalizableObject {

  /**
   * <b>ATTENTION:</b><br>
   * In most cases you wand to use {@link #getLocalizedMessage(Locale)} instead of this method.
   *
   * @return the untranslated message with arguments filled in. This results in the message in its original language
   *         that should typically be English.
   * @see #getLocalizedMessage()
   */
  default String getMessage() {

    return getLocalizedMessage(Locale.ROOT);
  }

  /**
   * This method tries to get the {@link #getLocalizedMessage(Locale) localized message} as {@link String} using the
   * {@link Locale#getDefault() default locale}. <b>ATTENTION:</b><br>
   * If possible try to avoid using this method and use {@link #getLocalizedMessage(Locale)} instead (e.g. using spring
   * {@code LocaleContextHolder} to get the users locale).
   *
   * @return the localized message.
   */
  default String getLocalizedMessage() {

    return getLocalizedMessage(Locale.getDefault());
  }

  /**
   * This method gets the resolved and localized message.
   *
   * @param locale is the locale to translate to.
   * @return the localized message.
   */
  default String getLocalizedMessage(Locale locale) {

    StringBuilder result = new StringBuilder();
    getLocalizedMessage(locale, result);
    return result.toString();
  }

  /**
   * This method writes the {@link #getLocalizedMessage(Locale) localized message} to the given {@link Appendable}. <br>
   *
   * @see #getLocalizedMessage(Locale)
   *
   * @param locale is the locale to translate to.
   * @param buffer is the {@link Appendable} where to {@link Appendable#append(CharSequence) write} the message to.
   */
  void getLocalizedMessage(Locale locale, Appendable buffer);

  @Override
  default Localizable toLocalizable() {

    return this;
  }

  /**
   * This method gets the language independent argument value for the given {@code key}.
   *
   * @param key is the name of the requested argument.
   * @return the argument value for the given key or {@code null} if NOT defined.
   */
  default Object getArgument(String key) {

    return null;
  }

  /**
   * @param message the {@link #getMessage() message}.
   * @return a dummy implementation of {@link Localizable} for the given {@code message} that does not support
   *         localization and simply ignores any given {@link Locale}.
   */
  static Localizable ofStatic(String message) {

    return new NotLocalizable(message);
  }

  /**
   * @param type the {@link Class} reflecting the context of the localization (e.g. {@link Enum} or {@code NlsMessage}).
   * @return the derived {@link java.util.ResourceBundle#getBundle(String, Locale) bundle name} for the given
   *         {@link Class}.
   */
  static String createBundleName(Class<?> type) {

    return "l10n." + type.getName();
  }

}
