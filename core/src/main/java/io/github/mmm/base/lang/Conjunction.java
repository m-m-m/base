/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.lang;

/**
 * {@link Enum} with the common boolean conjunction operators {@link #AND}, {@link #OR}, {@link #NAND}, and
 * {@link #NOR}.
 *
 * @since 1.0.0
 */
public enum Conjunction {
  /**
   * This conjunction is {@code true} if and only if all arguments are {@code true}.
   */
  AND("and", "&&") {

    @Override
    public boolean evalEmpty() {

      return true;
    }

    @Override
    public Boolean evalSingle(boolean argument) {

      if (!argument) {
        return Boolean.FALSE;
      }
      return null;
    }

    @Override
    public Conjunction negate() {

      return NAND;
    }
  },

  /**
   * This conjunction is {@code true} if and only if at least one argument is {@code true}.
   */
  OR("or", "||") {

    @Override
    public boolean evalEmpty() {

      return false;
    }

    @Override
    public Boolean evalSingle(boolean argument) {

      if (argument) {
        return Boolean.TRUE;
      }
      return null;
    }

    @Override
    public Conjunction negate() {

      return NOR;
    }
  },

  /**
   * This is the negation of {@link #AND}. It is only {@code true} if at least one argument is {@code false}.
   */
  NAND("nand", "!&") {

    @Override
    public boolean evalEmpty() {

      return false;
    }

    @Override
    public Boolean evalSingle(boolean argument) {

      if (!argument) {
        return Boolean.TRUE;
      }
      return null;
    }

    @Override
    public Conjunction negate() {

      return AND;
    }
  },

  /**
   * This is the negation of {@link #OR}. It is only {@code true} if all arguments are {@code false} .
   */
  NOR("nor", "!|") {

    @Override
    public boolean evalEmpty() {

      return true;
    }

    @Override
    public Boolean evalSingle(boolean argument) {

      if (argument) {
        return Boolean.FALSE;
      }
      return null;
    }

    @Override
    public Conjunction negate() {

      return OR;
    }
  },

  /**
   * This conjunction is {@code true} if and only if two arguments differ (exactly one of two arguments if
   * {@code true}).
   */
  XOR("xor", "!=") {

    @Override
    public boolean evalEmpty() {

      return false;
    }

    @Override
    public Boolean evalSingle(boolean argument) {

      return null;
    }

    @Override
    public boolean eval(boolean... arguments) {

      boolean result = false;
      boolean first = true;
      for (boolean b : arguments) {
        if (first) {
          result = b;
          first = false;
        } else {
          result = (result != b);
        }
      }
      return result;
    }

    @Override
    public Conjunction negate() {

      return XOR;
    }
  };

  private final String syntax;

  private final String name;

  /**
   * The constructor.
   *
   * @param name is the {@link #getName() name}.
   * @param syntax is the {@link #getSyntax() syntax}.
   */
  private Conjunction(String name, String syntax) {

    this.syntax = syntax;
    this.name = name;
  }

  /**
   * This method evaluates this conjunction for the given boolean {@code arguments}.
   *
   * @param arguments are the boolean values to evaluate.
   * @return the result of this conjunction applied to the given {@code arguments}.
   */
  public boolean eval(boolean... arguments) {

    for (boolean b : arguments) {
      Boolean single = evalSingle(b);
      if (single != null) {
        return single.booleanValue();
      }
    }
    return evalEmpty();
  }

  /**
   * @param argument is a literal boolean argument.
   * @return {@link Boolean#TRUE} if {@link #eval(boolean...)} will return {@code true} if any of the given arguments
   *         has the value of the given {@code argument}, {@link Boolean#FALSE} if {@link #eval(boolean...)} will return
   *         {@code false} if any of the given arguments has the value of the given {@code argument}, {@code null}
   *         otherwise.
   */
  public abstract Boolean evalSingle(boolean argument);

  /**
   * @return the result of {@link #eval(boolean...)} for no argument (an empty argument array).
   */
  public abstract boolean evalEmpty();

  /**
   * @return the syntax used for technical representation (e.g. '{@literal &&}' for {@link #AND}).
   */
  public String getSyntax() {

    return this.syntax;
  }

  /**
   * @return the name and {@link Object#toString() string representation} (e.g. "and" for {@link #AND}).
   */
  public String getName() {

    return this.name;
  }

  /**
   * @return the negation of this {@link Conjunction} that {@link #eval(boolean...) evaluates} to the negated result.
   */
  public abstract Conjunction negate();

  @Override
  public String toString() {

    return this.name;
  }

  /**
   * @param symbol is the {@link #getSyntax() symbol} of the requested {@link Conjunction}.
   * @return the requested {@link Conjunction} or {@code null} if no such {@link Conjunction} exists.
   */
  public static Conjunction ofSymbol(String symbol) {

    for (Conjunction conjunction : values()) {
      if (conjunction.syntax.equals(symbol)) {
        return conjunction;
      }
    }
    return null;
  }

  /**
   * @param name is the {@link #getName() name} of the requested {@link Conjunction}.
   * @return the requested {@link Conjunction} or {@code null} if no such {@link Conjunction} exists.
   */
  public static Conjunction ofName(String name) {

    for (Conjunction conjunction : values()) {
      if (conjunction.name.equals(name)) {
        return conjunction;
      }
    }
    return null;
  }
}
