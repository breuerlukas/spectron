package de.lukasbreuer.bot.connection.packet.inbound.play;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;

public abstract class PacketIncomingPlay extends PacketIncoming {
  protected PacketIncomingPlay(int id) {
    super(id);
  }

  public abstract void read(PacketBuffer buffer) throws Exception;
}
