package io.github.mmm.base.type;

import java.io.InputStream;
import java.time.Month;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link JavaType}.
 */
public class JavaTypeTest extends Assertions {

  @Test
  void testJavaTypeLoad() {

    // annotation
    verify(Override.class, "public @interface java.lang.Override");
    // record
    verify(ExampleTestRecord.class, "public record io.github.mmm.base.type.ExampleTestRecord");
    // class with extends and multiple implements
    verify(Integer.class,
        "public final class java.lang.Integer extends java.lang.Number implements java.lang.Comparable, java.lang.constant.Constable, java.lang.constant.ConstantDesc");
    // interface with extends
    verify(ProcessHandle.class, "public interface java.lang.ProcessHandle extends java.lang.Comparable");
    // enum
    verify(Month.class,
        "public enum java.time.Month implements java.time.temporal.TemporalAccessor, java.time.temporal.TemporalAdjuster");
  }

  // arrange
  private void verify(Class<?> clazz, String toString) {

    // act
    InputStream in = clazz.getResourceAsStream(clazz.getSimpleName() + ".class");
    // byte[] data;
    // try {
    // data = in.readAllBytes();
    // in.close();
    // } catch (IOException e) {
    // throw new RuntimeIoException(e);
    // }
    // System.out.println("Data length = " + data.length);
    // ClassFile classFile = ClassFile.of();
    // ClassModel model = classFile.parse(data);
    // System.out.println(model);
    // in = new ByteArrayInputStream(data);
    JavaType type = JavaType.readClassFile(in);

    // assert
    if (clazz.isAnnotation()) {
      assertThat(type.getKind()).isSameAs(JavaTypeKind.ANNOTATION);
    } else if (clazz.isInterface()) {
      assertThat(type.getKind()).isSameAs(JavaTypeKind.INTERFACE);
    } else if (clazz.isRecord()) {
      assertThat(type.getKind()).isSameAs(JavaTypeKind.RECORD);
    } else if (clazz.isEnum()) {
      assertThat(type.getKind()).isSameAs(JavaTypeKind.ENUM);
    } else {
      assertThat(type.getKind()).isSameAs(JavaTypeKind.CLASS);
    }
    assertThat(type.getName()).isEqualTo(clazz.getName());
    Class<?> superclass = clazz.getSuperclass();
    if (superclass == null) {
      superclass = Object.class;
    }
    assertThat(type.getSuperClass()).isEqualTo(superclass.getName());
    Class<?>[] interfaces = clazz.getInterfaces();
    assertThat(type.getInterfaceCount()).isEqualTo(interfaces.length);
    for (int i = 0; i < interfaces.length; i++) {
      assertThat(type.getInterface(i)).isEqualTo(interfaces[i].getName());
    }
    assertThat(type).hasToString(toString);
  }

}
