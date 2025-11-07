package de.lukasbreuer.spectron.connection.packet.outbound.play;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.outbound.PacketOutgoing;
import de.lukasbreuer.spectron.player.PlayerLocation;
import de.lukasbreuer.spectron.player.PlayerStatus;

public final class PacketSetPlayerPosition extends PacketOutgoing {
  private final PlayerLocation location;
  private final PlayerStatus status;

  public PacketSetPlayerPosition(
    PlayerLocation location, PlayerStatus status
  ) {
    super(0x15, ProtocolState.PLAY);
    this.location = location;
    this.status = status;
  }

  public PacketSetPlayerPosition() {
    super(0x15, ProtocolState.PLAY);
    this.location = null;
    this.status = null;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.raw().writeDouble(location.x());
    buffer.raw().writeDouble(location.y());
    buffer.raw().writeDouble(location.z());
    buffer.raw().writeFloat(location.yaw());
    buffer.raw().writeFloat(location.pitch());
    buffer.raw().writeBoolean(status.isOnGround());
  }
}
