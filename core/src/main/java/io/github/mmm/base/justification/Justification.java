/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.justification;

import io.github.mmm.base.impl.JustificationImpl;

/// This is the interface for a specific justification.
///
/// @see #of(String)
public interface Justification {

  /// This method applies the justification to the given `value` and [appends][Appendable#append(CharSequence)] the result to the given `target`.
  ///
  /// @param value is the string to justify.
  /// @param target is where to [append][Appendable#append(CharSequence)] the justified data.
  void justify(CharSequence value, Appendable target);

  /// This method applies the justification to the given `value` and returns the result.
  ///
  /// @param value is the string to justify.
  /// @return the justified string.
  default String justify(CharSequence value) {

    StringBuilder sb = new StringBuilder();
    justify(value, sb);
    return sb.toString();
  }

  /// This method parses the given `format` as [Justification]. It therefore expects the following
  /// format:
  /// `«filler»«alignment»«width»[«mode»]`
  /// The segments have the following meaning:
  /// <table border="1">
  /// <tr>
  /// <th>segment</th>
  /// <th>pattern</th>
  /// <th>meaning</th>
  /// </tr>
  /// <tr>
  /// <td>`«filler»`</td>
  /// <td>`.`</td>
  /// <td>character used to fill up with</td>
  /// </tr>
  /// <tr>
  /// <td>`«alignment»`</td>
  /// <td>`[+-~]`</td>
  /// <td>align to the right(+), left(-) or centered(~)</td>
  /// </tr>
  /// <tr>
  /// <td>`«with»`</td>
  /// <td>`[0-9]+`</td>
  /// <td>if the length of the string to [justify][Justification#justify(CharSequence, Appendable)] is less than
  /// this width, the string will be expanded using the filler according to the alignment.</td>
  /// </tr>
  /// <tr>
  /// <td>`«mode»`</td>
  /// <td>`[|]`</td>
  /// <td>if the mode is truncate(|) then the string will be truncated if its length is greater than «with» so the result
  /// will always have the length of «with». Please note that truncate can remove valuable information or cause wrong
  /// results (e.g. "10000" with a justification of " +3|" will result in "100").</td>
  /// </tr>
  /// </table>
  ///
  /// Examples:
  /// <table border="1">
  /// <tr>
  /// <th>value</th>
  /// <th>justification</th>
  /// <th>result</th>
  /// </tr>
  /// <tr>
  /// <td>`42`</td>
  /// <td>`0+4`</td>
  /// <td>`0042`</td>
  /// </tr>
  /// <tr>
  /// <td>`42`</td>
  /// <td>`.-4`</td>
  /// <td>`42..`</td>
  /// </tr>
  /// <tr>
  /// <td>`42`</td>
  /// <td>`_~11`</td>
  /// <td>`____42_____`</td>
  /// </tr>
  /// <tr>
  /// <td>`Hello World`</td>
  /// <td>`_+5`</td>
  /// <td>`Hello World`</td>
  /// </tr>
  /// <tr>
  /// <td>`Hello World`</td>
  /// <td>`_+5|`</td>
  /// <td>`Hello`</td>
  /// </tr>
  /// </table>
  ///
  /// @param format is the format as specified above.
  /// @return the parsed [Justification]
  public static Justification of(String format) {

    return new JustificationImpl(format);
  }

}
