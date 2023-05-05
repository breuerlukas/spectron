package de.lukasbreuer.bot.module.foundation;

import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketConfirmTeleportation;
import de.lukasbreuer.bot.event.EventHook;
import de.lukasbreuer.bot.event.Hook;
import de.lukasbreuer.bot.event.play.SynchronizePlayerPositionEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class SynchronizePlayerPosition implements Hook {
  private final ConnectionClient client;

  @EventHook
  private void disconnect(SynchronizePlayerPositionEvent event) {
    client.sendPacket(new PacketConfirmTeleportation(event.teleportId()));
  }
}
