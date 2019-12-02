/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.lang;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link Conjunction}.
 */
public class ConjunctionTest extends Assertions {

  /** Test of {@link Conjunction#AND}. */
  @Test
  public void testAnd() {

    Conjunction and = Conjunction.AND;
    assertThat(and.eval(true, true)).isTrue();
    assertThat(and.eval(true, false)).isFalse();
    assertThat(and.eval(false, true)).isFalse();
    assertThat(and.eval(false, false)).isFalse();
    assertThat(and.eval(true, true, false)).isFalse();
    assertThat(and.eval(true, true, true)).isTrue();
    assertThat(and.evalEmpty()).isTrue();
    assertThat(and.negate()).isSameAs(Conjunction.NAND);
  }

  /** Test of {@link Conjunction#OR}. */
  @Test
  public void testOr() {

    Conjunction or = Conjunction.OR;
    assertThat(or.eval(true, true)).isTrue();
    assertThat(or.eval(true, false)).isTrue();
    assertThat(or.eval(false, true)).isTrue();
    assertThat(or.eval(false, false)).isFalse();
    assertThat(or.eval(false, false, false)).isFalse();
    assertThat(or.eval(false, false, true)).isTrue();
    assertThat(or.evalEmpty()).isFalse();
    assertThat(or.negate()).isSameAs(Conjunction.NOR);
  }

  /** Test of {@link Conjunction#NAND}. */
  @Test
  public void testNand() {

    Conjunction nand = Conjunction.NAND;
    assertThat(nand.eval(true, true)).isFalse();
    assertThat(nand.eval(true, false)).isTrue();
    assertThat(nand.eval(false, true)).isTrue();
    assertThat(nand.eval(false, false)).isTrue();
    assertThat(nand.eval(true, true, true)).isFalse();
    assertThat(nand.eval(true, true, false)).isTrue();
    assertThat(nand.evalEmpty()).isFalse();
    assertThat(nand.negate()).isSameAs(Conjunction.AND);
  }

  /** Test of {@link Conjunction#NOR}. */
  @Test
  public void testNor() {

    Conjunction nor = Conjunction.NOR;
    assertThat(nor.eval(true, true)).isFalse();
    assertThat(nor.eval(true, false)).isFalse();
    assertThat(nor.eval(false, true)).isFalse();
    assertThat(nor.eval(false, false)).isTrue();
    assertThat(nor.eval(false, false, true)).isFalse();
    assertThat(nor.eval(false, false, false)).isTrue();
    assertThat(nor.evalEmpty()).isTrue();
    assertThat(nor.negate()).isSameAs(Conjunction.OR);
  }

  /** Test of {@link Conjunction#XOR}. */
  @Test
  public void testXor() {

    Conjunction xor = Conjunction.XOR;
    assertThat(xor.eval(true, true)).isFalse();
    assertThat(xor.eval(true, false)).isTrue();
    assertThat(xor.eval(false, true)).isTrue();
    assertThat(xor.eval(false, false)).isFalse();
    assertThat(xor.eval(false, true, true)).isFalse();
    assertThat(xor.eval(false, true, false)).isTrue();
    assertThat(xor.evalEmpty()).isFalse();
    assertThat(xor.negate()).isSameAs(xor);
  }

}
