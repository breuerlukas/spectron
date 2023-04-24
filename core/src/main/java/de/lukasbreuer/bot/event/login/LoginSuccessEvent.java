package de.lukasbreuer.bot.event.login;

import de.lukasbreuer.bot.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class LoginSuccessEvent extends Event {
  private final UUID uuid;
  private final String username;
}
