package io.github.mmm.base.type;

import java.util.Locale;

/// [Enum] with the availabe kinds of a [JavaType].
///
/// @see JavaType#getKind()
public enum JavaTypeKind {

  /// Kind of an [interface][Class#isInterface()].
  INTERFACE,

  /// Kind of a regular [Class].
  CLASS,

  /// Kind of an [Enum].
  ENUM,

  /// Kind of a [record][Class#isRecord()].
  RECORD,

  /// Kind of an [annotation][Class#isAnnotation()].
  ANNOTATION,

  /// Kind of a [Package] (package-info.class).
  PACKAGE,

  /// Kind of a [Module] (module-info.class).
  MODULE;

  /// @return `true` if an [interface][Class#isInterface()], `false` otherwise.
  public boolean isInterface() {

    return this == INTERFACE;
  }

  /// @return `true` if a regular [Class], `false` otherwise.
  public boolean isClass() {

    return this == CLASS;
  }

  /// @return `true` if an [Enum], `false` otherwise.
  public boolean isEnum() {

    return this == ENUM;
  }

  /// @return `true` if a [record][Class#isRecord()], `false` otherwise.
  public boolean isRecord() {

    return this == RECORD;
  }

  /// @return `true` if an [annotation][Class#isAnnotation()], `false` otherwise.
  public boolean isAnnotation() {

    return this == ANNOTATION;
  }

  @Override
  public String toString() {

    return name().toLowerCase(Locale.ROOT);
  }

}
