/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.version;

/// Abstract interface for the development phase from a [letter-sequence][VersionLetters] of [VersionSegment].
public interface AbstractVersionPhase {

  /// @return `true` if this [VersionPhase] is a real development phase that says something about the quality
  ///         of the release, `false` otherwise.
  boolean isDevelopmentPhase();

  /// @return `true` if this is an unstable [development phase][#isDevelopmentPhase()] (a pre-release).
  boolean isUnstable();

  /// @return `true` if this is a stable [development phase][#isDevelopmentPhase()] (an official release).
  boolean isStable();

}
