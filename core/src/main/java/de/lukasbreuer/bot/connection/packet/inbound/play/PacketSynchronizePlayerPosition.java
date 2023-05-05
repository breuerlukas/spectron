package de.lukasbreuer.bot.connection.packet.inbound.play;

import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketSynchronizePlayerPosition extends PacketIncoming {
  private double x;
  private double y;
  private double z;
  private float yaw;
  private float pitch;
  private byte flags;
  private int teleportId;

  public PacketSynchronizePlayerPosition() {
    super(0x3C, ProtocolState.PLAY);
  }

  @Override
  public void read(PacketBuffer buffer) {
    x = buffer.raw().readDouble();
    y = buffer.raw().readDouble();
    z = buffer.raw().readDouble();
    yaw = buffer.raw().readFloat();
    pitch = buffer.raw().readFloat();
    flags = buffer.raw().readByte();
    teleportId = buffer.readVarInt();
  }
}
