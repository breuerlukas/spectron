package de.lukasbreuer.bot.chat;

import de.lukasbreuer.bot.authentication.Authentication;
import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketChatMessage;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class ChatMessage {
  private final ConnectionClient client;
  private final Authentication authentication;
  private final UUID playerUuid;
  private final String message;

  public void send() throws Exception {
    var signature = createChatSignature();
    client.sendPacket(new PacketChatMessage(message, signature.timestamp(),
      signature.salt(), signature.sign()));
  }

  private ChatSignature createChatSignature() {
    return ChatSignature.create(authentication.playerCertificate().privateKey(),
      playerUuid, client.sessionId(), message);
  }
}
