package de.lukasbreuer.spectron.connection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class ConnectionInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  ConnectionConfiguration provideConnectionConfiguration() throws Exception {
    return ConnectionConfiguration.createAndLoad();
  }

  @Provides
  @Singleton
  @Named("hostname")
  String provideHostname(ConnectionConfiguration configuration) {
    return configuration.hostname();
  }

  @Provides
  @Singleton
  @Named("port")
  short providePort(ConnectionConfiguration configuration) {
    return configuration.port();
  }

  @Provides
  @Singleton
  @Named("protocolVersion")
  int provideProtocolVersion(ConnectionConfiguration configuration) {
    return configuration.protocolVersion();
  }
}
