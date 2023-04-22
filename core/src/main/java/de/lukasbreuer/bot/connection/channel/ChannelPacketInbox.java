package de.lukasbreuer.bot.connection.channel;

import de.lukasbreuer.bot.authentication.Authentication;
import de.lukasbreuer.bot.authentication.Credentials;
import de.lukasbreuer.bot.authentication.MinecraftPlayerCertificate;
import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.PacketRegistry;
import de.lukasbreuer.bot.connection.packet.compression.PacketCompression;
import de.lukasbreuer.bot.connection.packet.compression.PacketDecompression;
import de.lukasbreuer.bot.connection.packet.cryptography.PacketDecryption;
import de.lukasbreuer.bot.connection.packet.cryptography.PacketEncryption;
import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;
import de.lukasbreuer.bot.connection.packet.inbound.login.PacketDisconnect;
import de.lukasbreuer.bot.connection.packet.inbound.login.PacketEncryptionRequest;
import de.lukasbreuer.bot.connection.packet.inbound.login.PacketLoginSuccess;
import de.lukasbreuer.bot.connection.packet.inbound.login.PacketSetCompression;
import de.lukasbreuer.bot.connection.packet.outbound.login.PacketEncryptionResponse;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketChatCommand;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketChatMessage;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketClientInformation;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketPlayerSession;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.function.Consumer;

@RequiredArgsConstructor(staticName = "create")
public final class ChannelPacketInbox extends SimpleChannelInboundHandler<PacketIncoming> {
  private final ConnectionClient client;
  private final PacketRegistry packetRegistry;
  private Authentication authentication;

  @Override
  protected void channelRead0(ChannelHandlerContext context, PacketIncoming packet) throws Exception {
    if (client.login()) {
      return;
    }
    System.out.println("Received packet " + packet.getClass().toString());
    if (packet instanceof PacketDisconnect disconnectPacket) {
      System.out.println("Disconnected: " + disconnectPacket.reason());
    }
    if (packet instanceof PacketEncryptionRequest encryptionRequestPacket) {
      System.out.println("Server id: " + encryptionRequestPacket.serverId());
      var sharedSecret = generateSecret(16).getBytes(StandardCharsets.US_ASCII);
      MessageDigest crypt = MessageDigest.getInstance("SHA-1");
      crypt.reset();
      crypt.update(encryptionRequestPacket.serverId().getBytes(StandardCharsets.US_ASCII));
      crypt.update(sharedSecret);
      crypt.update(encryptionRequestPacket.publicKey());
      var serverHash = byteToHex(crypt.digest());
      System.out.println("Server hash: " + serverHash);
      authentication = Authentication.create(Credentials.create("lukas.breuer2004@gmail.com",
        "lukas2004510tilobreuer"), UUID.fromString("0ffbc827-0a24-4890-a3a4-ea7e19dd1a14"), serverHash);
      authentication.authenticate().thenAccept(success ->
        finishEncryptionRequest(encryptionRequestPacket.publicKey(),
          encryptionRequestPacket.verifyToken(), sharedSecret, success));
    }
    if (packet instanceof PacketSetCompression setCompressionPacket) {
      System.out.println("Compression threshold: " + setCompressionPacket.threshold());
      client.compressionEnabled(true);
      try {
        var pipeline = client.channel().pipeline();
        pipeline.addBefore("packet-encoder", "packet-compression",
          PacketCompression.create());
        pipeline.addAfter("packet-decoder", "packet-decompression",
          PacketDecompression.create());
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
    if (packet instanceof PacketLoginSuccess loginSuccess) {
      client.login(true);
      System.out.println("Login success");
      System.out.println("Username: " + loginSuccess.username());
      System.out.println("UUID: " + loginSuccess.uuid());
      MinecraftPlayerCertificate.create(authentication.accessToken()).send()
        .thenAccept(this::sendMessage);
    }
  }

  private void sendMessage(MinecraftPlayerCertificate.CertificateResult certificateResult) {
    try {
      client.sendPacket(new PacketClientInformation("de_DE", 16, 0, true,
        (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40), 1, false, true));
      var sessionId = UUID.randomUUID();
      client.sendPacket(new PacketPlayerSession(sessionId,
        certificateResult.expiration(), certificateResult.publicKey().getEncoded(),
        ByteBuffer.wrap(Base64.getDecoder().decode(certificateResult.publicKeySignature())).array()));
      var timestamp = System.currentTimeMillis();
      var salt = new Random().nextLong();
      var message = "1";
      var signature = signChatMessage(certificateResult, message,
        UUID.fromString("0ffbc827-0a24-4890-a3a4-ea7e19dd1a14"), sessionId, 0, salt, timestamp, true);
      client.sendPacket(new PacketChatMessage(message, timestamp, salt, signature));
      var timestamp1 = System.currentTimeMillis();
      var salt1 = new Random().nextLong();
      var message1 = "2";
      var signature1 = signChatMessage(certificateResult, message1,
        UUID.fromString("0ffbc827-0a24-4890-a3a4-ea7e19dd1a14"), sessionId, 1, salt1, timestamp1, true);
      client.sendPacket(new PacketChatMessage(message1, timestamp1, salt1, signature1));
      var timestamp2 = System.currentTimeMillis();
      var salt2 = new Random().nextLong();
      var command = "gamemode";
      var arguments = new PacketChatCommand.Argument[] {new PacketChatCommand.Argument(command,
        signChatMessage(certificateResult, command, UUID.fromString("0ffbc827-0a24-4890-a3a4-ea7e19dd1a14"),
          sessionId, 0, salt2, timestamp2, false))};
      client.sendPacket(new PacketChatCommand(command, timestamp2, salt2, arguments));
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private byte[] signChatMessage(
    MinecraftPlayerCertificate.CertificateResult certificateResult, String content,
    UUID senderUUID, UUID sessionUUID, int index, long salt, long timestamp, boolean isChatMessage
  ) {
    var buffer = PacketBuffer.create(Unpooled.buffer(0));
    buffer.raw().writeInt(1);
    buffer.writeUUID(senderUUID);
    buffer.writeUUID(sessionUUID);
    buffer.raw().writeInt(index);
    buffer.raw().writeLong(salt);
    buffer.raw().writeLong(timestamp / 1000);
    buffer.raw().writeInt(content.length());
    buffer.writeStringWithoutHeader(content);
    buffer.raw().writeInt(0);
    return sign(certificateResult.privateKey(), signature -> {
      try {
        signature.update(buffer.bytes());
      } catch (SignatureException e) {
        e.printStackTrace();
      }
    });
  }

  private byte[] sign(PrivateKey privateKey, Consumer<Signature> signatureConsumer) {
    try {
      Signature signature = Signature.getInstance("SHA256withRSA");
      signature.initSign(privateKey);
      Objects.requireNonNull(signature);
      signatureConsumer.accept(signature);
      return signature.sign();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void finishEncryptionRequest(
    byte[] rawPublicKey, byte[] rawVerifyToken, byte[] sharedSecret, boolean success
  ) {
    System.out.println("Finished authentication, success: " + success);
    System.out.println("Finish encryption request");
    var publicKey = createPublicKey(rawPublicKey);
    client.key(sharedSecret);
    var encryptedSharedSecret = encrypt(new String(sharedSecret), publicKey);
    var encryptedVerifyToken = encrypt(new String(rawVerifyToken), publicKey);
    client.sendPacket(new PacketEncryptionResponse(encryptedSharedSecret, encryptedVerifyToken));
    client.channel().pipeline().addBefore("packet-encoder", "packet-encryption",
      PacketEncryption.create(client));
    client.channel().pipeline().addBefore("packet-decoder", "packet-decryption",
      PacketDecryption.create(client));
  }

  private String generateSecret(int length) {
    return UUID.randomUUID().toString().replace("-", "").substring(0, length);
  }

  private PublicKey createPublicKey(byte[] publicKeyBytes){
    try{
      var keySpec = new X509EncodedKeySpec(publicKeyBytes);
      var keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePublic(keySpec);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return null;
  }

  private byte[] encrypt(String data, PublicKey publicKey) {
    return encrypt(data.getBytes(), publicKey);
  }

  private byte[] encrypt(byte[] data, PublicKey publicKey) {
    try {
      var cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      return cipher.doFinal(data);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return null;
  }

  private String byteToHex(byte[] hash) {
    Formatter formatter = new Formatter();
    for (byte b : hash)
    {
      formatter.format("%02x", b);
    }
    String result = formatter.toString();
    formatter.close();
    return result;
  }
}

