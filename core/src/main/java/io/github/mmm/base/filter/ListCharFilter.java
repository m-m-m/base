/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.filter;

/// Implementation of the [CharFilter] that [accepts][#accept(int)] characters from a white-list given at
/// [construction][ListCharFilter#ListCharFilter(String)].
public class ListCharFilter extends AbstractCharFilter {

  private final String chars;

  /// The constructor.
  ///
  /// @param chars are the chars to accept. Have to be unique and should be ordered.
  public ListCharFilter(String chars) {

    super(null);
    this.chars = chars;
    assert (isUnique()) : chars;
  }

  private boolean isUnique() {

    int i = 0;
    int length = this.chars.length();
    while (i < length) {
      char c = this.chars.charAt(i);
      if (this.chars.indexOf(c) != i) {
        return false;
      } else if (this.chars.lastIndexOf(c) != i) {
        return false;
      }
      i++;
    }
    return true;
  }

  @Override
  public boolean accept(int codePoint) {

    int i = this.chars.indexOf(codePoint);
    return (i >= 0);
  }

  /// @param characters the additional characters to [accept][#accept(int)].
  /// @return a new [ListCharFilter] that [accepts][#accept(int)] both the characters from `this`
  ///         [CharFilter] as well as the given `characters`.
  public ListCharFilter join(char... characters) {

    if ((characters == null) || (characters.length == 0)) {
      return this;
    }
    int length = this.chars.length();
    char[] composed = new char[length + characters.length];
    this.chars.getChars(0, length, composed, 0);
    int i = length;
    for (char c : characters) {
      if (this.chars.indexOf(c) < 0) {
        composed[i++] = c;
      }
    }
    if (i == length) {
      return this;
    }
    return new ListCharFilter(new String(composed, 0, i));
  }

  /// @param filter the additional [ListCharFilter] to [join][#join(char...)].
  /// @return a new [ListCharFilter] that [accepts][#accept(int)] both the characters from `this` as
  ///         well as the given [CharFilter].
  public ListCharFilter join(ListCharFilter filter) {

    return join(filter.chars.toCharArray());
  }

  @Override
  public CharFilter compose(CharFilter filter) {

    if (filter instanceof ListCharFilter) {
      return join((ListCharFilter) filter);
    }
    return super.compose(filter);
  }

  /// @return a [String] composed of all [accepted][#accept(int)] characters.
  public String getChars() {

    return this.chars;
  }

  @Override
  protected String computeDescription() {

    int length = this.chars.length();
    StringBuilder sb = new StringBuilder(length + 4);
    sb.append('{');
    for (int i = 0; i < length; i++) {
      char c = this.chars.charAt(i);
      CharFilter.append(c, sb);
    }
    sb.append('}');
    return sb.toString();
  }

}
