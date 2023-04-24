package de.lukasbreuer.bot.connection.packet.outbound.play;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;

public final class PacketKeepAliveResponse extends PacketOutgoing {
  private final long number;

  public PacketKeepAliveResponse(int number) {
    super(0x12);
    this.number = number;
  }

  public PacketKeepAliveResponse() {
    super(0x12);
    this.number = -1;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.raw().writeLong(number);
  }
}
