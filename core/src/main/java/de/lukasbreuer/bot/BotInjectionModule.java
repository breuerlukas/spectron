package de.lukasbreuer.bot;

import com.google.inject.AbstractModule;
import de.lukasbreuer.bot.connection.ConnectionInjectionModule;
import de.lukasbreuer.bot.credentials.CredentialsInjectionModule;
import de.lukasbreuer.bot.log.Log;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public class BotInjectionModule extends AbstractModule {
  @Override
  protected void configure() {
    configureLog();
    install(CredentialsInjectionModule.create());
    install(ConnectionInjectionModule.create());
  }

  private void configureLog() {
    try {
      bind(Log.class).toInstance(Log.create("Core", "/logs/core/"));
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }
}
