/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.i18n;

/// This is the interface for a [Localizable] object with native language support (NLS). Such object be can
/// [converted][#toLocalizable()] to an [Localizable] describing the object analog to its
/// [string representation][Object#toString()] but for end-users with internationalization support.
public interface LocalizableObject {

  /// This method is the equivalent to [Object#toString()] with native language support.
  ///
  /// @return an [Localizable] representing this object.
  Localizable toLocalizable();

}
