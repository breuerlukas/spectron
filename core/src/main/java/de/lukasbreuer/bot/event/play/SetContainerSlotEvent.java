package de.lukasbreuer.bot.event.play;

import de.lukasbreuer.bot.event.Event;
import de.lukasbreuer.bot.item.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class SetContainerSlotEvent extends Event {
  private final byte windowId;
  private final int lastStateId;
  private final short slot;
  private final Item item;
}
