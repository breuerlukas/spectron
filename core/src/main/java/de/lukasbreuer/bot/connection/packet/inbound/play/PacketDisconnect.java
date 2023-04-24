package de.lukasbreuer.bot.connection.packet.inbound.play;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketDisconnect extends PacketIncomingPlay {
  private String reason;

  public PacketDisconnect() {
    super(0x1A);
  }

  @Override
  public void read(PacketBuffer buffer) {
    reason = buffer.readString();
  }
}