package de.lukasbreuer.bot.module.login;

import de.lukasbreuer.bot.event.EventHook;
import de.lukasbreuer.bot.event.Hook;
import de.lukasbreuer.bot.event.login.LoginDisconnectEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class LoginFailure implements Hook {
  @EventHook
  private void disconnect(LoginDisconnectEvent event) {
    System.out.println("Login disconnected: " + event.reason());
  }
}
