package de.lukasbreuer.spectron.connection.packet.outbound.play;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.outbound.PacketOutgoing;

public final class PacketSwingArm extends PacketOutgoing {
  public PacketSwingArm() {
    super(0x2F, ProtocolState.PLAY);
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeVarInt(0);
  }
}
