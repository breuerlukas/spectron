package de.lukasbreuer.bot.connection.packet.inbound.login;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;

public abstract class PacketIncomingLogin extends PacketIncoming {
  protected PacketIncomingLogin(int id) {
    super(id);
  }

  public abstract void read(PacketBuffer buffer) throws Exception;
}
