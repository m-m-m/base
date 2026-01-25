package io.github.mmm.base.resource;

/**
 * {@link ResourcePath} pointing to a {@link Class} or {@link Package}.
 */
public interface ResourceJavaPath extends ResourcePath {

  /**
   * @return the qualified name of the {@link Class} or {@link Package}. This is the {@link #getPath() path} without the
   *         trailing suffix ".class" or "/" and with dots (".") replaced by slashes ("/").
   */
  String getName();

}
