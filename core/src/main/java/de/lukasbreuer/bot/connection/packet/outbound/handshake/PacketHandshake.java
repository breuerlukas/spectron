package de.lukasbreuer.bot.connection.packet.outbound.handshake;

import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;

public final class PacketHandshake extends PacketOutgoing {
  private final int protocolVersion;
  private final String serverAddress;
  private final short serverPort;

  public PacketHandshake(
    int protocolVersion, String serverAddress, short serverPort
  ) {
    super(0x00, ProtocolState.HANDSHAKE);
    this.protocolVersion = protocolVersion;
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
  }

  public PacketHandshake() {
    super(0x00, ProtocolState.HANDSHAKE);
    this.protocolVersion = -1;
    this.serverAddress = "";
    this.serverPort = -1;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeVarInt(protocolVersion);
    buffer.writeString(serverAddress);
    buffer.writeVarShort(serverPort);
    buffer.writeVarInt(2);
  }
}
