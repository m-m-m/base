package io.github.mmm.base.type.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.classfile.constantpool.PoolEntry;
import java.util.Set;

/**
 * Generic entry of the constant pool.
 */
class ConstantPoolEntry {

  static final Set<Integer> LARGE_ENTRIES = Set.of(PoolEntry.TAG_FIELDREF, PoolEntry.TAG_METHODREF,
      PoolEntry.TAG_INTERFACE_METHODREF, PoolEntry.TAG_NAME_AND_TYPE, PoolEntry.TAG_METHOD_HANDLE,
      PoolEntry.TAG_DYNAMIC, PoolEntry.TAG_INVOKE_DYNAMIC);

  final int tag;

  final int first;

  final int second;

  ConstantPoolEntry(int tag, int first, int second) {

    super();
    this.tag = tag;
    this.first = first;
    this.second = second;
  }

  static ConstantPoolEntry read(InputStream in, int tag) throws IOException {

    int first;
    if (tag == PoolEntry.TAG_METHOD_HANDLE) {
      first = in.read();
    } else {
      first = JavaTypeReader.readShort(in);
    }
    int second = 0;
    if (LARGE_ENTRIES.contains(tag)) {
      second = JavaTypeReader.readShort(in);
    }
    return new ConstantPoolEntry(tag, first, second);
  }

  @Override
  public String toString() {

    return this.tag + ":" + this.first + ((this.second == 0) ? "" : this.second);
  }

}
