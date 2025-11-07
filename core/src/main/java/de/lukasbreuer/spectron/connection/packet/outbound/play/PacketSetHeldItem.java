package de.lukasbreuer.spectron.connection.packet.outbound.play;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.outbound.PacketOutgoing;

public final class PacketSetHeldItem extends PacketOutgoing {
  private final short slot;

  public PacketSetHeldItem(short slot) {
    super(0x28, ProtocolState.PLAY);
    this.slot = slot;
  }

  public PacketSetHeldItem() {
    super(0x28, ProtocolState.PLAY);
    this.slot = -1;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.raw().writeShort(slot);
  }
}
