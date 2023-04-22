package de.lukasbreuer.bot.connection.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(staticName = "create")
public final class PacketDecoder extends ByteToMessageDecoder {
  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
    if (byteBuf.readableBytes() >= 3) {
      var buffer = PacketBuffer.create(byteBuf);
      buffer.raw().markReaderIndex();
      int packetLength = buffer.readVarInt();
      if (buffer.raw().readableBytes() < packetLength) {
        buffer.raw().resetReaderIndex();
      } else {
        byte[] data = new byte[packetLength];
        buffer.raw().readBytes(data);
        list.add(Unpooled.wrappedBuffer(data));
      }
    }
  }
}

