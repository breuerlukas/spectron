package de.lukasbreuer.bot.module.login;

import de.lukasbreuer.bot.authentication.Authentication;
import de.lukasbreuer.bot.authentication.PlayerCertificate;
import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketClientInformation;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketPlayerSession;
import de.lukasbreuer.bot.event.EventHook;
import de.lukasbreuer.bot.event.Hook;
import de.lukasbreuer.bot.event.HookPriority;
import de.lukasbreuer.bot.event.login.LoginSuccessEvent;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class LoginSuccess implements Hook {
  private final ConnectionClient client;
  private final Authentication authentication;

  @EventHook(priority = HookPriority.FIRST)
  private void loginSuccess(LoginSuccessEvent event) {
    client.protocolState(ProtocolState.PLAY);
    System.out.println("Login success");
    System.out.println("Username: " + event.username());
    System.out.println("UUID: " + event.uuid());
    var sessionId = UUID.randomUUID();
    client.sessionId(sessionId);
    sendClientInformationPacket();
    authentication.certificatePlayer().thenAccept(certificate ->
      sendPlayerSessionPacket(sessionId, certificate)).thenAccept(value ->
      sendClientInformationPacket());
  }

  private void sendClientInformationPacket() {
    client.sendPacket(new PacketClientInformation("de_DE", 16, 0, true,
      (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40), 1, false, true));
  }

  private void sendPlayerSessionPacket(UUID sessionId, PlayerCertificate certificate) {
    client.sendPacket(new PacketPlayerSession(sessionId,
      certificate.expiration(), certificate.publicKey().getEncoded(),
      ByteBuffer.wrap(Base64.getDecoder().decode(certificate.publicKeySignature()))
        .array()));
  }
}
