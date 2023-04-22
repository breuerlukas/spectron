package de.lukasbreuer.bot.connection.packet.outbound.login;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;

import java.util.UUID;

public final class PacketLoginStart extends PacketOutgoing {
  private final String username;
  private final UUID uuid;

  public PacketLoginStart(String username, UUID uuid) {
    super(0x00);
    this.username = username;
    this.uuid = uuid;
  }

  public PacketLoginStart() {
    super(0x00);
    this.username = "";
    this.uuid = UUID.randomUUID();
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeString(username);
    buffer.raw().writeBoolean(true);
    buffer.writeUUID(uuid);
  }
}
