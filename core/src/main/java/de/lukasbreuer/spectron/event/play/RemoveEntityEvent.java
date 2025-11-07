package de.lukasbreuer.spectron.event.play;


import de.lukasbreuer.spectron.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class RemoveEntityEvent extends Event {
  private final int entityId;
}
