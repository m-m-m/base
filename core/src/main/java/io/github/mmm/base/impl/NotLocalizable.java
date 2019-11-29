/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.impl;

import java.io.IOException;
import java.util.Locale;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.base.text.Localizable;

/**
 * Dummy implementation of {@link Localizable} that does not support localization and simply ignores any given
 * {@link Locale}.
 */
public class NotLocalizable implements Localizable {

  private final String message;

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message}.
   */
  public NotLocalizable(String message) {

    super();
    this.message = message;
  }

  @Override
  public String getMessage() {

    return this.message;
  }

  @Override
  public String getLocalizedMessage() {

    return this.message;
  }

  @Override
  public String getLocalizedMessage(Locale locale) {

    return this.message;
  }

  @Override
  public void getLocalizedMessage(Locale locale, Appendable buffer) {

    try {
      buffer.append(this.message);
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

}
