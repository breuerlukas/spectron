package de.lukasbreuer.bot.connection;

import de.lukasbreuer.bot.configuration.Configuration;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

@Getter
@Accessors(fluent = true)
public final class ConnectionConfiguration extends Configuration {
  private static final String CONFIGURATION_PATH = "/configurations/connection/connection.json";

  public static ConnectionConfiguration createAndLoad() throws Exception {
    var configuration = new ConnectionConfiguration(CONFIGURATION_PATH);
    configuration.load();
    return configuration;
  }

  private String hostname;
  private short port;
  private int protocolVersion;

  private ConnectionConfiguration(String path) {
    super(path);
  }

  @Override
  protected void deserialize(JSONObject json) {
    hostname = json.getString("hostname");
    port = (short) json.getInt("port");
    protocolVersion = json.getInt("protocolVersion");
  }
}
