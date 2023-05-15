package de.lukasbreuer.bot.connection.packet.inbound.play;

import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class PacketSpawnPlayer extends PacketIncoming {
  private UUID playerUuid;
  private int entityId;

  public PacketSpawnPlayer() {
    super(0x03, ProtocolState.PLAY);
  }

  @Override
  public void read(PacketBuffer buffer) {
    entityId = buffer.readVarInt();
    playerUuid = buffer.readUUID();
  }
}
