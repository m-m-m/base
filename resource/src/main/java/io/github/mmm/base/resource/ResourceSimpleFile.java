package io.github.mmm.base.resource;

import java.io.BufferedReader;
import java.util.List;

/**
 * {@link ResourceFile} that is not a {@link ResourceType}.
 */
public interface ResourceSimpleFile extends ResourceFile {

  default List<String> readAllLines() {

    return processAsReader(BufferedReader::readAllLines);
  }

}
