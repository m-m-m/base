package io.github.mmm.base.metainfo.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.base.metainfo.MetaInfo;

/**
 * Holder for the {@link MetaInfo#config() app config}.
 */
public class AppConfigHolder {

  /** @see MetaInfo#config() */
  public static final MetaInfo CONFIG;

  static {
    MetaInfo metaInfo = MetaInfo.empty();
    metaInfo = metaInfo.with(System.getenv());
    metaInfo = withApplicationProperties(metaInfo);
    metaInfo = metaInfo.with(System.getProperties());
    CONFIG = metaInfo;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private static MetaInfo withApplicationProperties(MetaInfo metaInfo) {

    Path configPath = Paths.get("./config/application.properties");
    if (!Files.exists(configPath)) {
      return metaInfo;
    }
    Properties properties = new Properties();
    try (BufferedReader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
      properties.load(reader);
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
    Map<String, String> map = new HashMap<>((Map) properties);
    return metaInfo.with(map);
  }

}
