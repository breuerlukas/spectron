package de.lukasbreuer.bot.connection.packet.outbound.play;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;

import java.util.UUID;

public final class PacketPlayerSession extends PacketOutgoing {
  private final UUID sessionId;
  private final long expiration;
  private final byte[] publicKey;
  private final byte[] keySignature;

  public PacketPlayerSession(
    UUID sessionId, long expiration, byte[] publicKey, byte[] keySignature
  ) {
    super(0x06);
    this.sessionId = sessionId;
    this.expiration = expiration;
    this.publicKey = publicKey;
    this.keySignature = keySignature;
  }

  public PacketPlayerSession() {
    super(0x06);
    this.sessionId = null;
    this.expiration = -1;
    this.publicKey = null;
    this.keySignature = null;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeUUID(sessionId);
    buffer.raw().writeLong(expiration);
    buffer.writeVarInt(publicKey.length);
    buffer.raw().writeBytes(publicKey);
    buffer.writeVarInt(keySignature.length);
    buffer.raw().writeBytes(keySignature);
  }
}
