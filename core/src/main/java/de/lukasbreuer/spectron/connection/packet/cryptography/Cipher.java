package de.lukasbreuer.spectron.connection.packet.cryptography;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

@RequiredArgsConstructor(staticName = "create")
public final class Cipher {
  private final SecretKey key;
  private final CipherMode mode;
  private javax.crypto.Cipher cipher;

  public void initialize() {
    try {
      cipher = javax.crypto.Cipher.getInstance("AES/CFB8/NoPadding");
      cipher.init(mode.identifier(), key,
        new IvParameterSpec(key.getEncoded()));
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public void encrypt(
    ByteBuf incomingBuffer, ByteBuf outgoingBuffer
  ) throws Exception {
    var readableBytes = incomingBuffer.readableBytes();
    var heapIn = bytesFromBuffer(incomingBuffer);
    var outputSize = cipher.getOutputSize(readableBytes);
    var heapOut = new byte[outputSize];
    outgoingBuffer.writeBytes(heapOut, 0, cipher.update(heapIn, 0,
      readableBytes, heapOut));
  }

  public ByteBuf decrypt(
    ChannelHandlerContext context, ByteBuf incomingBuffer
  ) throws Exception {
    var readableBytes = incomingBuffer.readableBytes();
    var heapIn = bytesFromBuffer(incomingBuffer);
    var heapOut = context.alloc().heapBuffer(cipher.getOutputSize(readableBytes));
    heapOut.writerIndex(cipher.update(heapIn, 0, readableBytes, heapOut.array(),
      heapOut.arrayOffset()));
    return heapOut;
  }

  private byte[] bytesFromBuffer(ByteBuf incoming) {
    var readableBytes = incoming.readableBytes();
    var heapIn = new byte[readableBytes];
    incoming.readBytes(heapIn, 0, readableBytes);
    return heapIn;
  }
}
