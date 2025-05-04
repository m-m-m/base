/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.justification;

import io.github.mmm.base.impl.JustificationImpl;

/**
 * This is the interface for a specific justification.
 *
 * @see #of(String)
 */
public interface Justification {

  /**
   * This method applies the justification to the given {@code value} and {@link Appendable#append(CharSequence)
   * appends} the result to the given {@code target}.
   *
   * @param value is the string to justify.
   * @param target is where to {@link Appendable#append(CharSequence) append} the justified data.
   */
  void justify(CharSequence value, Appendable target);

  /**
   * This method applies the justification to the given {@code value} and returns the result.
   *
   * @param value is the string to justify.
   * @return the justified string.
   */
  default String justify(CharSequence value) {

    StringBuilder sb = new StringBuilder();
    justify(value, sb);
    return sb.toString();
  }

  /**
   * This method parses the given {@code format} as {@link Justification}. It therefore expects the following
   * format:<br>
   * {@code «filler»«alignment»«width»[«mode»]}<br>
   * The segments have the following meaning:
   * <table border="1">
   * <tr>
   * <th>segment</th>
   * <th>pattern</th>
   * <th>meaning</th>
   * </tr>
   * <tr>
   * <td>{@code «filler»}</td>
   * <td>{@code .}</td>
   * <td>character used to fill up with</td>
   * </tr>
   * <tr>
   * <td>{@code «alignment»}</td>
   * <td>{@code [+-~]}</td>
   * <td>align to the right(+), left(-) or centered(~)</td>
   * </tr>
   * <tr>
   * <td>{@code «with»}</td>
   * <td>{@code [0-9]+}</td>
   * <td>if the length of the string to {@link Justification#justify(CharSequence, Appendable) justify} is less than
   * this width, the string will be expanded using the filler according to the alignment.</td>
   * </tr>
   * <tr>
   * <td>{@code «mode»}</td>
   * <td>{@code [|]}</td>
   * <td>if the mode is truncate(|) then the string will be truncated if its length is greater than «with» so the result
   * will always have the length of «with». Please note that truncate can remove valuable information or cause wrong
   * results (e.g. "10000" with a justification of " +3|" will result in "100").</td>
   * </tr>
   * </table>
   *
   * Examples:
   * <table border="1">
   * <tr>
   * <th>value</th>
   * <th>justification</th>
   * <th>result</th>
   * </tr>
   * <tr>
   * <td>{@code 42}</td>
   * <td>{@code 0+4}</td>
   * <td>{@code 0042}</td>
   * </tr>
   * <tr>
   * <td>{@code 42}</td>
   * <td>{@code .-4}</td>
   * <td>{@code 42..}</td>
   * </tr>
   * <tr>
   * <td>{@code 42}</td>
   * <td>{@code _~11}</td>
   * <td>{@code ____42_____}</td>
   * </tr>
   * <tr>
   * <td>{@code Hello World}</td>
   * <td>{@code _+5}</td>
   * <td>{@code Hello World}</td>
   * </tr>
   * <tr>
   * <td>{@code Hello World}</td>
   * <td>{@code _+5|}</td>
   * <td>{@code Hello}</td>
   * </tr>
   * </table>
   *
   * @param format is the format as specified above.
   * @return the parsed {@link Justification}
   */
  public static Justification of(String format) {

    return new JustificationImpl(format);
  }

}
