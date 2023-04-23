package de.lukasbreuer.bot.connection.packet.cryptography;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import java.util.List;

@RequiredArgsConstructor(staticName = "create")
public final class PacketDecryption extends ByteToMessageDecoder {
  public static PacketDecryption create(SecretKey key) {
    var cipher = Cipher.create(key, CipherMode.DECRYPTION);
    cipher.initialize();
    return create(cipher);
  }

  private final Cipher cipher;
  private boolean a = false;
  private int c = 0;

  @Override
  protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) throws Exception {
    list.add(cipher.decrypt(context, buffer));
  }
}
