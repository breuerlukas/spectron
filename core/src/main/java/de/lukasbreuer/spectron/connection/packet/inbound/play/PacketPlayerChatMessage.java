package de.lukasbreuer.spectron.connection.packet.inbound.play;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class PacketPlayerChatMessage extends PacketIncoming {
  private UUID senderId;
  private int index;
  private byte[] messageSignature;
  private String message;
  private long timestamp;
  private long salt;

  public PacketPlayerChatMessage() {
    super(0x35, ProtocolState.PLAY);
  }

  @Override
  public void read(PacketBuffer buffer) {
    senderId = buffer.readUUID();
    index = buffer.readVarInt();
    if (buffer.raw().readBoolean()) {
      messageSignature = PacketBuffer.create(buffer.raw().readBytes(256)).bytes();
    }
    message = buffer.readString();
    timestamp = buffer.raw().readLong();
    salt = buffer.raw().readLong();
  }
}
