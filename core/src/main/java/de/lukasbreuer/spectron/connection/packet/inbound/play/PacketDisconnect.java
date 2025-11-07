package de.lukasbreuer.spectron.connection.packet.inbound.play;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketDisconnect extends PacketIncoming {
  private String reason;

  public PacketDisconnect() {
    super(0x1A, ProtocolState.PLAY);
  }

  @Override
  public void read(PacketBuffer buffer) {
    reason = buffer.readString();
  }
}