package de.lukasbreuer.spectron.module.foundation;

import de.lukasbreuer.spectron.connection.ConnectionClient;
import de.lukasbreuer.spectron.connection.packet.outbound.play.PacketConfirmTeleportation;
import de.lukasbreuer.spectron.event.EventHook;
import de.lukasbreuer.spectron.event.Hook;
import de.lukasbreuer.spectron.event.play.SynchronizePlayerPositionEvent;
import de.lukasbreuer.spectron.player.PlayerLocation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class SynchronizePlayerPosition implements Hook {
  private final ConnectionClient client;
  private final PlayerLocation playerLocation;

  @EventHook
  private void disconnect(SynchronizePlayerPositionEvent event) {
    playerLocation.update(event.x(), event.y(), event.z(), event.yaw(), event.pitch());
    client.sendPacket(new PacketConfirmTeleportation(event.teleportId()));
  }
}
