/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.i18n;

import java.util.Locale;
import java.util.Locale.Builder;

import io.github.mmm.base.filter.CharFilter;

/**
 * Simple helper for {@link Locale} specific stuff.
 *
 * @since 1.0.0
 */
public final class LocaleHelper {

  /** The default separator used for {@link #toInfixes(Locale) locale-infixes}. */
  public static final char SEPARATOR_DEFAULT = '_';

  /** The separator used for {@link Locale#forLanguageTag(String) language tag}. */
  public static final char SEPARATOR_FOR_LANGUAGE_TAG = '-';

  /** The separator used for {@link Locale#getExtensionKeys() extension keys}. */
  public static final char SEPARATOR_EXTENSION = '-';

  /** The separator used for {@link Locale#getScript() script}. */
  public static final char SEPARATOR_SCRIPT = '#';

  private LocaleHelper() {

    super();
  }

  /**
   * @param locale is the {@link Locale}.
   * @return the localization-infix in the form
   *         <code>[_{@link Locale#getLanguage() language}[_{@link Locale#getCountry() country}[_{@link Locale#getVariant() variant}]]]</code>.
   *         Will be the empty {@link String} for the {@link Locale#ROOT root locale}. E.g. for {@link Locale#GERMANY}
   *         this method will return "_de_DE".
   */
  public static String toInfix(Locale locale) {

    StringBuilder infix = new StringBuilder();
    String language = locale.getLanguage();
    boolean hasLanguage = (language != null) && (language.length() == 2);
    if (hasLanguage) {
      infix.append(SEPARATOR_DEFAULT);
      infix.append(language);
    }
    String country = locale.getCountry();
    boolean hasCountry = (country != null) && (country.length() == 2);
    if (hasCountry) {
      if (!hasLanguage) {
        infix.append(SEPARATOR_DEFAULT);
      }
      infix.append(SEPARATOR_DEFAULT);
      infix.append(country);
    }
    String variant = locale.getVariant();
    if ((variant != null) && (variant.length() > 0)) {
      if (!hasCountry) {
        infix.append(SEPARATOR_DEFAULT);
        if (!hasLanguage) {
          infix.append(SEPARATOR_DEFAULT);
        }
      }
      infix.append(SEPARATOR_DEFAULT);
      infix.append(variant);
    }
    return infix.toString();
  }

  /**
   * This method determines the infix-strings for localization lookup ordered from most specific to least specific
   * (empty string representing {@link Locale#ROOT}). Each infix is defined as:
   *
   * <pre>
   * [_{@link Locale#getLanguage() language}[_{@link Locale#getCountry() country}[_{@link Locale#getVariant() variant}]]]
   * </pre>
   *
   * Please note that if a segment is empty but a following segment is present, multiple underscores ('_') will occur.
   * <br>
   * Examples:
   * <table border="1">
   * <tr>
   * <th>locale</th>
   * <th>result</th>
   * </tr>
   * <tr>
   * <td>{@link Locale#GERMANY}</td>
   * <td><code>{"_de_DE", "_de", ""}</code></td>
   * </tr>
   * <tr>
   * <td><code>new Locale("", "CM")</code></td>
   * <td><code>{"__CM", ""}</code></td>
   * </tr>
   * <tr>
   * <td><code>new Locale("", "", "variant")</code></td>
   * <td><code>{"___variant", ""}</code></td>
   * </tr>
   * </table>
   *
   * @param locale is the {@link Locale}.
   * @return the localization-infixes ordered from most specific to least specific. The returned array will always
   *         contain the empty string as last entry.
   */
  public static String[] toInfixes(Locale locale) {

    String[] infixes;
    int length = 1;
    StringBuilder infix = new StringBuilder();
    String infixLang = null;
    String language = locale.getLanguage();
    infix.append(SEPARATOR_DEFAULT);
    if ((language != null) && (language.length() == 2)) {
      infix.append(language);
      infixLang = infix.toString();
      length++;
    }
    String infixCountry = null;
    String country = locale.getCountry();
    infix.append(SEPARATOR_DEFAULT);
    if ((country != null) && (country.length() == 2)) {
      infix.append(country);
      infixCountry = infix.toString();
      length++;
    }
    String infixVariant = null;
    String variant = locale.getVariant();
    infix.append(SEPARATOR_DEFAULT);
    if ((variant != null) && (variant.length() > 0)) {
      infix.append(variant);
      infixVariant = infix.toString();
      length++;
    }
    infixes = new String[length];
    int i = 0;
    if (infixVariant != null) {
      infixes[i] = infixVariant;
      i++;
    }
    if (infixCountry != null) {
      infixes[i] = infixCountry;
      i++;
    }
    if (infixLang != null) {
      infixes[i] = infixLang;
      i++;
    }
    infixes[i] = "";
    return infixes;
  }

  /**
   * Create a {@link Locale} from a given {@link String} representation such as {@link Locale#toString()} or
   * {@link Locale#forLanguageTag(String)}.
   *
   * @param locale the {@link String} representation of the {@link Locale}. May be an from {@link Locale#toString()},
   *        {@link Locale#toLanguageTag()} or {@link #toInfix(Locale)}.
   * @return the parsed {@link Locale}.
   */
  public static Locale fromString(String locale) {

    if (locale == null) {
      return null;
    }
    if (locale.isEmpty()) {
      return Locale.ROOT;
    }
    int start = 0;
    int end = locale.indexOf(SEPARATOR_DEFAULT);
    if (end == -1) {
      if (locale.charAt(0) == SEPARATOR_FOR_LANGUAGE_TAG) {
        locale = locale.substring(1); // tolerant for accepting suffixes like "-en-US"
      }
      return Locale.forLanguageTag(locale);
    } else if (end == 0) {
      start = end + 1;
      end = locale.indexOf(SEPARATOR_DEFAULT, start);
    }
    Builder builder = new Builder();
    if (end == -1) {
      builder.setLanguage(locale.substring(start));
    } else {
      String language = locale.substring(start, end);
      builder.setLanguage(language);
      start = end + 1;
      end = locale.indexOf(SEPARATOR_DEFAULT, start);
      if (end == -1) {
        builder.setRegion(locale.substring(start));
      } else {
        String region = locale.substring(start, end);
        builder.setRegion(region);
        String variant = fromStringDetails(locale, end + 1, builder);
        if (variant != null) {
          return new Locale.Builder().setLanguage(language).setRegion(region).setVariant(variant).build();
        }
      }
    }
    return builder.build();
  }

  private static String fromStringDetails(String locale, int start, Builder builder) {

    int len = locale.length();
    if (start >= len) {
      return null;
    }
    int end;
    char c = locale.charAt(start);
    if (c != SEPARATOR_SCRIPT) { // variant?
      end = locale.indexOf(SEPARATOR_DEFAULT, start);
      String variant;
      if (end == -1) {
        variant = locale.substring(start);
      } else {
        variant = locale.substring(start, end);
        start = end + 1;
        if (start < len) {
          c = locale.charAt(start);
        }
      }
      try {
        builder.setVariant(variant);
      } catch (Exception e) {
        return variant;
      }
    }
    if (c == SEPARATOR_SCRIPT) { // script/extension?
      start++;
      end = locale.indexOf(SEPARATOR_EXTENSION, start);
      if (end == -1) {
        builder.setScript(locale.substring(start));
      } else if (end > start + 1) {
        builder.setScript(locale.substring(start, end));
        start = end + 1;
      }
      end = findExtensionKey(locale, start);
      while (end != -1) {
        char extensionKey = locale.charAt(end);
        start = end + 2;
        end = findExtensionKey(locale, start);
        String value;
        if (end == -1) {
          value = locale.substring(start);
        } else {
          value = locale.substring(start, end - 1);
        }
        if (!value.isEmpty()) {
          builder.setExtension(extensionKey, value);
        }
      }
    }
    return null;
  }

  private static int findExtensionKey(String locale, int start) {

    while (true) {
      int end = locale.indexOf(SEPARATOR_EXTENSION, start);
      if (end == -1) {
        return -1;
      }
      if (end == start + 1) {
        char extensionKey = locale.charAt(start);
        if (CharFilter.LATIN_LETTER_OR_DIGIT.accept(extensionKey)) {
          return start;
        }
      } else {
        start = end + 1;
      }
    }
  }

}
