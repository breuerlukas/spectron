package de.lukasbreuer.spectron.event.module;

import de.lukasbreuer.spectron.event.Event;
import de.lukasbreuer.spectron.module.Module;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class LoadModuleEvent extends Event {
  private final Module module;
}
