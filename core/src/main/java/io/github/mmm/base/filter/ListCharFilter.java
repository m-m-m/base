/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

import java.util.Arrays;

/**
 * Implementation of the {@link CharFilter} that {@link #accept(char) accepts} characters from a whitelist given at
 * {@link ListCharFilter#ListCharFilter(char...) construction}.
 */
public class ListCharFilter implements CharFilter {

  private final char[] chars;

  /**
   * The constructor.
   *
   * @param charArray are the chars to accept.
   */
  public ListCharFilter(char... charArray) {

    super();
    this.chars = charArray;
  }

  @Override
  public boolean accept(char c) {

    for (char currentChar : this.chars) {
      if (c == currentChar) {
        return true;
      }
    }
    return false;
  }

  /**
   * @param characters the additional characters to {@link #accept(char) accept}.
   * @return a new {@link ListCharFilter} that {@link #accept(char) accepts} both the characters from {@code this}
   *         {@link CharFilter} as well as the given {@code characters}.
   */
  public ListCharFilter join(char... characters) {

    if ((characters == null) || (characters.length == 0)) {
      return this;
    }
    char[] composed = new char[this.chars.length + this.chars.length];
    System.arraycopy(this.chars, 0, composed, 0, this.chars.length);
    int i = this.chars.length;
    for (char c : characters) {
      if (!accept(c)) {
        composed[i++] = c;
      }
    }
    if (i < composed.length) {
      if (i == this.chars.length) {
        return this;
      } else {
        composed = Arrays.copyOf(composed, i);
      }
    }
    return new ListCharFilter(composed);
  }

  /**
   * @param filter the additional {@link ListCharFilter} to {@link #join(char...) join}.
   * @return a new {@link ListCharFilter} that {@link #accept(char) accepts} both the characters from {@code this} as
   *         well as the given {@link CharFilter}.
   */
  public ListCharFilter join(ListCharFilter filter) {

    return join(filter.chars);
  }

  @Override
  public CharFilter compose(CharFilter filter) {

    if (filter instanceof ListCharFilter) {
      return join((ListCharFilter) filter);
    }
    return CharFilter.super.compose(filter);
  }

}
