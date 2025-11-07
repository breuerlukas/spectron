package de.lukasbreuer.spectron.connection.packet.inbound.play;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketKeepAliveRequest extends PacketIncoming {
  private long number;

  public PacketKeepAliveRequest() {
    super(0x23, ProtocolState.PLAY);
  }

  @Override
  public void read(PacketBuffer buffer) {
    number = buffer.raw().readLong();
  }
}
