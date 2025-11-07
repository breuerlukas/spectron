package de.lukasbreuer.spectron.credentials;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class Credentials {
  private final String username;
  private final UUID uuid;
  private final String email;
  private final String password;
}
