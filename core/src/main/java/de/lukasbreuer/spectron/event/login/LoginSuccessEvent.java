package de.lukasbreuer.spectron.event.login;

import de.lukasbreuer.spectron.connection.packet.inbound.login.PacketLoginSuccess;
import de.lukasbreuer.spectron.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class LoginSuccessEvent extends Event {
  private final UUID uuid;
  private final String username;
  private final PacketLoginSuccess.Property[] properties;
}
