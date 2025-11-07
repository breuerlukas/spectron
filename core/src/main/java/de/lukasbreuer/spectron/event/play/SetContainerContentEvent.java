package de.lukasbreuer.spectron.event.play;

import de.lukasbreuer.spectron.event.Event;
import de.lukasbreuer.spectron.item.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class SetContainerContentEvent extends Event {
  private final byte windowId;
  private final int lastStateId;
  private final Map<Integer, Item> content;
}
