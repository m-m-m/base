package io.github.mmm.base.resource;

/// Interface for a [ResourcePath] pointing to a folder ([likely a package][#getPackage()]).
public interface ResourcePackage extends ResourcePath {

  /// @return the Java [Package] or `null` if this is a simple folder that has no [Package]. Examples
  ///         are the [root][#isRoot()] folder, parent packages of modules not containing types, or folders like
  ///         "META-INF" that do not have a corresponding [Package] in Java.
  Package getPackage();

  /// @return `true` if this is the root folder, `false` otherwise.
  default boolean isRoot() {

    return getParent() == null;
  }

  /// @return `true` if the [Package] is open unconditionally (the entire module is open, automatic or
  ///         exports the package unconditionally), `false` otherwise.
  default boolean isOpen() {

    return getModuleAccess().isOpen(getName());
  }

}
