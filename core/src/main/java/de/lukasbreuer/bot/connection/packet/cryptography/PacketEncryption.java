package de.lukasbreuer.bot.connection.packet.cryptography;

import de.lukasbreuer.bot.connection.ConnectionClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.spec.SecretKeySpec;

public final class PacketEncryption extends MessageToByteEncoder<ByteBuf> {
  public static PacketEncryption create(ConnectionClient client) {
    return new PacketEncryption(client);
  }

  private final ConnectionClient client;
  private final JavaCipher cipher;

  private PacketEncryption(ConnectionClient client) {
    this.client = client;
    cipher = new JavaCipher();
    try {
      cipher.init(true, new SecretKeySpec(client.key(), "AES"));
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  @Override
  protected void encode(
    ChannelHandlerContext context, ByteBuf inByteBuf, ByteBuf outByteBuf
  ) throws Exception {
    System.out.println("ENCODE3");
    cipher.cipher(inByteBuf, outByteBuf);
  }
}
