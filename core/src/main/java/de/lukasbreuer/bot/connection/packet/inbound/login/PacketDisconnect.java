package de.lukasbreuer.bot.connection.packet.inbound.login;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketDisconnect extends PacketIncoming {
  private String reason;

  public PacketDisconnect() {
    super(0x00);
  }

  @Override
  public void read(PacketBuffer buffer) {
    reason = buffer.readString();
  }
}