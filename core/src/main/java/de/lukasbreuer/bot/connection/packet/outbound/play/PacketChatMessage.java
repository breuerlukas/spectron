package de.lukasbreuer.bot.connection.packet.outbound.play;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;

import java.util.BitSet;

public final class PacketChatMessage extends PacketOutgoing {
  private final String message;
  private final long timestamp;
  private final long salt;
  private final byte[] signature;

  public PacketChatMessage(
    String message, long timestamp, long salt, byte[] signature
  ) {
    super(0x05);
    this.message = message;
    this.timestamp = timestamp;
    this.salt = salt;
    this.signature = signature;
  }

  public PacketChatMessage() {
    super(0x05);
    this.message = "";
    this.timestamp = -1;
    this.salt = -1;
    this.signature = null;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeString(message);
    buffer.raw().writeLong(timestamp);
    buffer.raw().writeLong(salt);
    buffer.raw().writeBoolean(true);
    buffer.raw().writeBytes(signature);
    buffer.writeVarInt(0);
    buffer.writeBitSet(new BitSet(), 20);
  }
}
