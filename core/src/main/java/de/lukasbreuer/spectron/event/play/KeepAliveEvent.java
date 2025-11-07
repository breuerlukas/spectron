package de.lukasbreuer.spectron.event.play;

import de.lukasbreuer.spectron.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class KeepAliveEvent extends Event {
  private final long number;
}
