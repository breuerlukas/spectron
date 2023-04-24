package de.lukasbreuer.bot.connection.packet.inbound.login;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketSetCompression extends PacketIncomingLogin {
  private int threshold;

  public PacketSetCompression() {
    super(0x03);
  }

  @Override
  public void read(PacketBuffer buffer) {
    threshold = buffer.readVarInt();
  }
}