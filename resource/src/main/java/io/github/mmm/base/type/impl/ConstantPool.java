package io.github.mmm.base.type.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.classfile.constantpool.PoolEntry;

import io.github.mmm.base.number.NumberCodec;

/**
 * Constant pool of class file structure.
 */
class ConstantPool {

  private final Object[] pool;

  ConstantPool(int size) {

    super();
    this.pool = new Object[size];
  }

  @SuppressWarnings("unchecked")
  <T> T get(int i) {

    return (T) this.pool[i];
  }

  String getName(int i) {

    Object value = get(i);
    if (value instanceof ConstantPoolEntry entry) {
      return get(entry.first);
    }
    return "Undefined";
  }

  String getTypeName(int i) {

    return getName(i).replace('/', '.');
  }

  static ConstantPool read(InputStream in) throws IOException {

    int size = JavaTypeReader.readShort(in);
    assert (size > 0);
    ConstantPool result = new ConstantPool(size);
    result.readAll(in);
    return result;
  }

  private void readAll(InputStream in) throws IOException {

    int i = 1;
    while (i < this.pool.length) {
      int tag = in.read();
      Object entry = switch (tag) {
        case 0 -> null;
        case PoolEntry.TAG_UTF8 -> readUtf8(in);
        case PoolEntry.TAG_INTEGER -> readInteger(in);
        case PoolEntry.TAG_FLOAT -> readFloat(in);
        case PoolEntry.TAG_LONG -> readLong(in);
        case PoolEntry.TAG_DOUBLE -> readDouble(in);
        default -> ConstantPoolEntry.read(in, tag);
      };
      this.pool[i++] = entry;
      if ((tag == PoolEntry.TAG_LONG) || (tag == PoolEntry.TAG_DOUBLE)) {
        i++;
      }
    }
  }

  private String readUtf8(InputStream in) throws IOException {

    int length = JavaTypeReader.readShort(in);
    byte[] utf8 = in.readNBytes(length);
    return new String(utf8);
  }

  private int readInteger(InputStream in) throws IOException {

    byte[] bytes = in.readNBytes(4);
    return NumberCodec.readU4(bytes, 0, false);
  }

  private float readFloat(InputStream in) throws IOException {

    return Float.intBitsToFloat(readInteger(in));
  }

  private long readLong(InputStream in) throws IOException {

    byte[] bytes = in.readNBytes(8);
    return NumberCodec.readU8(bytes, 0, false);
  }

  private double readDouble(InputStream in) throws IOException {

    return Double.longBitsToDouble(readLong(in));
  }

}
