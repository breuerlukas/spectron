package de.lukasbreuer.bot.module.foundation;

import de.lukasbreuer.bot.chat.ChatComponent;
import de.lukasbreuer.bot.event.EventHook;
import de.lukasbreuer.bot.event.Hook;
import de.lukasbreuer.bot.event.play.DisconnectEvent;
import de.lukasbreuer.bot.log.Log;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

@RequiredArgsConstructor(staticName = "create")
public final class PlayerDisconnect implements Hook {
  private final Log log;

  @EventHook
  private void disconnect(DisconnectEvent event) {
    var chatComponent = ChatComponent.create(new JSONObject(event.reason()));
    chatComponent.interpret();
    log.info(chatComponent.toString());
  }
}