package de.lukasbreuer.spectron.module.foundation;

import de.lukasbreuer.spectron.chat.ChatComponent;
import de.lukasbreuer.spectron.event.EventHook;
import de.lukasbreuer.spectron.event.Hook;
import de.lukasbreuer.spectron.event.play.DisconnectEvent;
import de.lukasbreuer.spectron.log.Log;
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