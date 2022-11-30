package io.github.mmm.base.placement;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link Alignment}.
 */
public class AlignmentTest extends Assertions {

  /** Test of {@link Alignment#BOTTOM}. */
  @Test
  public void testBottom() {

    Alignment a = Alignment.BOTTOM;
    assertThat(a.getSyntax()).isEqualTo("s");
    assertThat(a.toString()).isEqualTo("bottom");
    assertThat(a.toDirection()).isEqualTo(Direction.DOWN);
    assertThat(a.toHorizontalAlignment()).isEqualTo(HorizontalAlignment.CENTER);
    assertThat(a.toVerticalAlignment()).isEqualTo(VerticalAlignment.BOTTOM);
  }

}
