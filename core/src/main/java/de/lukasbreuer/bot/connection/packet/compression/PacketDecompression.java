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
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
    if (byteBuf.readableBytes() != 0) {
      var buffer = PacketBuffer.create(byteBuf);
      var dataLength = buffer.readVarInt();
      if (dataLength == 0) {
        list.add(buffer.raw().readBytes(buffer.raw().readableBytes()));
      } else {
        try {
          var decompressed = decompress(buffer.raw(), dataLength);
          list.add(decompressed);
        } catch (Exception exception) {
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