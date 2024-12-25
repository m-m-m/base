/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link ListCharFilter}.
 */
public class ListCharFilterTest extends Assertions {

  /** Test of {@link ListCharFilter}. */
  @Test
  public void test() {

    ListCharFilter filter1 = new ListCharFilter(" \r\t");
    assertThat(filter1.accept(' ')).isTrue();
    assertThat(filter1.accept('\r')).isTrue();
    assertThat(filter1.accept('\t')).isTrue();
    assertThat(filter1.accept('!')).isFalse();
    assertThat(filter1.getChars()).isEqualTo(" \r\t");
    assertThat(filter1.join(filter1)).isSameAs(filter1);
    assertThat(filter1.join(filter1.getChars().toCharArray())).isSameAs(filter1);
    assertThat(filter1.compose(filter1)).isSameAs(filter1);
    assertThat(filter1).hasToString("{ \\r\\t}").hasToString(filter1.getDescription());
    char nonBreakingSpace = '\u00A0';
    ListCharFilter filter2 = new ListCharFilter(" " + nonBreakingSpace);
    assertThat(filter2.accept(' ')).isTrue();
    assertThat(filter2.accept(nonBreakingSpace)).isTrue();
    assertThat(filter2.accept('\r')).isFalse();
    assertThat(filter2.accept('\t')).isFalse();
    assertThat(filter2.getChars()).isEqualTo(" " + nonBreakingSpace);
    ListCharFilter filter3 = filter1.join(filter2);
    assertThat(filter3.accept(' ')).isTrue();
    assertThat(filter3.accept(nonBreakingSpace)).isTrue();
    assertThat(filter3.accept('\r')).isTrue();
    assertThat(filter3.accept('\t')).isTrue();
    assertThat(filter3.accept('!')).isFalse();
    assertThat(filter3.getChars()).isEqualTo(" \r\t" + nonBreakingSpace);
    ListCharFilter filter4 = filter1.join(filter2.getChars().toCharArray());
    assertThat(filter4.getChars()).isEqualTo(filter3.getChars());
    assertThat(filter4).hasToString("{ \\r\\t\\u00a0}").hasToString(filter3.getDescription());
  }

}
