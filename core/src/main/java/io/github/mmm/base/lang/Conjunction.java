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
  AND("and", "&") {

    @Override
    public boolean eval(boolean arg1, boolean arg2) {

      return arg1 && arg2;
    }

    @Override
    public boolean evalEmpty() {

      return true;
    }

    @Override
    public boolean isNegated() {

      return false;
    }

    @Override
    public Conjunction negate() {

      return NAND;
    }
  },

  /**
   * This conjunction is {@code true} if and only if at least one argument is {@code true}.
   */
  OR("or", "|") {

    @Override
    public boolean eval(boolean arg1, boolean arg2) {

      return arg1 || arg2;
    }

    @Override
    public boolean evalEmpty() {

      return false;
    }

    @Override
    public boolean isNegated() {

      return false;
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
    public boolean eval(boolean arg1, boolean arg2) {

      return !(arg1 && arg2);
    }

    @Override
    public boolean evalEmpty() {

      return false;
    }

    @Override
    public boolean isNegated() {

      return true;
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
    public boolean eval(boolean arg1, boolean arg2) {

      return !(arg1 || arg2);
    }

    @Override
    public boolean evalEmpty() {

      return true;
    }

    @Override
    public boolean isNegated() {

      return true;
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
    public boolean eval(boolean arg1, boolean arg2) {

      return arg1 ^ arg2;
    }

    @Override
    public boolean evalEmpty() {

      return false;
    }

    @Override
    public boolean isNegated() {

      return true;
    }

    @Override
    public Conjunction negate() {

      return EQ;
    }
  },

  /**
   * This conjunction is {@code true} if and only if two arguments equal.
   */
  EQ("eq", "=") {

    @Override
    public boolean eval(boolean arg1, boolean arg2) {

      return arg1 == arg2;
    }

    @Override
    public boolean eval(boolean... arguments) {

      if (arguments.length == 0) {
        return evalEmpty();
      }
      int i = 0;
      boolean first = arguments[i++];
      while (i < arguments.length) {
        boolean next = arguments[i++];
        if (first != next) {
          return false;
        }
      }
      return true;
    }

    @Override
    public boolean evalEmpty() {

      return true;
    }

    @Override
    public boolean isNegated() {

      return false;
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
   * @param arg1 the first argument.
   * @param arg2 the second argument.
   * @return the result of this conjunction applied to the given two arguments.
   */
  public abstract boolean eval(boolean arg1, boolean arg2);

  /**
   * @return {@code true} if this {@link Conjunction} is {@link #negate() negated} and therefore not left-associative,
   *         {@code false} otherwise (normal form).
   */
  public abstract boolean isNegated();

  /**
   * Evaluates this {@link Conjunction} for the given boolean {@code arguments}. <br>
   * <b>ATTENTION:</b> The result is NOT the same as a applying the {@link Conjunction} left-associative as
   * {@link #eval(boolean, boolean) binary} operation because a {@link #negate() negation} is applied to the final
   * result. Example: {@code NOR(a, b, c) = !(OR(a, b, c))} what is not the same as {@code NOR(NOR(a, b), c)}.
   *
   * @param arguments the boolean values to evaluate.
   * @return the result of this {@link Conjunction} applied to the given {@code arguments}.
   */
  public boolean eval(boolean... arguments) {

    if (isNegated()) {
      return !negate().eval(arguments);
    }
    if (arguments.length == 0) {
      return evalEmpty();
    }
    int i = 0;
    boolean result = arguments[i++];
    while (i < arguments.length) {
      result = eval(result, arguments[i++]);
    }
    return result;
  }

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
   * @param syntax the {@link #getSyntax() syntax} of the requested {@link Conjunction}.
   * @return the requested {@link Conjunction} or {@code null} if no such {@link Conjunction} exists.
   */
  public static Conjunction ofSyntax(String syntax) {

    for (Conjunction conjunction : values()) {
      if (conjunction.syntax.equals(syntax)) {
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
