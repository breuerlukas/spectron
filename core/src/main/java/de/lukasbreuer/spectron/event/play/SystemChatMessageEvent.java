package de.lukasbreuer.spectron.event.play;

import de.lukasbreuer.spectron.connection.packet.inbound.play.PacketSystemChatMessage;
import de.lukasbreuer.spectron.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class SystemChatMessageEvent extends Event {
  private final String content;
  private final PacketSystemChatMessage.DisplayType displayType;
}
