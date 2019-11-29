/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.exception;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.UUID;

import io.github.mmm.base.text.Localizable;

/**
 * Extends {@link RuntimeException} with the following features:
 * <ul>
 * <li>a {@link #getUuid() UUID} unique per exception instance automatically generated once per exception
 * {@link #getCause() chain}.</li>
 * <li>an {@link #getCode() error code} that should be unique per exception type.</li>
 * <li>distinction between {@link #isTechnical() technical} exceptions and exceptions {@link #isForUser() intended for
 * end-users}.</li>
 * </ul>
 * <b>NOTE:</b><br>
 * Exceptions should only occur in unexpected or undesired situations. Never use exceptions for control flows.
 */
public abstract class ApplicationException extends RuntimeException implements Localizable {

  private static final long serialVersionUID = 1L;

  private final Localizable message;

  private final UUID uuid;

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   */
  public ApplicationException(String message) {

    this(Localizable.ofStatic(message));
  }

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   */
  public ApplicationException(Localizable message) {

    super();
    this.message = message;
    this.uuid = createUuid();
  }

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   * @param cause is the {@link #getCause() cause} of this exception.
   */
  public ApplicationException(String message, Throwable cause) {

    this(Localizable.ofStatic(message), cause, null);
  }

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   * @param cause is the {@link #getCause() cause} of this exception.
   */
  public ApplicationException(Localizable message, Throwable cause) {

    this(message, cause, null);
  }

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   * @param cause is the {@link #getCause() cause} of this exception. May be <code>null</code>.
   * @param uuid the explicit {@link #getUuid() UUID} or <code>null</code> to initialize by default (from given
   *        {@link Throwable} or as new {@link UUID}).
   */
  protected ApplicationException(String message, Throwable cause, UUID uuid) {

    this(Localizable.ofStatic(message));
  }

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   * @param cause is the {@link #getCause() cause} of this exception. May be <code>null</code>.
   * @param uuid the explicit {@link #getUuid() UUID} or <code>null</code> to initialize by default (from given
   *        {@link Throwable} or as new {@link UUID}).
   */
  protected ApplicationException(Localizable message, Throwable cause, UUID uuid) {

    super(cause);
    this.message = message;
    if (uuid == null) {
      if ((cause != null) && (cause instanceof ApplicationException)) {
        this.uuid = ((ApplicationException) cause).getUuid();
      } else {
        this.uuid = createUuid();
      }
    } else {
      this.uuid = uuid;
    }
  }

  /**
   * @return a new {@link UUID} for {@link #getUuid()}.
   */
  protected UUID createUuid() {

    return UUID.randomUUID();
  }

  /**
   * This method gets the {@link UUID} of this {@link ApplicationException}. When a new {@link ApplicationException} is
   * created, a {@link UUID} is assigned. In case the {@link ApplicationException} is created from another
   * {@link ApplicationException} as cause, the existing {@link UUID} will be used from that cause. Otherwise a new
   * {@link UUID} is generated. <br>
   * The {@link UUID} will appear in the {@link #printStackTrace() stacktrace} but NOT in the
   * {@link Throwable#getMessage() message}. It will therefore be written to log-files if this exception is logged. If
   * you supply the {@link UUID} to the end-user, he can provide it with the problem report so an administrator or
   * software developer can easily find the stacktrace in the log-files.
   *
   * @return the {@link UUID} of this object.
   */
  public final UUID getUuid() {

    return this.uuid;
  }

  @Override
  public String getMessage() {

    StringBuilder buffer = new StringBuilder();
    getLocalizedMessage(Locale.ROOT, buffer);
    buffer.append(System.lineSeparator());
    buffer.append(this.uuid);
    String code = getCode();
    if (!getClass().getSimpleName().equals(code)) {
      buffer.append(":");
      buffer.append(code);
    }
    return buffer.toString();
  }

  /**
   * @return the {@link Localizable} message describing the problem.
   * @see #getMessage()
   * @see #getLocalizedMessage(Locale)
   */
  public Localizable getNlsMessage() {

    return this.message;
  }

  @Override
  public String getLocalizedMessage(Locale locale) {

    return this.message.getLocalizedMessage(locale);
  }

  @Override
  public void getLocalizedMessage(Locale locale, Appendable appendable) {

    this.message.getLocalizedMessage(locale, appendable);
  }

  /**
   * This method prints the stack trace with localized exception message(s).
   *
   * @param locale is the locale to translate to.
   * @param buffer is where to write the stack trace to.
   * @throws IllegalStateException if the given {@code buffer} produced an {@link java.io.IOException}.
   */
  public void printStackTrace(Locale locale, Appendable buffer) {

    printStackTrace(this, locale, buffer);
  }

  /**
   * @see NlsThrowable#printStackTrace(Locale, Appendable)
   *
   * @param throwable is the {@link NlsThrowable} to print.
   * @param locale is the {@link Locale} to translate to.
   * @param buffer is where to write the stack trace to.
   */
  private static void printStackTrace(ApplicationException throwable, Locale locale, Appendable buffer) {

    try {
      synchronized (buffer) {
        buffer.append(throwable.getClass().getName());
        buffer.append(": ");
        throwable.getLocalizedMessage(locale, buffer);
        buffer.append(System.lineSeparator());
        UUID uuid = throwable.getUuid();
        if (uuid != null) {
          buffer.append(uuid.toString());
          buffer.append(System.lineSeparator());
        }
        StackTraceElement[] trace = throwable.getStackTrace();
        for (int i = 0; i < trace.length; i++) {
          buffer.append("\tat ");
          buffer.append(trace[i].toString());
          buffer.append(System.lineSeparator());
        }
        for (Throwable suppressed : ((Throwable) throwable).getSuppressed()) {
          buffer.append("Suppressed: ");
          buffer.append(System.lineSeparator());
          printStackTraceCause(suppressed, locale, buffer);
        }

        Throwable cause = throwable.getCause();
        if (cause != null) {
          buffer.append("Caused by: ");
          buffer.append(System.lineSeparator());
          printStackTraceCause(cause, locale, buffer);
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * @see NlsThrowable#printStackTrace(Locale, Appendable)
   *
   * @param cause is the {@link Throwable} to print.
   * @param locale is the {@link Locale} to translate to.
   * @param buffer is where to write the stack trace to.
   * @throws IOException if caused by {@code buffer}.
   */
  private static void printStackTraceCause(Throwable cause, Locale locale, Appendable buffer) throws IOException {

    if (cause instanceof ApplicationException) {
      ((ApplicationException) cause).printStackTrace(locale, buffer);
    } else {
      if (buffer instanceof PrintStream) {
        cause.printStackTrace((PrintStream) buffer);
      } else if (buffer instanceof PrintWriter) {
        cause.printStackTrace((PrintWriter) buffer);
      } else {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        cause.printStackTrace(printWriter);
        printWriter.flush();
        buffer.append(writer.toString());
      }
    }
  }

  /**
   * Determines if this is a <em>technical exception</em>.
   * <ul>
   * <li>A technical exception is an unexpected situation that is to be logged on error level and should be analyzed by
   * the operators or software developers of the system. Further in such case the end-user can typically do nothing
   * about the problem (except to retry his operation) and will typically not understand the problem. Therefore a
   * generic message should be {@link #isForUser() displayed to the end-user} in such case.</li>
   * <li>A non technical exception is called <em>user failure</em>. It is an undesired but NOT abnormal situation (e.g.
   * a mandatory field was not filled). It should be logged on a level less than error (typically info). The
   * {@link #getMessage() message} is typically {@link #isForUser() intended for to end-users} and has to be easy to
   * understand.</li>
   * </ul>
   * This separation is intentionally not done via inheritance of technical and business exception base classes to allow
   * reuse. However, if you want to have it this way, create such classes on your own derived from this class.
   *
   * @see #isForUser()
   *
   * @return {@code true} if this is a technical exception, {@code false} if this is a user error.
   */
  public boolean isTechnical() {

    // override to change...
    return true;
  }

  /**
   * <b>Note:</b> Please consider using {@code net.sf.mmm.nls.exception.NlsException} with i18n support in case you are
   * creating exceptions for end-users.
   *
   * @return {@code true} if the {@link #getMessage() message} of this exception is for end-users (or clients),
   *         {@code false} otherwise (for internal {@link #isTechnical() technical} errors).
   */
  public boolean isForUser() {

    return !isTechnical();
  }

  /**
   * This method gets the <em>code</em> that identifies the detailed type of this object. While {@link #getUuid() UUID}
   * is unique per instance of a {@link ApplicationException} this code is a short and readable identifier representing
   * the {@link ApplicationException} {@link Class}. The default implementation returns the {@link Class#getSimpleName()
   * simple name}. However, the code should remain stable after refactoring (so at least after the rename the previous
   * code should be returned as {@link String} literal). This code may be used as a compact identifier to reference the
   * related problem or information as well as for automatic tests of error situations that should remain stable even if
   * the message text gets improved or the locale is unknown.
   *
   * @return the error code.
   */
  public String getCode() {

    return getClass().getSimpleName();
  }

  @Override
  public String toString() {

    // We intentionally use the system locale here to prevent mixed languages in log-files...
    return toString(Locale.getDefault(), null).toString();
  }

  /**
   * Like {@link #toString()} but using the specified {@link Locale}.
   *
   * @param locale is the {@link Locale} used for {@link #getLocalizedMessage(Locale)}.
   * @return the localized string representation of this exception as described in {@link #toString(Locale, Appendable)}
   */
  public String toString(Locale locale) {

    return toString(locale, null).toString();
  }

  /**
   * {@link Appendable#append(CharSequence) appends} the localized string representation of this exception. It is
   * defined as following:
   *
   * <pre>
   * &lt;{@link Class#getName() classname}>: [&lt;custom-{@link #getCode() code}>: ]&lt;{@link #getLocalizedMessage(Locale) message}>
   * </pre>
   *
   * @param locale is the {@link Locale} used for {@link #getLocalizedMessage(Locale)}.
   * @param appendable is the buffer to {@link Appendable#append(CharSequence) append} to. Will be created as
   *        {@link StringBuilder} if {@code null} is provided.
   * @return the provided {@link Appendable} or the created one if {@code null} was given.
   */
  public Appendable toString(Locale locale, Appendable appendable) {

    Appendable buffer = appendable;
    if (buffer == null) {
      buffer = new StringBuilder(32);
    }
    try {
      Class<?> myClass = getClass();
      buffer.append(myClass.getName());
      buffer.append(": ");
      String code = getCode();
      if (!myClass.getSimpleName().equals(code)) {
        buffer.append(code);
        buffer.append(": ");
      }
      buffer.append(getLocalizedMessage(locale));
      if (this.uuid != null) {
        buffer.append(System.lineSeparator());
        buffer.append(this.uuid.toString());
      }
      return buffer;
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to appendable", e);
    }
  }
}
