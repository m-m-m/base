package io.github.mmm.base.type;

import java.io.IOException;
import java.io.InputStream;
import java.lang.classfile.ClassFileVersion;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.base.type.impl.JavaTypeReader;

/**
 * Interface for a simplistic view on a Java class file. Designed for performance and not for completeness. For the
 * latter use {@link java.lang.classfile.ClassFile} or even {@link Class}. It has no information about anything but the
 * pure class declaration but no fields, methods, constructors, imports, or nested types.
 */
public interface JavaType {

  /**
   * @return the name of this type.
   */
  String getName();

  /**
   * @return {@code true} if public, {@code false} otherwise.
   */
  boolean isPublic();

  /**
   * @return {@code true} if abstract, {@code false} otherwise. Please note that interfaces are always abstract no
   *         matter if declared abstract in the source-code or not.
   */
  boolean isAbstract();

  /**
   * @return {@code true} if final, {@code false} otherwise.
   */
  boolean isFinal();

  /**
   * @return {@code true} if the {@link #getSuperClass() super-class} is {@link Object}.
   */
  default boolean hasObjectAsSuperClass() {

    return Object.class.getName().equals(getSuperClass());
  }

  /**
   * @return the kind of this type.
   */
  JavaTypeKind getKind();

  /**
   * @return the name of the super-class. Will be "java.lang.Object" for an {@link JavaTypeKind#INTERFACE interface}.
   */
  String getSuperClass();

  /**
   * @return the number of {@link #getInterface(int) interfaces} implemented/extended by this type.
   */
  int getInterfaceCount();

  /**
   * @param index index of the requested interface in the range from {@code 0} to
   *        <code>{@link #getInterfaceCount()}-1</code>.
   * @return the interface implemented/extended by this type for the given {@code index}.
   */
  String getInterface(int index);

  /**
   * @return the {@link ClassFileVersion}.
   */
  ClassFileVersion getVersion();

  /**
   * @param in the {@link InputStream} to the class-file.
   * @return the parsed {@link JavaType}.
   */
  static JavaType readClassFile(InputStream in) {

    try (in) {
      return JavaTypeReader.INSTANCE.process(in);
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

}
