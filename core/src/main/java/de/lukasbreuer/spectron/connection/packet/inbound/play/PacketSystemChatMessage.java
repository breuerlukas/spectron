package de.lukasbreuer.spectron.connection.packet.inbound.play;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketSystemChatMessage extends PacketIncoming {
  public enum DisplayType {
    CHAT,
    ACTIONBAR
  }

  private String content;
  private DisplayType displayType;

  public PacketSystemChatMessage() {
    super(0x64, ProtocolState.PLAY);
  }

  @Override
  public void read(PacketBuffer buffer) {
    content = buffer.readString();
    displayType = buffer.raw().readBoolean() ?
      DisplayType.ACTIONBAR : DisplayType.CHAT;
  }
}
