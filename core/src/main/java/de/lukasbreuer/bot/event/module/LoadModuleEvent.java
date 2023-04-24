package de.lukasbreuer.bot.event.module;

import de.lukasbreuer.bot.event.Event;
import de.lukasbreuer.bot.module.Module;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class LoadModuleEvent extends Event {
  private final Module module;
}
