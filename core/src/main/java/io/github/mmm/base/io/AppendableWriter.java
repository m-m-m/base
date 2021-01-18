/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

import io.github.mmm.base.exception.RuntimeIoException;

/**
 * {@link Writer} that adapts an {@link Appendable} to avoid checked {@link IOException}s.
 *
 * @since 1.0.0
 */
public class AppendableWriter extends Writer {

  /** The delegate. */
  private final Appendable appendable;

  /**
   * The constructor.
   *
   * @param appendable is the {@link Appendable} to adapt.
   */
  public AppendableWriter(Appendable appendable) {

    super();
    this.appendable = appendable;
  }

  @Override
  public void close() throws RuntimeIoException {

    try {
      if (this.appendable instanceof Closeable) {
        ((Closeable) this.appendable).close();
      }
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public void flush() throws RuntimeIoException {

    try {
      if (this.appendable instanceof Flushable) {
        ((Flushable) this.appendable).flush();
      }
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public AppendableWriter append(char c) throws RuntimeIoException {

    try {
      this.appendable.append(c);
      return this;
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public AppendableWriter append(CharSequence csq) throws RuntimeIoException {

    try {
      this.appendable.append(csq);
      return this;
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public AppendableWriter append(CharSequence csq, int start, int end) throws RuntimeIoException {

    try {
      this.appendable.append(csq, start, end);
      return this;
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public void write(int c) throws IOException {

    try {
      this.appendable.append((char) c);
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public void write(char[] buffer) throws RuntimeIoException {

    append(new String(buffer));
  }

  @Override
  public void write(String string) throws RuntimeIoException {

    append(string);
  }

  @Override
  public void write(String string, int offset, int length) throws RuntimeIoException {

    append(string, offset, offset + length);
  }

  @Override
  public void write(char[] buffer, int offset, int length) throws RuntimeIoException {

    append(new String(buffer, offset, length));
  }

  /**
   * This method gets the {@link Appendable} to delegate to.
   *
   * @return the appendable
   */
  public Appendable getAppendable() {

    return this.appendable;
  }

  @Override
  public String toString() {

    return this.appendable.toString();
  }

  /**
   * @param appendable the {@link Appendable} to convert.
   * @return a {@link Writer} writing to the given {@link Appendable}.
   */
  public static Writer asWriter(Appendable appendable) {

    if (appendable == null) {
      return null;
    } else if (appendable instanceof Writer) {
      return (Writer) appendable;
    } else {
      return new AppendableWriter(appendable);
    }
  }
}
