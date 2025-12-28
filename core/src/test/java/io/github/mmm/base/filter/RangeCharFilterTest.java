/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link RangeCharFilter}.
 *
 * @since 1.0.0
 */
class RangeCharFilterTest extends Assertions {

  /** Test of {@link RangeCharFilter}. */
  @Test
  void test() {

    RangeCharFilter c_f = new RangeCharFilter('c', 'f');
    assertThat(c_f).hasToString("[c-f]").hasToString(c_f.getDescription());
    assertThat(c_f.filter("0a1b2c3d4e5f6g7h8i9j")).isEqualTo("cdef");
    RangeCharFilter a_a = new RangeCharFilter('a', 'a');
    assertThat(a_a).hasToString("[a-a]").hasToString(a_a.getDescription());
    assertThat(a_a.filter("0a1b2c3d4e5f6g7h8i9j")).isEqualTo("a");
    RangeCharFilter a_b = new RangeCharFilter('a', 'b');
    assertThat(a_b).hasToString("[a-b]").hasToString(a_b.getDescription());
    assertThat(a_b.filter("0a1b2c3d4e5f6g7h8i9j")).isEqualTo("ab");
    RangeCharFilter d_e = new RangeCharFilter('d', 'e');
    assertThat(d_e).hasToString("[d-e]").hasToString(d_e.getDescription());
    assertThat(d_e.filter("0a1b2c3d4e5f6g7h8i9j")).isEqualTo("de");
    RangeCharFilter f_h = new RangeCharFilter('f', 'h');
    assertThat(f_h).hasToString("[f-h]").hasToString(f_h.getDescription());
    assertThat(f_h.filter("0a1b2c3d4e5f6g7h8i9j")).isEqualTo("fgh");
    RangeCharFilter i_j = new RangeCharFilter('i', 'j');
    assertThat(i_j).hasToString("[i-j]").hasToString(i_j.getDescription());
    assertThat(i_j.filter("0a1b2c3d4e5f6g7h8i9j")).isEqualTo("ij");
    // test compose
    assertThat(c_f.compose(d_e)).isSameAs(c_f);
    assertThat(d_e.compose(c_f)).isSameAs(c_f);
    assertThat(a_a.compose(a_b)).isSameAs(a_b);
    assertThat(a_b.compose(a_a)).isSameAs(a_b);
    assertThat(c_f.compose(a_b)).isInstanceOf(RangeCharFilter.class).hasToString("[a-f]");
    assertThat(a_a.compose(c_f)).isInstanceOf(ComposedCharFilter.class).hasToString("[a-a][c-f]");
    assertThat(c_f.compose(f_h)).isInstanceOf(RangeCharFilter.class).hasToString("[c-h]");
    assertThat(c_f.compose(i_j)).isInstanceOf(ComposedCharFilter.class).hasToString("[c-f][i-j]");
  }

}
