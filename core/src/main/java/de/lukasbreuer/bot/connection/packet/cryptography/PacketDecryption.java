package de.lukasbreuer.bot.connection.packet.cryptography;

import de.lukasbreuer.bot.connection.ConnectionClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

public final class PacketDecryption extends ByteToMessageDecoder {
  public static PacketDecryption create(ConnectionClient client) {
    return new PacketDecryption(client);
  }

  private final ConnectionClient client;
  private final JavaCipher cipher;

  private PacketDecryption(ConnectionClient client) {
    this.client = client;
    cipher = new JavaCipher();
    try {
      cipher.init(false, new SecretKeySpec(client.key(), "AES"));
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  @Override
  protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) throws Exception {
    if (client.login()) {
      return;
    }
    list.add(cipher.cipher(context, buffer));
  }
}
