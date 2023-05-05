package de.lukasbreuer.bot.connection.packet.outbound.play;

import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;
import de.lukasbreuer.bot.player.PlayerLocation;
import de.lukasbreuer.bot.player.PlayerStatus;

public final class PacketSetPlayerPosition extends PacketOutgoing {
  private final PlayerLocation location;
  private final PlayerStatus status;

  public PacketSetPlayerPosition(
    PlayerLocation location, PlayerStatus status
  ) {
    super(0x14, ProtocolState.PLAY);
    this.location = location;
    this.status = status;
  }

  public PacketSetPlayerPosition() {
    super(0x14, ProtocolState.PLAY);
    this.location = null;
    this.status = null;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.raw().writeDouble(location.x());
    buffer.raw().writeDouble(location.y());
    buffer.raw().writeDouble(location.z());
    buffer.raw().writeBoolean(status.isOnGround());
  }
}
