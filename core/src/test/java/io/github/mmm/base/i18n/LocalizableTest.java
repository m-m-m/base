package io.github.mmm.base.i18n;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link Localizable}.
 */
public class LocalizableTest extends Assertions {

  /** Test of {@link Localizable#ofStatic(String)}. */
  @Test
  public void testOfStatic() {

    Localizable nonNls = Localizable.ofStatic("message");
    assertThat(nonNls.getMessage()).isEqualTo("message");
    assertThat(nonNls.getLocalizedMessage()).isEqualTo("message");
    assertThat(nonNls.getLocalizedMessage(Locale.ROOT)).isEqualTo("message");
    assertThat(nonNls.getLocalizedMessage(null)).isEqualTo("message");
    Appendable appendable = new StringBuilder();
    nonNls.getLocalizedMessage(Locale.ROOT, appendable);
    assertThat(appendable.toString()).isEqualTo("message");
    assertThat(nonNls.getArgument("argument")).isNull();
    assertThat(nonNls.toLocalizable()).isSameAs(nonNls);
  }

  /** Test of {@link Localizable#createBundleName(Class)}. */
  @Test
  public void testOf() {

    assertThat(Localizable.createBundleName(Localizable.class)).isEqualTo("l10n.io.github.mmm.base.i18n.Localizable");
  }

}
