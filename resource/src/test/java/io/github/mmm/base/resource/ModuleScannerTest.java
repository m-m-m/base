package io.github.mmm.base.resource;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.module.ResolvedModule;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.base.lang.Composable;
import io.github.mmm.base.lang.Conjunction;
import io.github.mmm.base.lang.ToStringFormatter;
import io.github.mmm.base.lang.ValueType;
import io.github.mmm.base.type.JavaType;
import io.github.mmm.base.type.JavaTypeKind;

/**
 * Test of {@link ModuleScanner}.
 */
class ModuleScannerTest extends Assertions {

  @Test
  void testGetRequired() {

    // arrange
    String moduleNameJavaBase = "java.base";
    String moduleNameMmmBase = "io.github.mmm.base";
    ModuleScanner scanner = ModuleScanner.get();

    // act
    ModuleAccess moduleAccessJavaBase = scanner.getRequired(moduleNameJavaBase);
    ModuleAccess moduleAccessMmmBase = scanner.getRequired(moduleNameMmmBase);

    // assert
    assertThat(moduleAccessJavaBase.getResolved().name()).isEqualTo(moduleNameJavaBase);
    assertThat(moduleAccessJavaBase.get().getName()).isEqualTo(moduleNameJavaBase);
    assertThat(moduleAccessMmmBase.getResolved().name()).isEqualTo(moduleNameMmmBase);
    assertThat(moduleAccessMmmBase.get().getName()).isEqualTo(moduleNameMmmBase);
  }

  @Test
  void testGetRequiredNonExistent() {

    // arrange
    String moduleName = "non-existent";
    ModuleScanner scanner = ModuleScanner.get();

    // act
    ObjectNotFoundException error = assertThrows(ObjectNotFoundException.class, () -> scanner.getRequired(moduleName));

    // assert
    assertThat(error.getNlsMessage().getMessage()).isEqualTo("Could not find Module for key 'non-existent'");
  }

  @Test
  void testGetNonExistent() {

    // arrange
    String moduleName = "non-existent";
    ModuleScanner scanner = ModuleScanner.get();

    // act
    ModuleAccess moduleAccess = scanner.get(moduleName);

    // assert
    assertThat(moduleAccess).isNull();
  }

  @Test
  void testGetAll() {

    // arrange
    ModuleScanner scanner = ModuleScanner.get();

    // act
    Collection<ModuleAccess> allModules = scanner.getAll();

    // assert
    // most likely 67 (41 jdk.* modules, 21 java.* modules, 1 slf4j, 2 logback, 2 mmm)
    // however, we do not want the test to break because of internal changes in newer JDK versions
    assertThat(allModules).hasSizeGreaterThan(60);
    Set<String> requiredModules = new HashSet<>(Arrays.asList("java.base", "jdk.net", "org.slf4j",
        "ch.qos.logback.core", "ch.qos.logback.classic", "io.github.mmm.base", "io.github.mmm.base.resource"));
    for (ModuleAccess moduleAccess : allModules) {
      Module module = moduleAccess.get();
      ResolvedModule resolved = moduleAccess.getResolved();
      assertThat(module).isNotNull();
      assertThat(resolved).isNotNull();
      assertThat(module.getName()).isEqualTo(resolved.name());
      requiredModules.remove(module.getName());
    }
    assertThat(requiredModules).isEmpty(); // we found and removed all required modules...
  }

  @Test
  void testGetAndFindResources() {

    // arrange
    String moduleName = "io.github.mmm.base";
    ModuleScanner scanner = ModuleScanner.get();

    // act
    ModuleAccess moduleAccess = scanner.getRequired(moduleName);
    ResourceMap resources = moduleAccess.findResources();

    // assert
    assertThat(resources.getAll()).hasSizeGreaterThan(120);
    // regular files...
    assertThat(resources.getFiles()).hasSizeGreaterThanOrEqualTo(1);
    ResourceFile service = getFile(resources, "META-INF/services/io.github.mmm.base.temporal.TemporalConverter",
        "META-INF.services.io.github.mmm.base.temporal.TemporalConverter",
        "io.github.mmm.base.temporal.TemporalConverter", moduleAccess);
    assertThat(service.readAllLines()).containsExactly("io.github.mmm.base.temporal.impl.TemporalConverterImpl");
    // types (class files)...
    assertThat(resources.getTypes()).hasSizeGreaterThan(100);
    ResourceType pkgInfo = getType(resources, "io/github/mmm/base/lang/package-info.class",
        "io.github.mmm.base.lang.package-info", "package-info", moduleAccess);
    ResourceType composable = getType(resources, Composable.class, moduleAccess);
    ResourceType conjunction = getType(resources, Conjunction.class, moduleAccess);
    ResourceType toStringFormatter = getType(resources, ToStringFormatter.class, moduleAccess);
    ResourceType valueType1 = getType(resources, "io/github/mmm/base/lang/ValueType$1.class",
        "io.github.mmm.base.lang.ValueType$1", "ValueType$1", moduleAccess);
    assertThat(valueType1.loadClass().getNestHost()).isSameAs(ValueType.class);
    ResourceType moduleInfo = getModuleInfo(resources, moduleAccess);

    // packages
    assertThat(resources.getPackages()).hasSizeGreaterThan(10);
    ResourcePackage lang = getPackage(resources, "io/github/mmm/base/lang/", "io.github.mmm.base.lang", "lang",
        moduleAccess);
    assertThat(lang.getPackage()).isSameAs(composable.getParent().getPackage())
        .isSameAs(pkgInfo.getParent().getPackage());
    ResourcePackage root = moduleInfo.getParent();
    assertThat(root.getPath()).isEqualTo("/");
    assertThat(root.getName()).isEmpty();
    assertThat(root.getSimpleName()).isEmpty();
    assertThat(root.isFile()).isFalse();
    assertThat(root.isType()).isFalse();
    assertThat(root.getParent()).isNull();
    assertThat(root.getPackage()).isNull();

    // packages that are only folders
    ResourcePackage metaInf = getPackage(resources, "META-INF/", "META-INF", "META-INF", moduleAccess);
    assertThat(metaInf.getParent().isRoot()).isTrue();
    assertThat(metaInf.getPackage()).isNull();
    ResourcePackage base = getPackage(resources, "io/github/mmm/base/", "io.github.mmm.base", "base", moduleAccess);
    assertThat(base.getPackage()).isNull();

    // fast load types as JavaType
    JavaType type = loadType(composable, Composable.class);
    assertThat(type).hasToString("public interface io.github.mmm.base.lang.Composable extends java.lang.Iterable");
    type = loadType(conjunction, Conjunction.class);
    assertThat(type.getSuperClass()).isEqualTo(Enum.class.getName());
    assertThat(type.isAbstract()).isTrue();
    assertThat(type.isFinal()).isFalse();
    assertThat(type.getInterfaceCount()).isZero();
    assertThat(type).hasToString("public enum io.github.mmm.base.lang.Conjunction");
    type = loadType(toStringFormatter, ToStringFormatter.class);
    assertThat(type).hasToString(
        "public final class io.github.mmm.base.lang.ToStringFormatter implements java.util.function.Function");
  }

  @SuppressWarnings("unchecked")
  private <T extends ResourcePath> T getResource(ResourceMap resources, String path, String name, String simpleName,
      ModuleAccess access) {

    ResourcePath resource;
    if (path.equals(ResourceType.MODULE_INFO_CLASS)) {
      resource = resources.getModuleInfo();
    }
    resource = resources.getByPath(path);
    assertThat(resource.getPath()).isEqualTo(path);
    assertThat(resource.getName()).isEqualTo(name);
    assertThat(resource.getSimpleName()).isEqualTo(simpleName);
    assertThat(resource.getModuleAccess()).isSameAs(access);
    return (T) resource;
  }

  private ResourcePackage getPackage(ResourceMap resources, String path, String name, String simpleName,
      ModuleAccess access) {

    ResourcePackage pkg = getResource(resources, path, name, simpleName, access);
    assertThat(pkg.isPackage()).isTrue();
    assertThat(pkg.isFile()).isFalse();
    assertThat(pkg.isType()).isFalse();
    return pkg;
  }

  private ResourceFile getFile(ResourceMap resources, String path, String name, String simpleName,
      ModuleAccess access) {

    ResourceFile file = getResource(resources, path, name, simpleName, access);
    assertThat(file.isFile()).isTrue();
    assertThat(file.isPackage()).isFalse();
    assertThat(file.isType()).isFalse();
    return file;
  }

  private ResourceType getType(ResourceMap resources, String path, String name, String simpleName,
      ModuleAccess access) {

    ResourceType type = getResource(resources, path, name, simpleName, access);
    assertThat(type.isType()).isTrue();
    assertThat(type.isFile()).isFalse();
    assertThat(type.isPackage()).isFalse();
    assertThat(type.isInnerType()).isEqualTo(path.contains("$"));
    assertThat(type.isPackageInfo()).isEqualTo(simpleName.equals("package-info"));
    assertThat(type.isModuleInfo()).isEqualTo(simpleName.equals("module-info"));
    return type;
  }

  private ResourceType getType(ResourceMap resources, Class<?> clazz, ModuleAccess access) {

    ResourceType type = getType(resources, clazz.getName().replace('.', '/') + ".class", clazz.getName(),
        clazz.getSimpleName(), access);
    assertThat(type.loadClass()).isSameAs(clazz);
    assertThat(type.getParent().getPackage()).isSameAs(clazz.getPackage());
    return type;
  }

  private ResourceType getModuleInfo(ResourceMap resources, ModuleAccess access) {

    ResourceType type = getType(resources, "module-info.class", "module-info", "module-info", access);
    assertThat(type.loadClass()).isNull();
    return type;
  }

  private JavaType loadType(ResourceType resourceType, Class<?> clazz) {

    JavaType javaType = resourceType.loadType();
    JavaTypeKind kind = javaType.getKind();
    if (clazz.isAnnotation()) {
      assertThat(kind.isAnnotation()).isTrue();
    } else if (clazz.isEnum()) {
      assertThat(kind.isEnum()).isTrue();
    } else if (clazz.isRecord()) {
      assertThat(kind.isRecord()).isTrue();
    } else if (clazz.isInterface()) {
      assertThat(kind.isInterface()).isTrue();
    } else {
      assertThat(kind.isClass()).isTrue();
    }
    assertThat(javaType.getName()).isEqualTo(clazz.getName()).isEqualTo(resourceType.getName());
    Class<?> superclass = clazz.getSuperclass();
    if (superclass == null) {
      assertThat(javaType.hasObjectAsSuperClass()).isTrue();
    } else {
      assertThat(javaType.getSuperClass()).isEqualTo(superclass.getName());
    }
    int modifiers = clazz.getModifiers();
    assertThat(javaType.isPublic()).isEqualTo(Modifier.isPublic(modifiers));
    assertThat(javaType.isAbstract()).isEqualTo(Modifier.isAbstract(modifiers));
    assertThat(javaType.isFinal()).isEqualTo(Modifier.isFinal(modifiers));
    Class<?>[] interfaces = clazz.getInterfaces();
    assertThat(javaType.getInterfaceCount()).isEqualTo(interfaces.length);
    for (int i = 0; i < interfaces.length; i++) {
      assertThat(javaType.getInterface(i)).isEqualTo(interfaces[i].getName());
    }
    return javaType;
  }

}
