package de.lukasbreuer.spectron.module.login;

import de.lukasbreuer.spectron.chat.ChatComponent;
import de.lukasbreuer.spectron.event.EventHook;
import de.lukasbreuer.spectron.event.Hook;
import de.lukasbreuer.spectron.event.login.LoginDisconnectEvent;
import de.lukasbreuer.spectron.log.Log;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

@RequiredArgsConstructor(staticName = "create")
public final class LoginFailure implements Hook {
  private final Log log;

  @EventHook
  private void disconnect(LoginDisconnectEvent event) {
    var chatComponent = ChatComponent.create(new JSONObject(event.reason()));
    chatComponent.interpret();
    log.info(chatComponent.toString());
  }
}
