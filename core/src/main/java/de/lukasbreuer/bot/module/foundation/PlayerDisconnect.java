package de.lukasbreuer.bot.module.foundation;

import de.lukasbreuer.bot.event.EventHook;
import de.lukasbreuer.bot.event.Hook;
import de.lukasbreuer.bot.event.play.DisconnectEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class PlayerDisconnect implements Hook {
  @EventHook
  private void disconnect(DisconnectEvent event) {
    System.out.println("Disconnected: " + event.reason());
  }
}