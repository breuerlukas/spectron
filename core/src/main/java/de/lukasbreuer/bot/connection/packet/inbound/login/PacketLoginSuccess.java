package de.lukasbreuer.bot.connection.packet.inbound.login;

import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class PacketLoginSuccess extends PacketIncoming {
  private UUID uuid;
  private String username;

  public PacketLoginSuccess() {
    super(0x02, ProtocolState.LOGIN);
  }

  @Override
  public void read(PacketBuffer buffer) {
    uuid = buffer.readUUID();
    username = buffer.readString();
  }
}