package de.lukasbreuer.spectron.module.login;

import de.lukasbreuer.spectron.authentication.Authentication;
import de.lukasbreuer.spectron.authentication.PlayerCertificate;
import de.lukasbreuer.spectron.connection.ConnectionClient;
import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.outbound.play.PacketClientInformation;
import de.lukasbreuer.spectron.connection.packet.outbound.play.PacketPlayerSession;
import de.lukasbreuer.spectron.event.EventHook;
import de.lukasbreuer.spectron.event.Hook;
import de.lukasbreuer.spectron.event.HookPriority;
import de.lukasbreuer.spectron.event.login.LoginSuccessEvent;
import de.lukasbreuer.spectron.log.Log;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class LoginSuccess implements Hook {
  private final ConnectionClient client;
  private final Log log;
  private final Authentication authentication;

  @EventHook(priority = HookPriority.FIRST)
  private void loginSuccess(LoginSuccessEvent event) {
    client.protocolState(ProtocolState.PLAY);
    log.info("");
    log.info("The login was successful");
    log.info("Username: " + event.username());
    log.info("UUID: " + event.uuid());
    log.info("");
    var sessionId = UUID.randomUUID();
    client.sessionId(sessionId);
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
