package de.lukasbreuer.bot.connection.packet.outbound.login;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;

public final class PacketEncryptionResponse extends PacketOutgoing {
  private final byte[] sharedSecret;
  private final byte[] verifyToken;

  public PacketEncryptionResponse(byte[] sharedSecret, byte[] verifyToken) {
    super(0x01);
    this.sharedSecret = sharedSecret;
    this.verifyToken = verifyToken;
  }

  public PacketEncryptionResponse() {
    super(0x01);
    this.sharedSecret = new byte[0];
    this.verifyToken = new byte[0];
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeVarInt(sharedSecret.length);
    buffer.raw().writeBytes(sharedSecret);
    buffer.writeVarInt(verifyToken.length);
    buffer.raw().writeBytes(verifyToken);
  }
}
