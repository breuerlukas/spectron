package de.lukasbreuer.bot.connection.packet.outbound.play;

import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;
import de.lukasbreuer.bot.player.PlayerLocation;
import de.lukasbreuer.bot.player.PlayerStatus;

public final class PacketSetPlayerRotation extends PacketOutgoing {
  private final PlayerLocation location;
  private final PlayerStatus status;

  public PacketSetPlayerRotation(
    PlayerLocation location, PlayerStatus status
  ) {
    super(0x16, ProtocolState.PLAY);
    this.location = location;
    this.status = status;
  }

  public PacketSetPlayerRotation() {
    super(0x16, ProtocolState.PLAY);
    this.location = null;
    this.status = null;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.raw().writeFloat(location.yaw());
    buffer.raw().writeFloat(location.pitch());
    buffer.raw().writeBoolean(true);
  }
}
