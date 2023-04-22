package de.lukasbreuer.bot.connection.packet.inbound;

import de.lukasbreuer.bot.connection.packet.Packet;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;

public abstract class PacketIncoming extends Packet {
  protected PacketIncoming(int id) {
    super(id);
  }

  public abstract void read(PacketBuffer buffer) throws Exception;
}
