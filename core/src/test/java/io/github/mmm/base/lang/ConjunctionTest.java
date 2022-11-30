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
    assertThat(and.getSyntax()).isEqualTo("&");
    assertThat(and.toString()).isEqualTo("and");
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
    assertThat(or.getSyntax()).isEqualTo("|");
    assertThat(or.toString()).isEqualTo("or");
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
    assertThat(nand.getSyntax()).isEqualTo("!&");
    assertThat(nand.toString()).isEqualTo("nand");
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
    assertThat(nor.getSyntax()).isEqualTo("!|");
    assertThat(nor.toString()).isEqualTo("nor");
  }

  /** Test of {@link Conjunction#XOR}. */
  @Test
  public void testXor() {

    Conjunction xor = Conjunction.XOR;
    assertThat(xor.eval(true, true)).isFalse();
    assertThat(xor.eval(true, false)).isTrue();
    assertThat(xor.eval(false, true)).isTrue();
    assertThat(xor.eval(false, false)).isFalse();
    assertThat(xor.eval(false, true, true)).isTrue();
    assertThat(xor.eval(false, true, false)).isTrue();
    assertThat(xor.evalEmpty()).isFalse();
    assertThat(xor.negate()).isSameAs(Conjunction.EQ);
    assertThat(xor.getSyntax()).isEqualTo("!=");
    assertThat(xor.toString()).isEqualTo("xor");
  }

  /** Test of {@link Conjunction#EQ}. */
  @Test
  public void testEq() {

    Conjunction eq = Conjunction.EQ;
    assertThat(eq.eval(true, true)).isTrue();
    assertThat(eq.eval(true, false)).isFalse();
    assertThat(eq.eval(false, true)).isFalse();
    assertThat(eq.eval(false, false)).isTrue();
    assertThat(eq.eval(false, true, true)).isFalse();
    assertThat(eq.eval(false, true, false)).isFalse();
    assertThat(eq.eval(true, true, true)).isTrue();
    assertThat(eq.eval(false, false, false)).isTrue();
    assertThat(eq.evalEmpty()).isTrue();
    assertThat(eq.negate()).isSameAs(Conjunction.XOR);
    assertThat(eq.getSyntax()).isEqualTo("=");
    assertThat(eq.toString()).isEqualTo("eq");
  }

  /**
   * Generic test for all {@link Conjunction}s.
   */
  @Test
  public void testGeneric() {

    for (Conjunction conjunction : Conjunction.values()) {
      assertThat(Conjunction.ofName(conjunction.getName())).isSameAs(conjunction);
      assertThat(Conjunction.ofSyntax(conjunction.getSyntax())).isSameAs(conjunction);
      Conjunction negation = conjunction.negate();
      assertThat(negation).isNotEqualTo(conjunction);
      assertThat(negation.negate()).isSameAs(conjunction);
      assertThat(negation.eval(true, true)).isSameAs(!conjunction.eval(true, true));
      assertThat(negation.eval(true, false)).isSameAs(!conjunction.eval(true, false));
      assertThat(negation.eval(false, true)).isSameAs(!conjunction.eval(false, true));
      assertThat(negation.eval(false, false)).isSameAs(!conjunction.eval(false, false));
      assertThat(negation.eval(true, true, true)).isSameAs(!conjunction.eval(true, true, true));
      assertThat(negation.eval(true, true, false)).isSameAs(!conjunction.eval(true, true, false));
      assertThat(negation.eval(true, false, true)).isSameAs(!conjunction.eval(true, false, true));
      assertThat(negation.eval(true, false, false)).isSameAs(!conjunction.eval(true, false, false));
      assertThat(negation.eval(false, true, true)).isSameAs(!conjunction.eval(false, true, true));
      assertThat(negation.eval(false, true, false)).isSameAs(!conjunction.eval(false, true, false));
      assertThat(negation.eval(false, false, true)).isSameAs(!conjunction.eval(false, false, true));
      assertThat(negation.eval(false, false, false)).isSameAs(!conjunction.eval(false, false, false));
    }
  }

}
