/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.impl;

import java.io.IOException;
import java.util.Objects;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.base.io.UncheckedAppendable;

/**
 * Implementation of {@link UncheckedAppendable}.
 *
 * @since 1.0.0
 */
public class UncheckedAppendableWrapper implements UncheckedAppendable {

  private final Appendable appendable;

  /**
   * The constructor.
   *
   * @param appendable the {@link #unwrap() underlying} {@link Appendable} to wrap.
   */
  public UncheckedAppendableWrapper(Appendable appendable) {

    super();
    Objects.requireNonNull(appendable);
    this.appendable = appendable;
  }

  @Override
  public UncheckedAppendableWrapper append(char c) throws RuntimeIoException {

    try {
      this.appendable.append(c);
      return this;
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public UncheckedAppendableWrapper append(CharSequence csq) throws RuntimeIoException {

    try {
      this.appendable.append(csq);
      return this;
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public UncheckedAppendableWrapper append(CharSequence csq, int start, int end) throws RuntimeIoException {

    try {
      this.appendable.append(csq, start, end);
      return this;
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public Appendable unwrap() {

    return this.appendable;
  }

  @Override
  public String toString() {

    return this.appendable.toString();
  }

}
