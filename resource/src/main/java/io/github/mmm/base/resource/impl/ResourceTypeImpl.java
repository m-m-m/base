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

/**
 * Implementation of {@link ResourceType}.
 */
public final class ResourceTypeImpl extends AbstractResourceFileImpl implements ResourceType {

  private static final Logger LOG = LoggerFactory.getLogger(ResourceTypeImpl.class);

  static final String SUFFIX_CLASS = ".class";

  private Class<?> javaClass;

  private JavaType javaType;

  /**
   * The constructor.
   *
   * @param moduleAccess the {@link #getModuleAccess() module access}.
   * @param path the {@link #getPath() path}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param parent the {@link #getParent() package folder}.
   */
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

  /**
   * @param name the {@link Class#getName() class name}.
   * @param classLoader the explicit {@link ClassLoader} to use or {@code null} to use {@link Class#forName(String)}.
   * @param returnNullOnException - {@code true} to log a potential {@link Exception} and return {@code null},
   *        {@code false} to re-throw as {@link RuntimeException}.
   * @return the loaded {@link Class}. May be {@code null} if {@code returnNullOnException} was {@code true} and
   *         class-loading failed.
   */
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

  /**
   * @param resourcePath the {@link #getPath() resource path} of a potential {@link Class}.
   * @return the loaded {@link Class} or {@code null} if not a {@link Class} or loading failed.
   */
  public static Class<?> loadClassFromResourcePath(String resourcePath) {

    if (!resourcePath.endsWith(SUFFIX_CLASS)) {
      return null;
    }
    String typeName = resourcePath.substring(0, resourcePath.length() - 6).replace('/', '.');
    return loadClass(typeName, null, true);
  }

}
