package de.lukasbreuer.spectron.connection.packet.inbound.login;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketSetCompression extends PacketIncoming {
  private int threshold;

  public PacketSetCompression() {
    super(0x03, ProtocolState.LOGIN);
  }

  @Override
  public void read(PacketBuffer buffer) {
    threshold = buffer.readVarInt();
  }
}