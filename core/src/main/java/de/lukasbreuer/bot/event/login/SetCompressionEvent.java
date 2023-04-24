package de.lukasbreuer.bot.event.login;

import de.lukasbreuer.bot.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class SetCompressionEvent extends Event {
  private final int threshold;
}
