/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.i18n;

import java.util.Locale;
import java.util.Locale.Builder;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link LocaleHelper}.
 */
public class LocaleHelperTest extends Assertions {

  /** Test of {@link LocaleHelper#toInfix(java.util.Locale)} */
  @Test
  public void testToInfix() {

    assertThat(LocaleHelper.toInfix(Locale.ROOT)).isEmpty();
    assertThat(LocaleHelper.toInfix(Locale.GERMAN)).isEqualTo("_de");
    assertThat(LocaleHelper.toInfix(Locale.GERMANY)).isEqualTo("_de_DE");

    assertThat(LocaleHelper.toInfix(locale("de", "DE", "bayrisch"))).isEqualTo("_de_DE_bayrisch");
    assertThat(LocaleHelper.toInfix(locale("", "FR"))).isEqualTo("__FR");
    assertThat(LocaleHelper.toInfix(locale("", "FR", "Paris"))).isEqualTo("__FR_Paris");
    assertThat(LocaleHelper.toInfix(locale("fr", "", "Paris"))).isEqualTo("_fr__Paris");
    assertThat(LocaleHelper.toInfix(locale("", "", "variant"))).isEqualTo("___variant");
  }

  /** Test of {@link LocaleHelper#toInfixes(java.util.Locale)} */
  @Test
  public void testToInfixes() {

    assertThat(LocaleHelper.toInfixes(Locale.ROOT)).containsExactly("");
    assertThat(LocaleHelper.toInfixes(Locale.GERMAN)).containsExactly("_de", "");
    assertThat(LocaleHelper.toInfixes(Locale.GERMANY)).containsExactly("_de_DE", "_de", "");
    assertThat(LocaleHelper.toInfixes(locale("de", "DE", "bayrisch"))).containsExactly("_de_DE_bayrisch", "_de_DE",
        "_de", "");
    assertThat(LocaleHelper.toInfixes(locale("", "FR"))).containsExactly("__FR", "");
    assertThat(LocaleHelper.toInfixes(locale("", "FR", "Paris"))).containsExactly("__FR_Paris", "__FR", "");
    assertThat(LocaleHelper.toInfixes(locale("fr", "", "Paris"))).containsExactly("_fr__Paris", "_fr", "");
    assertThat(LocaleHelper.toInfixes(locale("", "", "variant"))).containsExactly("___variant", "");
  }

  /** Test of {@link LocaleHelper#fromString(String)} */
  @Test
  public void testFromString() {

    assertThat(LocaleHelper.fromString("")).isEqualTo(Locale.ROOT);
    assertThat(LocaleHelper.fromString("_de")).isEqualTo(Locale.GERMAN);
    assertThat(LocaleHelper.fromString("_de_DE")).isEqualTo(Locale.GERMANY);
    assertThat(LocaleHelper.fromString("_de_DE_bayrisch")).isEqualTo(locale("de", "DE", "bayrisch"));
    assertThat(LocaleHelper.fromString("__FR")).isEqualTo(locale("", "FR"));
    assertThat(LocaleHelper.fromString("__FR_Paris")).isEqualTo(locale("", "FR", "Paris"));
    assertThat(LocaleHelper.fromString("_fr__Paris")).isEqualTo(locale("fr", "", "Paris"));
    assertThat(LocaleHelper.fromString("___variant")).isEqualTo(locale("", "", "variant"));
    Locale locale = LocaleHelper.fromString("zh_CN_#Hans");
    assertThat(locale.getLanguage()).isEqualTo("zh");
    assertThat(locale.getCountry()).isEqualTo("CN");
    assertThat(locale.getScript()).isEqualTo("Hans");
    assertThat(locale.getExtensionKeys()).isEmpty();
    locale = LocaleHelper.fromString("zh_TW_#Hant-x-java");
    assertThat(locale.getLanguage()).isEqualTo("zh");
    assertThat(locale.getCountry()).isEqualTo("TW");
    assertThat(locale.getScript()).isEqualTo("Hant");
    assertThat(locale.getExtensionKeys()).containsExactly(Character.valueOf('x'));
    assertThat(locale.getExtension('x')).isEqualTo("java");
    // example "th_TH_TH_#u-nu-thai" from official Locale.toString() JavaDoc is actually invalid...
    locale = LocaleHelper.fromString("th_TH_Siamese_#u-nu-thai");
    assertThat(locale.getLanguage()).isEqualTo("th");
    assertThat(locale.getCountry()).isEqualTo("TH");
    assertThat(locale.getVariant()).isEqualTo("Siamese");
    assertThat(locale.getScript()).isEmpty();
    assertThat(locale.getExtensionKeys()).containsExactly(Character.valueOf('u'));
    assertThat(locale.getExtension('u')).isEqualTo("nu-thai");
    // tolerance test...
    assertThat(LocaleHelper.fromString("th_TH_variant")).isEqualTo(locale("th", "TH", "variant"));
    // test language tag
    assertThat(LocaleHelper.fromString("de-POSIX-x-URP-lvariant-Abc-Def"))
        .isEqualTo(Locale.forLanguageTag("de-POSIX-x-URP-lvariant-Abc-Def"));
  }

  private static Locale locale(String language, String country) {

    return locale(language, country, null);
  }

  private static Locale locale(String language, String country, String variant) {

    Builder builder = new Locale.Builder().setLanguage(language).setRegion(country);
    if (variant != null) {
      builder = builder.setVariant(variant);
    }
    return builder.build();
  }
}
