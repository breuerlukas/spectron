package de.lukasbreuer.bot.connection.packet.inbound.login;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketLoginDisconnect extends PacketIncomingLogin {
  private String reason;

  public PacketLoginDisconnect() {
    super(0x00);
  }

  @Override
  public void read(PacketBuffer buffer) {
    reason = buffer.readString();
  }
}