/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.metainfo.impl;

import java.util.Objects;

/**
 * Simple class for a {@link io.github.mmm.base.metainfo.MetaInfos#value() meta-info value} as {@link #getKey()
 * key}-{@link #getValue() value} pair. Further, it implements a linked list
 *
 * @since 1.0.0
 */
public final class MetaInfoValue {

  final String key;

  final String value;

  final MetaInfoValue next;

  /**
   * The constructor.
   *
   * @param key the {@link #getKey() key}.
   * @param value the {#getValue() value}.
   */
  public MetaInfoValue(String key, String value) {

    this(key, value, null);
  }

  /**
   * The constructor.
   *
   * @param key the {@link #getKey() key}.
   * @param value the {#getValue() value}.
   * @param next the next {@link MetaInfoValue}.
   */
  public MetaInfoValue(String key, String value, MetaInfoValue next) {

    super();
    Objects.requireNonNull(key);
    Objects.requireNonNull(value);
    this.key = key;
    this.value = value;
    this.next = next;
  }

  /**
   * @return the key.
   * @see java.util.Map.Entry#getKey()
   */
  public String getKey() {

    return this.key;
  }

  /**
   * @return the value.
   * @see java.util.Map.Entry#getValue()
   */
  public String getValue() {

    return this.value;
  }

  /**
   * @return the link to the next {@link MetaInfoValue} or {@code null} if this is the last element of the list.
   */
  public MetaInfoValue getNext() {

    return this.next;
  }

  @Override
  public String toString() {

    return this.key + "=" + this.value;
  }

  /**
   * @param keyValue the key-value pair. See {@link io.github.mmm.base.metainfo.MetaInfos#value()}.
   * @param next the {@link #getNext() next} {@link MetaInfoValue}.
   * @return the new {@link MetaInfoValue} with the parsed {@link #getKey() key} + {@link #getValue() value} and linked
   *         to {@code next}.
   */
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
