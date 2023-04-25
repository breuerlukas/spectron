package de.lukasbreuer.bot.event.play;

import de.lukasbreuer.bot.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class ChatMessageEvent extends Event {
  private final UUID senderId;
  private final String message;
  private final long timestamp;
}
