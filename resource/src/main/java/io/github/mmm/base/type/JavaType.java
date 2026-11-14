package io.github.mmm.base.type;

import java.io.IOException;
import java.io.InputStream;
import java.lang.classfile.ClassFileVersion;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.base.type.impl.JavaTypeReader;

/// Interface for a simplistic view on a Java class file. Designed for performance and not for completeness. For the
/// latter use [java.lang.classfile.ClassFile] or even [Class]. It has no information about anything but the
/// pure class declaration but no fields, methods, constructors, imports, or nested types.
public interface JavaType {

  /// @return the name of this type.
  String getName();

  /// @return `true` if public, `false` otherwise.
  boolean isPublic();

  /// @return `true` if abstract, `false` otherwise. Please note that interfaces are always abstract no
  ///         matter if declared abstract in the source-code or not.
  boolean isAbstract();

  /// @return `true` if final, `false` otherwise.
  boolean isFinal();

  /// @return `true` if the class-file (byte-code) could not be read properly and this instance is just a dummy as
  ///         fallback, `false` otherwise.
  boolean isBroken();

  /// @return `true` if the [super-class][#getSuperClass()] is [Object].
  default boolean hasObjectAsSuperClass() {

    return Object.class.getName().equals(getSuperClass());
  }

  /// @return the kind of this type.
  JavaTypeKind getKind();

  /// @return the name of the super-class. Will be "java.lang.Object" for an [interface][JavaTypeKind#INTERFACE].
  String getSuperClass();

  /// @return the number of [interfaces][#getInterface(int)] implemented/extended by this type.
  int getInterfaceCount();

  /// @param index index of the requested interface in the range from `0` to
  ///        `[#getInterfaceCount()]-1`.
  /// @return the interface implemented/extended by this type for the given `index`.
  String getInterface(int index);

  /// @return the [ClassFileVersion].
  ClassFileVersion getVersion();

  /// @param in the [InputStream] to the class-file.
  /// @return the parsed [JavaType].
  static JavaType readClassFile(InputStream in) {

    try (in) {
      return JavaTypeReader.INSTANCE.process(in);
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

}
