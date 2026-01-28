package io.github.mmm.base.type;

import java.util.Locale;

/**
 * {@link Enum} with the availabe kinds of a {@link JavaType}.
 *
 * @see JavaType#getKind()
 */
public enum JavaTypeKind {

  /** Kind of an {@link Class#isInterface() interface}. */
  INTERFACE,

  /** Kind of a regular {@link Class}. */
  CLASS,

  /** Kind of an {@link Enum}. */
  ENUM,

  /** Kind of a {@link Class#isRecord() record}. */
  RECORD,

  /** Kind of an {@link Class#isAnnotation() annotation}. */
  ANNOTATION,

  /** Kind of a {@link Package} (package-info.class). */
  PACKAGE,

  /** Kind of a {@link Module} (module-info.class). */
  MODULE;

  /**
   * @return {@code true} if an {@link Class#isInterface() interface}, {@code false} otherwise.
   */
  public boolean isInterface() {

    return this == INTERFACE;
  }

  /**
   * @return {@code true} if a regular {@link Class}, {@code false} otherwise.
   */
  public boolean isClass() {

    return this == CLASS;
  }

  /**
   * @return {@code true} if an {@link Enum}, {@code false} otherwise.
   */
  public boolean isEnum() {

    return this == ENUM;
  }

  /**
   * @return {@code true} if a {@link Class#isRecord() record}, {@code false} otherwise.
   */
  public boolean isRecord() {

    return this == RECORD;
  }

  /**
   * @return {@code true} if an {@link Class#isAnnotation() annotation}, {@code false} otherwise.
   */
  public boolean isAnnotation() {

    return this == ANNOTATION;
  }

  @Override
  public String toString() {

    return name().toLowerCase(Locale.ROOT);
  }

}
