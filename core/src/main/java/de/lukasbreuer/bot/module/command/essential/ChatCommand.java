package de.lukasbreuer.bot.module.command.essential;

import de.lukasbreuer.bot.authentication.Authentication;
import de.lukasbreuer.bot.chat.ChatMessage;
import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.log.Log;
import de.lukasbreuer.bot.module.command.Command;

import java.util.UUID;

public final class ChatCommand extends Command {
  public static ChatCommand create(
    Log log, ConnectionClient client, Authentication authentication,
    UUID playerUuid
  ) {
    return new ChatCommand(log, client, authentication, playerUuid);
  }

  private final ConnectionClient client;
  private final Authentication authentication;
  private final UUID playerUuid;

  private ChatCommand(
    Log log, ConnectionClient client, Authentication authentication,
    UUID playerUuid
  ) {
    super(log, "chat", new String[] {"send", "enter"}, new String[] {"message"});
    this.client = client;
    this.authentication = authentication;
    this.playerUuid = playerUuid;
  }

  @Override
  public boolean execute(String[] arguments) throws Exception {
    if (arguments.length == 0){
      return false;
    }
    if (arguments[0].startsWith("/")) {
      return sendChatCommand(String.join(" ", arguments).replace("/", ""));
    }
    return sendChatMessage(String.join(" ", arguments));
  }

  private boolean sendChatCommand(String command) throws Exception {
    var chatCommand = de.lukasbreuer.bot.chat.ChatCommand.create(client,
      authentication, playerUuid, command);
    chatCommand.send();
    log().info("You have successfully sent the message \"/" + command + "\"");
    return true;
  }

  private boolean sendChatMessage(String message) throws Exception {
    var chatMessage = ChatMessage.create(client, authentication, playerUuid,
      message);
    chatMessage.send();
    log().info("You have successfully sent the message \"" + message + "\"");
    return true;
  }
}
