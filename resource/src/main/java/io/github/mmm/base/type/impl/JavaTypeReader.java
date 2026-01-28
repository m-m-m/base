package io.github.mmm.base.type.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.classfile.ClassFileVersion;

import io.github.mmm.base.io.IoProcessor;
import io.github.mmm.base.number.NumberCodec;
import io.github.mmm.base.type.JavaType;

/**
 * Simple reader to class-file as {@link JavaType}.
 */
public class JavaTypeReader implements IoProcessor<InputStream, JavaType> {

  /** Singleton instance of {@link JavaTypeReader}. */
  public static final JavaTypeReader INSTANCE = new JavaTypeReader();

  private static final int MAGIC = 0xCAFEBABE;

  @Override
  public JavaType process(InputStream in) throws IOException {

    // ClassFile.of().parse()
    int magic = readInteger(in);
    if (magic != MAGIC) {
      throw new IllegalStateException();
    }
    int minor = readShort(in);
    int major = readShort(in);
    ClassFileVersion version = ClassFileVersion.of(major, minor);

    ConstantPool pool = ConstantPool.read(in);

    int accessFlags = readShort(in);
    int thisClassIndex = readShort(in);
    String name = pool.getTypeName(thisClassIndex);
    int superClassIndex = readShort(in);
    String superClass = pool.getTypeName(superClassIndex);
    int interfacesCount = readShort(in);
    String[] interfaces = new String[interfacesCount];
    for (int i = 0; i < interfacesCount; i++) {
      interfaces[i] = pool.getTypeName(readShort(in));
    }
    return new JavaTypeImpl(name, superClass, interfaces, accessFlags, version);
  }

  static int readShort(InputStream in) throws IOException {

    byte[] bytes = in.readNBytes(2);
    return NumberCodec.u2(bytes[0], bytes[1]);
  }

  static int readInteger(InputStream in) throws IOException {

    byte[] bytes = in.readNBytes(4);
    return NumberCodec.u4(bytes, 0);
  }
}
