package de.lukasbreuer.spectron.connection.packet.cryptography;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;

@RequiredArgsConstructor(staticName = "create")
public final class PacketEncryption extends MessageToByteEncoder<ByteBuf> {
  public static PacketEncryption create(SecretKey key) {
    var cipher = Cipher.create(key, CipherMode.ENCRYPTION);
    cipher.initialize();
    return create(cipher);
  }

  private final Cipher cipher;

  @Override
  protected void encode(
    ChannelHandlerContext context, ByteBuf incomingBuffer,
    ByteBuf outgoingBuffer
  ) throws Exception {
    cipher.encrypt(incomingBuffer, outgoingBuffer);
  }
}
