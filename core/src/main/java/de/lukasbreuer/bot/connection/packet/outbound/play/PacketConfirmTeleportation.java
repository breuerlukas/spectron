package de.lukasbreuer.bot.connection.packet.outbound.play;

import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;

public final class PacketConfirmTeleportation extends PacketOutgoing {
  private final int teleportId;

  public PacketConfirmTeleportation(int teleportId) {
    super(0x00, ProtocolState.PLAY);
    this.teleportId = teleportId;
  }

  public PacketConfirmTeleportation() {
    super(0x00, ProtocolState.PLAY);
    this.teleportId = -1;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeVarInt(teleportId);
  }
}
