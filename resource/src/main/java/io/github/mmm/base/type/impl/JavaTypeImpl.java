package io.github.mmm.base.type.impl;

import java.lang.classfile.ClassFileVersion;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Modifier;

import io.github.mmm.base.type.JavaType;
import io.github.mmm.base.type.JavaTypeKind;

/**
 * Implementation of {@link JavaType}.
 */
public class JavaTypeImpl implements JavaType {

  private final String name;

  private final String superClass;

  private final String[] interfaces;

  private final int accessFlags;

  private final ClassFileVersion version;

  private final JavaTypeKind kind;

  JavaTypeImpl(String name, String superClass, String[] interfaces, int accessFlags, ClassFileVersion version) {

    super();
    this.name = name;
    this.superClass = superClass;
    this.interfaces = interfaces;
    this.accessFlags = accessFlags;
    this.version = version;
    if ((accessFlags & AccessFlag.ANNOTATION.mask()) != 0) {
      this.kind = JavaTypeKind.ANNOTATION;
    } else if (Modifier.isInterface(accessFlags)) {
      this.kind = JavaTypeKind.INTERFACE;
    } else if ((accessFlags & AccessFlag.ENUM.mask()) != 0) {
      this.kind = JavaTypeKind.ENUM;
    } else if ((accessFlags & AccessFlag.MODULE.mask()) != 0) {
      this.kind = JavaTypeKind.MODULE;
    } else if (Record.class.getName().equals(superClass)) {
      this.kind = JavaTypeKind.RECORD;
    } else {
      this.kind = JavaTypeKind.CLASS;
    }
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public String getSuperClass() {

    return this.superClass;
  }

  @Override
  public int getInterfaceCount() {

    return this.interfaces.length;
  }

  @Override
  public String getInterface(int index) {

    return this.interfaces[index];
  }

  @Override
  public ClassFileVersion getVersion() {

    return this.version;
  }

  @Override
  public boolean isPublic() {

    return Modifier.isPublic(this.accessFlags);
  }

  @Override
  public boolean isAbstract() {

    return Modifier.isAbstract(this.accessFlags);
  }

  @Override
  public boolean isFinal() {

    return Modifier.isFinal(this.accessFlags);
  }

  @Override
  public JavaTypeKind getKind() {

    return this.kind;
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder(32);
    if (isPublic()) {
      sb.append("public ");
    }
    if (Modifier.isStatic(this.accessFlags)) {
      sb.append("static ");
    }
    if (isAbstract() && !this.kind.isInterface() && !this.kind.isEnum() && !this.kind.isAnnotation()) {
      sb.append("abstract ");
    }
    if (isFinal() && !this.kind.isRecord() && !this.kind.isEnum()) {
      sb.append("final ");
    }
    if (this.kind.isAnnotation()) {
      sb.append("@interface ");
    } else {
      sb.append(this.kind);
      sb.append(' ');
    }
    sb.append(this.name);
    if (this.kind.isClass() && (this.superClass != null) && !hasObjectAsSuperClass()) {
      sb.append(" extends ");
      sb.append(this.superClass);
    }
    if ((this.interfaces.length > 0) && (!this.kind.isAnnotation())) {
      if (this.kind.isInterface()) {
        sb.append(" extends ");
      } else {
        sb.append(" implements ");
      }
      for (int i = 0; i < this.interfaces.length; i++) {
        if (i > 0) {
          sb.append(", ");
        }
        sb.append(this.interfaces[i]);
      }
    }
    return sb.toString();
  }

}
