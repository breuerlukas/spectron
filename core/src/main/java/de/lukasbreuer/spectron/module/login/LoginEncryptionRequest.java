package de.lukasbreuer.spectron.module.login;

import de.lukasbreuer.spectron.authentication.Authentication;
import de.lukasbreuer.spectron.connection.ConnectionClient;
import de.lukasbreuer.spectron.connection.packet.cryptography.CryptographyStatus;
import de.lukasbreuer.spectron.connection.packet.cryptography.PacketDecryption;
import de.lukasbreuer.spectron.connection.packet.cryptography.PacketEncryption;
import de.lukasbreuer.spectron.connection.packet.outbound.login.PacketEncryptionResponse;
import de.lukasbreuer.spectron.event.EventHook;
import de.lukasbreuer.spectron.event.Hook;
import de.lukasbreuer.spectron.event.login.EncryptionRequestEvent;
import lombok.RequiredArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class LoginEncryptionRequest implements Hook {
  private final ConnectionClient client;
  private final Authentication authentication;

  @EventHook
  private void encryptionRequest(EncryptionRequestEvent event) throws Exception {
    var sharedSecret = generateSecret(16);
    var serverHash = ServerHash.build(event.serverId(), sharedSecret, event.publicKey());
    authentication.authenticate(serverHash).thenAccept(success ->
      finishEncryptionRequest(event.publicKey(), event.verifyToken(), sharedSecret));
  }

  private byte[] generateSecret(int length) {
    return UUID.randomUUID().toString().replace("-", "").substring(0, length)
      .getBytes(StandardCharsets.US_ASCII);
  }

  private void finishEncryptionRequest(
    byte[] rawPublicKey, byte[] rawVerifyToken, byte[] sharedSecret
  ) {
    sendEncryptionResponsePacket(rawPublicKey, rawVerifyToken, sharedSecret);
    var secretKey = new SecretKeySpec(sharedSecret, "AES");
    var pipeline = client.channel().pipeline();
    pipeline.addBefore("packet-encoder", "packet-encryption",
      PacketEncryption.create(secretKey));
    pipeline.addBefore("packet-decoder", "packet-decryption",
      PacketDecryption.create(secretKey));
    client.cryptographyStatus(CryptographyStatus.ENABLED);
  }

  private void sendEncryptionResponsePacket(
    byte[] rawPublicKey, byte[] rawVerifyToken, byte[] sharedSecret
  ) {
    try {
      var publicKey = createPublicKey(rawPublicKey);
      var encryptedSharedSecret = encrypt(sharedSecret, publicKey);
      var encryptedVerifyToken = encrypt(rawVerifyToken, publicKey);
      client.sendPacket(new PacketEncryptionResponse(encryptedSharedSecret,
        encryptedVerifyToken));
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private PublicKey createPublicKey(byte[] publicKeyBytes) throws Exception {
    var keySpec = new X509EncodedKeySpec(publicKeyBytes);
    var keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePublic(keySpec);
  }

  private byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
    var cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    return cipher.doFinal(data);
  }
}
