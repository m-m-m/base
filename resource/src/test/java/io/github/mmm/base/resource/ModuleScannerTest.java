package io.github.mmm.base.resource;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.module.ResolvedModule;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.base.lang.Builder;
import io.github.mmm.base.lang.ValueType;

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
    // simple files...
    assertThat(resources.getSimpleFiles()).hasSizeGreaterThanOrEqualTo(1);
    ResourceSimpleFile service = resources.get("META-INF/services/io.github.mmm.base.temporal.TemporalConverter");
    assertThat(service.isFile()).isTrue();
    assertThat(service.isSimple()).isTrue();
    assertThat(service.isFolder()).isFalse();
    assertThat(service.isJava()).isFalse();
    assertThat(service.readAllLines()).containsExactly("io.github.mmm.base.temporal.impl.TemporalConverterImpl");
    // types...
    assertThat(resources.getTypes()).hasSizeGreaterThan(100);
    ResourceType pkgInfo = resources.get("io/github/mmm/base/lang/package-info.class");
    assertThat(pkgInfo.isFile()).isTrue();
    assertThat(pkgInfo.isSimple()).isFalse();
    assertThat(pkgInfo.isFolder()).isFalse();
    assertThat(pkgInfo.isJava()).isTrue();
    assertThat(pkgInfo.isPackageInfo()).isTrue();
    assertThat(pkgInfo.isInnerType()).isFalse();
    ResourceType builder = resources.get("io/github/mmm/base/lang/Builder.class");
    assertThat(builder.loadClass()).isSameAs(Builder.class);
    assertThat(builder.isPackageInfo()).isFalse();
    assertThat(builder.isInnerType()).isFalse();
    ResourceType valueType1 = resources.get("io/github/mmm/base/lang/ValueType$1.class");
    assertThat(valueType1.isPackageInfo()).isFalse();
    assertThat(valueType1.isInnerType()).isTrue();
    assertThat(valueType1.loadClass().getNestHost()).isSameAs(ValueType.class);
    // packages
    assertThat(resources.getPackages()).hasSizeGreaterThan(10);
    ResourcePackage lang = resources.get("io/github/mmm/base/lang/");
    assertThat(lang.isFile()).isFalse();
    assertThat(lang.isSimple()).isFalse();
    assertThat(lang.isFolder()).isTrue();
    assertThat(lang.isJava()).isTrue();
    assertThat(lang.getPackage()).isSameAs(Builder.class.getPackage());
    // simple folders
    assertThat(resources.getSimpleFolders()).hasSizeGreaterThan(2);
    ResourceSimpleFolder metaInf = resources.get("META-INF/");
    assertThat(metaInf.isFile()).isFalse();
    assertThat(metaInf.isSimple()).isTrue();
    assertThat(metaInf.isFolder()).isTrue();
    assertThat(metaInf.isJava()).isFalse();
  }

}
