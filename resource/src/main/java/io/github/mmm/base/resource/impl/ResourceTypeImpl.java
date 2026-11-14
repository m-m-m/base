package io.github.mmm.base.resource.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourcePackage;
import io.github.mmm.base.resource.ResourceType;
import io.github.mmm.base.type.JavaType;
import io.github.mmm.base.type.impl.JavaTypeImpl;
import io.github.mmm.base.type.impl.JavaTypeReader;

/// Implementation of [ResourceType].
public final class ResourceTypeImpl extends AbstractResourceFileImpl implements ResourceType {

  private static final Logger LOG = LoggerFactory.getLogger(ResourceTypeImpl.class);

  static final String SUFFIX_CLASS = ".class";

  private Class<?> javaClass;

  private JavaType javaType;

  /// The constructor.
  ///
  /// @param moduleAccess the [module access][#getModuleAccess()].
  /// @param path the [path][#getPath()].
  /// @param simpleName the [simple name][#getSimpleName()].
  /// @param parent the [package folder][#getParent()].
  public ResourceTypeImpl(ModuleAccess moduleAccess, String path, String simpleName, ResourcePackage parent) {

    super(moduleAccess, path, simpleName, parent);
    assert (path.endsWith(SUFFIX_CLASS)) : "invalid class file path:" + path;
    Objects.requireNonNull(parent);
  }

  @Override
  public Class<?> loadClass() {

    if (this.javaClass == null) {
      if (isModuleInfo()) {
        return null;
      }
      this.javaClass = loadClass(this.name, this.moduleAccess.get().getClassLoader(), false);
    }
    return this.javaClass;
  }

  @Override
  public JavaType loadType() {

    if (this.javaType == null) {
      try {
        this.javaType = processAsStream(JavaTypeReader.INSTANCE);
      } catch (Throwable e) {
        LOG.warn("Failed to fast-scan class-file for type {}: {}", this, e.getMessage(), e);
        this.javaType = new JavaTypeImpl(this.name);
      }
    }
    return this.javaType;
  }

  /// @param name the [class name][Class#getName()].
  /// @param classLoader the explicit [ClassLoader] to use or `null` to use [Class#forName(String)].
  /// @param returnNullOnException - `true` to log a potential [Exception] and return `null`,
  ///        `false` to re-throw as [RuntimeException].
  /// @return the loaded [Class]. May be `null` if `returnNullOnException` was `true` and
  ///         class-loading failed.
  public static Class<?> loadClass(String name, ClassLoader classLoader, boolean returnNullOnException) {

    try {
      if (classLoader == null) {
        return Class.forName(name);
      } else {
        return classLoader.loadClass(name);
      }
    } catch (Exception e) {
      if (returnNullOnException) {
        LOG.error("Failed to load class '" + name + "'.", e);
        return null;
      }
      throw new IllegalStateException(e);
    }
  }

  /// @param resourcePath the [resource path][#getPath()] of a potential [Class].
  /// @return the loaded [Class] or `null` if not a [Class] or loading failed.
  public static Class<?> loadClassFromResourcePath(String resourcePath) {

    if (!resourcePath.endsWith(SUFFIX_CLASS)) {
      return null;
    }
    String typeName = resourcePath.substring(0, resourcePath.length() - 6).replace('/', '.');
    return loadClass(typeName, null, true);
  }

}
