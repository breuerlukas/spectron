package de.lukasbreuer.bot.credentials;

import de.lukasbreuer.bot.configuration.Configuration;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

@Getter
@Accessors(fluent = true)
public final class CredentialsConfiguration extends Configuration {
  private static final String CONFIGURATION_PATH = "/configurations/credentials/credentials.json";

  public static CredentialsConfiguration createAndLoad() throws Exception {
    var configuration = new CredentialsConfiguration(CONFIGURATION_PATH);
    configuration.load();
    return configuration;
  }

  private String username;
  private String uuid;
  private String email;
  private String password;

  private CredentialsConfiguration(String path) {
    super(path);
  }

  @Override
  protected void deserialize(JSONObject json) {
    username = json.getString("username");
    uuid = json.getString("uuid");
    email = json.getString("email");
    password = json.getString("password");
  }
}
