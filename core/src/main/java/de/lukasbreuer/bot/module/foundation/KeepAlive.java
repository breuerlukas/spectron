package de.lukasbreuer.bot.module.foundation;

import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketKeepAliveResponse;
import de.lukasbreuer.bot.event.EventHook;
import de.lukasbreuer.bot.event.Hook;
import de.lukasbreuer.bot.event.play.KeepAliveEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class KeepAlive implements Hook {
  private final ConnectionClient client;

  @EventHook
  private void keepAlive(KeepAliveEvent event) {
    client.sendPacket(new PacketKeepAliveResponse(event.number()));
  }
}
