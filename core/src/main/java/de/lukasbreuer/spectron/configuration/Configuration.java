package de.lukasbreuer.spectron.configuration;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;

public abstract class Configuration {
  private final String path;

  protected Configuration(String path) {
    this.path = path;
  }

  protected abstract void deserialize(JSONObject json);

  public void load() throws Exception {
    deserialize(new JSONObject(FileUtils.readFileToString(
      new File(System.getProperty("user.dir") + path), Charset.defaultCharset())));
  }
}