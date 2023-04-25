package de.lukasbreuer.bot.module.command.implementation;

import de.lukasbreuer.bot.authentication.Authentication;
import de.lukasbreuer.bot.chat.ChatSignature;
import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketChatCommand;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketChatMessage;
import de.lukasbreuer.bot.log.Log;
import de.lukasbreuer.bot.module.command.Command;

import java.util.Arrays;
import java.util.Random;
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
      return sendChatCommand(String.join(" ", arguments).replace("/", ""),
        Arrays.copyOfRange(arguments, 1, arguments.length));
    }
    return sendChatMessage(String.join(" ", arguments));
  }

  private boolean sendChatCommand(String command, String[] arguments) throws Exception {
    var salt = new Random().nextLong();
    var timestamp = System.currentTimeMillis();
    var commandArguments = new PacketChatCommand.Argument[arguments.length];
    for (var i = 0; i < arguments.length; i++) {
      var argument = arguments[i];
      var signature = ChatSignature.create(authentication.playerCertificate().privateKey(),
        playerUuid, client.sessionId(), salt, timestamp, argument);
      commandArguments[i] = new PacketChatCommand.Argument(argument,
        signature.sign());
    }
    client.sendPacket(new PacketChatCommand(command, timestamp, salt,
      commandArguments));
    log().info("You have successfully sent the message \"/" + command + "\"");
    return true;
  }

  private boolean sendChatMessage(String message) throws Exception {
    var signature = ChatSignature.create(authentication.playerCertificate().privateKey(),
      playerUuid, client.sessionId(), message);
    client.sendPacket(new PacketChatMessage(message, signature.timestamp(),
      signature.salt(), signature.sign()));
    log().info("You have successfully sent the message \"" + message + "\"");
    return true;
  }
}
