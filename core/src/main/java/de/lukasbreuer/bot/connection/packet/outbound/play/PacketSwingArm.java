package de.lukasbreuer.bot.connection.packet.outbound.play;

import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;

public final class PacketSwingArm extends PacketOutgoing {
  public PacketSwingArm() {
    super(0x2F, ProtocolState.PLAY);
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeVarInt(0);
  }
}
