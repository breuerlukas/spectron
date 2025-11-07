package de.lukasbreuer.spectron.connection.packet.inbound.login;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketEncryptionRequest extends PacketIncoming {
  private String serverId;
  private byte[] publicKey;
  private byte[] verifyToken;

  public PacketEncryptionRequest() {
    super(0x01, ProtocolState.LOGIN);
  }

  @Override
  public void read(PacketBuffer buffer) {
    serverId = buffer.readString();
    var publicKeyLength = buffer.readVarInt();
    publicKey = new byte[publicKeyLength];
    buffer.raw().readBytes(publicKeyLength).readBytes(publicKey);
    var verifyTokenLength = buffer.readVarInt();
    verifyToken = new byte[verifyTokenLength];
    buffer.raw().readBytes(verifyTokenLength).readBytes(verifyToken);
  }
}