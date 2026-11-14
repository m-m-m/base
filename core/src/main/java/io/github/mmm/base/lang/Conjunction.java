/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.lang;

/// [Enum] with the common boolean conjunction operators [#AND], [#OR], [#NAND], and
/// [#NOR].
///
/// @since 1.0.0
public enum Conjunction {
  /// This conjunction is `true` if and only if all arguments are `true`.
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

  /// This conjunction is `true` if and only if at least one argument is `true`.
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

  /// This is the negation of [#AND]. It is only `true` if at least one argument is `false`.
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

  /// This is the negation of [#OR]. It is only `true` if all arguments are `false` .
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

  /// This conjunction is `true` if and only if two arguments differ (exactly one of two arguments if
  /// `true`).
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

  /// This conjunction is `true` if and only if two arguments equal.
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

  /// The constructor.
  ///
  /// @param name is the [name][#getName()].
  /// @param syntax is the [syntax][#getSyntax()].
  private Conjunction(String name, String syntax) {

    this.syntax = syntax;
    this.name = name;
  }

  /// This method evaluates this conjunction for the given boolean `arguments`.
  ///
  /// @param arg1 the first argument.
  /// @param arg2 the second argument.
  /// @return the result of this conjunction applied to the given two arguments.
  public abstract boolean eval(boolean arg1, boolean arg2);

  /// @return `true` if this [Conjunction] is [negated][#negate()] and therefore not left-associative,
  ///         `false` otherwise (normal form).
  public abstract boolean isNegated();

  /// Evaluates this [Conjunction] for the given boolean `arguments`.
  /// **ATTENTION:** The result is NOT the same as a applying the [Conjunction] left-associative as
  /// [binary][#eval(boolean, boolean)] operation because a [negation][#negate()] is applied to the final
  /// result. Example: `NOR(a, b, c) = !(OR(a, b, c))` what is not the same as `NOR(NOR(a, b), c)`.
  ///
  /// @param arguments the boolean values to evaluate.
  /// @return the result of this [Conjunction] applied to the given `arguments`.
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

  /// @return the result of [#eval(boolean...)] for no argument (an empty argument array).
  public abstract boolean evalEmpty();

  /// @return the syntax used for technical representation (e.g. '`&&`' for [#AND]).
  public String getSyntax() {

    return this.syntax;
  }

  /// @return the name and [string representation][Object#toString()] (e.g. "and" for [#AND]).
  public String getName() {

    return this.name;
  }

  /// @return the negation of this [Conjunction] that [evaluates][#eval(boolean...)] to the negated result.
  public abstract Conjunction negate();

  @Override
  public String toString() {

    return this.name;
  }

  /// @param syntax the [syntax][#getSyntax()] of the requested [Conjunction].
  /// @return the requested [Conjunction] or `null` if no such [Conjunction] exists.
  public static Conjunction ofSyntax(String syntax) {

    for (Conjunction conjunction : values()) {
      if (conjunction.syntax.equals(syntax)) {
        return conjunction;
      }
    }
    return null;
  }

  /// @param name is the [name][#getName()] of the requested [Conjunction].
  /// @return the requested [Conjunction] or `null` if no such [Conjunction] exists.
  public static Conjunction ofName(String name) {

    for (Conjunction conjunction : values()) {
      if (conjunction.name.equals(name)) {
        return conjunction;
      }
    }
    return null;
  }
}
