package de.lukasbreuer.spectron.connection.packet.inbound;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.Packet;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;

public abstract class PacketIncoming extends Packet {
  protected PacketIncoming(int id, ProtocolState protocolState) {
    super(id, protocolState);
  }

  public abstract void read(PacketBuffer buffer) throws Exception;
}
