package de.lukasbreuer.spectron.connection.packet.outbound.play;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.outbound.PacketOutgoing;

public final class PacketKeepAliveResponse extends PacketOutgoing {
  private final long number;

  public PacketKeepAliveResponse(long number) {
    super(0x12, ProtocolState.PLAY);
    this.number = number;
  }

  public PacketKeepAliveResponse() {
    super(0x12, ProtocolState.PLAY);
    this.number = -1;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.raw().writeLong(number);
  }
}
