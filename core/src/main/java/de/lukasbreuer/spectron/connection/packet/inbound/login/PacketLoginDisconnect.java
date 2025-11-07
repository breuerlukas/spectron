package de.lukasbreuer.spectron.connection.packet.inbound.login;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketLoginDisconnect extends PacketIncoming {
  private String reason;

  public PacketLoginDisconnect() {
    super(0x00, ProtocolState.LOGIN);
  }

  @Override
  public void read(PacketBuffer buffer) {
    reason = buffer.readString();
  }
}