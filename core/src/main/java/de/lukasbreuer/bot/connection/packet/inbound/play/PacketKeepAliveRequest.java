package de.lukasbreuer.bot.connection.packet.inbound.play;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketKeepAliveRequest extends PacketIncomingPlay {
  private long number;

  public PacketKeepAliveRequest() {
    super(0x23);
  }

  @Override
  public void read(PacketBuffer buffer) {
    number = buffer.raw().readLong();
  }
}
