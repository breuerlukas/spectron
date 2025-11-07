package de.lukasbreuer.spectron.module.foundation;

import de.lukasbreuer.spectron.connection.ConnectionClient;
import de.lukasbreuer.spectron.connection.packet.outbound.play.PacketKeepAliveResponse;
import de.lukasbreuer.spectron.event.EventHook;
import de.lukasbreuer.spectron.event.Hook;
import de.lukasbreuer.spectron.event.play.KeepAliveEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class KeepAlive implements Hook {
  private final ConnectionClient client;

  @EventHook
  private void keepAlive(KeepAliveEvent event) {
    client.sendPacket(new PacketKeepAliveResponse(event.number()));
  }
}
