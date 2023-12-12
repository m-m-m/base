package io.github.mmm.base.metadata;

import java.util.Map;
import java.util.Properties;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.base.metainfo.MetaInfos;
import io.github.mmm.base.metainfo.impl.AbstractMetaInfo;

/**
 * Test of {@link MetaInfo}.
 */
@MetaInfos({ "key1=value1", "key2=value2" })
public class MetaInfoTest extends Assertions {

  private String toString(MetaInfo metaInfo) {

    return ((AbstractMetaInfo) metaInfo).toString(true);
  }

  /** Test of {@link MetaInfo#empty()}. */
  @Test
  public void testEmpty() {

    // arrange
    // act
    MetaInfo metaInfo = MetaInfo.empty();
    // assert
    assertThat(metaInfo.isEmpty()).isTrue();
    assertThat(metaInfo.size()).isEqualTo(0);
    assertThat(metaInfo).isEmpty();
    assertThat(metaInfo.get("key1")).isEqualTo(null);
    assertThat(metaInfo.get("key2")).isEqualTo(null);
    assertThat(metaInfo.getParent()).isNull();
    assertThat(toString(metaInfo)).isEqualTo("{}");
  }

  /** Test of {@link MetaInfo#with(String, String)}. */
  @Test
  public void testWithSingle() {

    // arrange
    String key = "key1";
    String value = "value1";
    MetaInfo metaInfo = MetaInfo.empty();
    // act
    metaInfo = metaInfo.with(key, value);
    // assert
    assertThat(metaInfo.isEmpty()).isFalse();
    assertThat(metaInfo.size()).isEqualTo(1);
    assertThat(metaInfo).containsExactly(key);
    assertThat(metaInfo.get("key1")).isEqualTo("value1");
    assertThat(metaInfo.get("key2")).isEqualTo(null);
    assertThat(metaInfo.getParent()).isNull();
    assertThat(toString(metaInfo)).isEqualTo("{key1=value1}");
  }

  /** Test of {@link MetaInfo#with(java.lang.reflect.AnnotatedElement)}. */
  @Test
  public void testWithClass() {

    // arrange
    Class<MetaInfoTest> annotatedClass = MetaInfoTest.class;
    // act
    MetaInfo metaInfo = MetaInfo.empty().with(annotatedClass);
    // assert
    assertThat(metaInfo.size()).isEqualTo(2);
    assertThat(metaInfo).containsExactly("key1", "key2");
    assertThat(metaInfo.get("key1")).isEqualTo("value1");
    assertThat(metaInfo.get("key2")).isEqualTo("value2");
    assertThat(toString(metaInfo)).isEqualTo("{key1=value1, key2=value2}");

    // and act
    MetaInfo metaInfo2 = metaInfo.with(Map.of("key2", "two", "key3", "value3"));
    // and assert
    assertThat(metaInfo2.get("key1")).isEqualTo("value1");
    assertThat(metaInfo2.get(true, "key1")).isEqualTo("value1");
    assertThat(metaInfo2.get(false, "key1")).isNull();
    assertThat(metaInfo2.get("key2")).isEqualTo("two");
    assertThat(metaInfo2.get("key3")).isEqualTo("value3");
    assertThat(metaInfo2).containsExactlyInAnyOrder("key1", "key2", "key3");
    assertThat(metaInfo2.getParent()).isSameAs(metaInfo);
    assertThat(toString(metaInfo2)).isEqualTo("{key1=value1, key2=two, key3=value3}");
  }

  /** Test of {@link MetaInfo#with(Map)}. */
  @Test
  public void testWithMap() {

    // arrange
    Map<String, String> map = Map.of("key1", "42", "key2", "true", "key3", "magicValue");
    // act
    MetaInfo metaInfo = MetaInfo.empty().with(map);
    // assert
    assertThat(metaInfo.size()).isEqualTo(map.size());
    assertThat(metaInfo).containsExactly(map.keySet().toArray(new String[map.size()]));
    assertThat(metaInfo.getAsLong(true, "key1", -1)).isEqualTo(42L);
    assertThat(metaInfo.getAsBoolean(true, "key2", false)).isTrue();
    assertThat(metaInfo.get("key3")).isEqualTo("magicValue");
    assertThat(toString(metaInfo)).isEqualTo("{key1=42, key2=true, key3=magicValue}");
  }

  /** Test of {@link MetaInfo#with(Properties)}. */
  @Test
  public void testWithProperties() {

    // arrange
    Properties root = new Properties();
    root.setProperty("key2", "false");
    Properties properties = new Properties(root);
    properties.setProperty("key3", "magicValue");
    properties.setProperty("key2", "true");
    root.setProperty("key1", "42");
    // act
    MetaInfo metaInfo = MetaInfo.empty().with(properties);
    // assert
    assertThat(metaInfo.size()).isEqualTo(3); // properties.size() returns 2 even though we have key1-3
    assertThat(metaInfo).containsExactly(properties.stringPropertyNames().toArray(new String[3]));
    assertThat(metaInfo.getAsLong("key1", -1)).isEqualTo(42L);
    assertThat(metaInfo.getAsBoolean(true, "key2", false)).isTrue();
    assertThat(metaInfo.get("key3")).isEqualTo("magicValue");
    assertThat(toString(metaInfo)).isEqualTo("{key1=42, key2=true, key3=magicValue}");
  }

}