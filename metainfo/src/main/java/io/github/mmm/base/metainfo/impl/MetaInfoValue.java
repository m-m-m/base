/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Objects;

/// Simple class for a [meta-info value][io.github.mmm.base.metainfo.MetaInfos#value()] as [key][#getKey()]-[value][#getValue()] pair. Further, it implements a linked list
///
/// @since 1.0.0
public final class MetaInfoValue {

  final String key;

  final String value;

  final MetaInfoValue next;

  /// The constructor.
  ///
  /// @param key the [key][#getKey()].
  /// @param value the {#getValue() value}.
  public MetaInfoValue(String key, String value) {

    this(key, value, null);
  }

  /// The constructor.
  ///
  /// @param key the [key][#getKey()].
  /// @param value the {#getValue() value}.
  /// @param next the next [MetaInfoValue].
  public MetaInfoValue(String key, String value, MetaInfoValue next) {

    super();
    Objects.requireNonNull(key);
    Objects.requireNonNull(value);
    this.key = key;
    this.value = value;
    this.next = next;
  }

  /// @return the key.
  /// @see java.util.Map.Entry#getKey()
  public String getKey() {

    return this.key;
  }

  /// @return the value.
  /// @see java.util.Map.Entry#getValue()
  public String getValue() {

    return this.value;
  }

  /// @return the link to the next [MetaInfoValue] or `null` if this is the last element of the list.
  public MetaInfoValue getNext() {

    return this.next;
  }

  @Override
  public String toString() {

    return this.key + "=" + this.value;
  }

  /// @param keyValue the key-value pair. See [io.github.mmm.base.metainfo.MetaInfos#value()].
  /// @param next the [next][#getNext()] [MetaInfoValue].
  /// @return the new [MetaInfoValue] with the parsed [key][#getKey()] + [value][#getValue()] and linked
  ///         to `next`.
  public static MetaInfoValue of(String keyValue, MetaInfoValue next) {

    int i = keyValue.indexOf('=');
    if (i < 0) {
      throw new IllegalArgumentException(
          "Invalid MetaInfo '" + keyValue + "' - has to be in the form '«key»=«value»'!");
    }
    String key = keyValue.substring(0, i);
    String value = keyValue.substring(i + 1);
    return new MetaInfoValue(key, value, next);
  }

}
