package de.lukasbreuer.bot.connection.packet.outbound;

import de.lukasbreuer.bot.connection.packet.Packet;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;

public abstract class PacketOutgoing extends Packet {
  protected PacketOutgoing(int id) {
    super(id);
  }

  public abstract void write(PacketBuffer buffer) throws Exception;
}
