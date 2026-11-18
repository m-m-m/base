package io.github.mmm.base.metainfo.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.base.metainfo.MetaInfo;

/// Holder for the [app config][MetaInfo#config()].
public class AppConfigHolder {

  /// @see MetaInfo#config()
  public static final MetaInfo CONFIG;

  static {
    MetaInfo metaInfo = MetaInfo.empty();
    metaInfo = withProperties(metaInfo, "application.properties");
    metaInfo = metaInfo.with(System.getenv());
    metaInfo = withProperties(metaInfo, "app-config/application.properties");
    metaInfo = metaInfo.with(System.getProperties());
    CONFIG = metaInfo;
  }

  private static MetaInfo withProperties(MetaInfo metaInfo, String propertiesPath) {

    Properties properties = new Properties();
    Path fsPath = Path.of(".").resolve(propertiesPath);
    String source;
    if (Files.exists(fsPath)) {
      try (BufferedReader reader = Files.newBufferedReader(fsPath, StandardCharsets.UTF_8)) {
        properties.load(reader);
        source = fsPath.toAbsolutePath().toString();
      } catch (IOException e) {
        throw new RuntimeIoException(e);
      }
    } else {
      URL resource = Thread.currentThread().getContextClassLoader().getResource(propertiesPath);
      if (resource == null) {
        source = "unavailable:" + propertiesPath;
      } else {
        try (InputStream in = resource.openStream()) {
          InputStreamReader reader = new InputStreamReader(in);
          properties.load(reader);
          source = resource.toString();
          properties.put(MetaInfo.KEY_SOURCE, source);
        } catch (IOException e) {
          throw new RuntimeIoException(e);
        }
      }
    }
    properties.put(MetaInfo.KEY_SOURCE.getName(), source);
    return metaInfo.with(properties);
  }

}
