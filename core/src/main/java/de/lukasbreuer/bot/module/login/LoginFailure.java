package de.lukasbreuer.bot.module.login;

import de.lukasbreuer.bot.chat.ChatComponent;
import de.lukasbreuer.bot.event.EventHook;
import de.lukasbreuer.bot.event.Hook;
import de.lukasbreuer.bot.event.login.LoginDisconnectEvent;
import de.lukasbreuer.bot.log.Log;
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
