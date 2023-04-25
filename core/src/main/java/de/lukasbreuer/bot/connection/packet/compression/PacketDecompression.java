package de.lukasbreuer.bot.connection.packet.compression;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.zip.Inflater;

@RequiredArgsConstructor(staticName = "create")
public final class PacketDecompression extends ByteToMessageDecoder {
  private final Inflater inflater = new Inflater();

  @Override
  protected void decode(
    ChannelHandlerContext context, ByteBuf buffer, List<Object> list
  ) {
    if (buffer.readableBytes() != 0) {
      var tempBuffer = PacketBuffer.create(buffer);
      var dataLength = tempBuffer.readVarInt();
      if (dataLength == 0) {
        list.add(tempBuffer.raw().readBytes(tempBuffer.raw().readableBytes()));
      } else {
        try {
          var decompressed = decompress(tempBuffer.raw(), dataLength);
          list.add(decompressed);
        } catch (Exception exception) {
          exception.printStackTrace();
        }
      }
    }
  }

  private ByteBuf decompress(ByteBuf buffer, int dataLength) throws Exception {
    var compressed = new byte[buffer.readableBytes()];
    buffer.readBytes(compressed);
    inflater.setInput(compressed);
    var uncompressed = new byte[dataLength];
    inflater.inflate(uncompressed);
    inflater.reset();
    return Unpooled.wrappedBuffer(uncompressed);
  }
}