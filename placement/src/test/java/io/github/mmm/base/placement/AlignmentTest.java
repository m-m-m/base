package io.github.mmm.base.placement;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/// Test of [Alignment].
class AlignmentTest extends Assertions {

  /// Test of [Alignment#BOTTOM].
  @Test
  void testBottom() {

    Alignment a = Alignment.BOTTOM;
    assertThat(a.getSyntax()).isEqualTo("s");
    assertThat(a.toString()).isEqualTo("bottom");
    assertThat(a.toDirection()).isEqualTo(Direction.DOWN);
    assertThat(a.toHorizontalAlignment()).isEqualTo(HorizontalAlignment.CENTER);
    assertThat(a.toVerticalAlignment()).isEqualTo(VerticalAlignment.BOTTOM);
  }

}
