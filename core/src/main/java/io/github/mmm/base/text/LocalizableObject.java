/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.text;

/**
 * This is the interface for a {@link Localizable} object for native language support (NLS). Such object be can
 * {@link #toLocalizable() converted} to an {@link Localizable i18n-message} describing the object analog to its
 * {@link Object#toString() string representation}.
 */
public interface LocalizableObject {

  /**
   * This method is the equivalent to {@link Object#toString()} with native language support.
   *
   * @return an {@link Localizable} representing this object.
   */
  Localizable toLocalizable();

}
