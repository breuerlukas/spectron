package de.lukasbreuer.spectron.chat;

import de.lukasbreuer.spectron.authentication.Authentication;
import de.lukasbreuer.spectron.connection.ConnectionClient;
import de.lukasbreuer.spectron.connection.packet.outbound.play.PacketChatCommand;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class ChatCommand {
  private final ConnectionClient client;
  private final Authentication authentication;
  private final UUID playerUuid;
  private final String command;

  public void send() throws Exception {
    var salt = new Random().nextLong();
    var timestamp = System.currentTimeMillis();
    var arguments = command.split(" ");
    arguments = Arrays.copyOfRange(arguments, 1, arguments.length);
    var commandArguments = new PacketChatCommand.Argument[arguments.length];
    for (var i = 0; i < arguments.length; i++) {
      commandArguments[i] = createCommandArgument(salt, timestamp, arguments[i]);
    }
    client.sendPacket(new PacketChatCommand(command, timestamp, salt,
      commandArguments));
  }

  private PacketChatCommand.Argument createCommandArgument(
    long salt, long timestamp, String argument
  ) throws Exception {
    var signature = ChatSignature.create(authentication.playerCertificate().privateKey(),
      playerUuid, client.sessionId(), salt, timestamp, argument);
    return new PacketChatCommand.Argument(argument, signature.sign());
  }
}
