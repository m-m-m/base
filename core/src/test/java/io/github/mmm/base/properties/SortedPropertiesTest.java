package io.github.mmm.base.properties;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link SortedProperties}.
 */
public class SortedPropertiesTest extends Assertions {

  /**
   * Test of {@link SortedProperties}.
   *
   * @throws IOException on error.
   */
  @Test
  public void test() throws IOException {

    // given
    String unsorted = "key=value\n" //
        + "apple=banana\n" //
        + "zoo=forrest\n" //
        + "key2=value2";
    // when
    SortedProperties properties = new SortedProperties();
    properties.load(new StringReader(unsorted));
    properties.put("key1", "value1");
    StringWriter writer = new StringWriter();
    properties.store(writer, null);
    String sorted = writer.toString().replace("\r", "").replaceAll("#.*\n", "");
    // then
    String expected = "apple=banana\n" //
        + "key=value\n" //
        + "key1=value1\n" //
        + "key2=value2\n" //
        + "zoo=forrest\n";
    assertThat(sorted).isEqualTo(expected);
  }

}
