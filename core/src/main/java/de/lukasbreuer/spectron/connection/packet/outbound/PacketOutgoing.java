package de.lukasbreuer.spectron.connection.packet.outbound;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.Packet;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;

public abstract class PacketOutgoing extends Packet {
  protected PacketOutgoing(int id, ProtocolState protocolState) {
    super(id, protocolState);
  }

  public abstract void write(PacketBuffer buffer) throws Exception;
}
