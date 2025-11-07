package de.lukasbreuer.spectron.chat;

import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.security.PrivateKey;
import java.security.Signature;
import java.util.Random;
import java.util.UUID;

@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class ChatSignature {
  public static ChatSignature create(
    PrivateKey key, UUID senderUUID, UUID sessionUUID, String content
  ) {
    return new ChatSignature(key, senderUUID, sessionUUID,
      new Random().nextLong(), System.currentTimeMillis(), content);
  }

  private final PrivateKey key;
  private final UUID senderUUID;
  private final UUID sessionUUID;
  @Getter
  private final long salt;
  @Getter
  private final long timestamp;
  private final String content;
  private static int index = 0;

  public byte[] sign() throws Exception {
    var buffer = PacketBuffer.create(Unpooled.buffer(0));
    appendSignatureHeader(buffer);
    appendSignatureBody(buffer);
    buffer.raw().writeInt(0);
    index += 1;
    return sign(buffer.bytes());
  }

  private void appendSignatureHeader(PacketBuffer buffer) {
    buffer.raw().writeInt(1);
    buffer.writeUUID(senderUUID);
    buffer.writeUUID(sessionUUID);
    buffer.raw().writeInt(index);
  }

  private void appendSignatureBody(PacketBuffer buffer) {
    buffer.raw().writeLong(salt);
    buffer.raw().writeLong(timestamp / 1000);
    buffer.raw().writeInt(content.length());
    buffer.writeStringWithoutHeader(content);
  }

  private byte[] sign(byte[] buffer) throws Exception {
    var signature = Signature.getInstance("SHA256withRSA");
    signature.initSign(key);
    signature.update(buffer);
    return signature.sign();
  }
}
