package de.lukasbreuer.spectron.event.login;

import de.lukasbreuer.spectron.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class EncryptionRequestEvent extends Event {
  private final String serverId;
  private final byte[] publicKey;
  private final byte[] verifyToken;
}
