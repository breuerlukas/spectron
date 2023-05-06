package de.lukasbreuer.bot.credentials;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class CredentialsInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  CredentialsConfiguration provideCredentialsConfiguration() throws Exception {
    return CredentialsConfiguration.createAndLoad();
  }

  @Provides
  @Singleton
  Credentials provideCredentials(CredentialsConfiguration configuration) {
    return Credentials.create(configuration.username(),
      UUID.fromString(configuration.uuid()), configuration.email(),
      configuration.password());
  }

  @Provides
  @Singleton
  @Named("username")
  String provideUsername(Credentials credentials) {
    return credentials.username();
  }

  @Provides
  @Singleton
  @Named("uuid")
  UUID provideUuid(Credentials credentials) {
    return credentials.uuid();
  }

  @Provides
  @Singleton
  @Named("email")
  String provideEmail(Credentials credentials) {
    return credentials.email();
  }

  @Provides
  @Singleton
  @Named("password")
  String providePassword(Credentials credentials) {
    return credentials.password();
  }
}
