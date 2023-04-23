package de.lukasbreuer.bot.connection.packet.compression;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public class PacketCompression extends MessageToByteEncoder<ByteBuf> {
  @Override
  protected void encode(
    ChannelHandlerContext context, ByteBuf incomingBuffer,
    ByteBuf outgoingBuffer
  ) {
    var tempBuffer = PacketBuffer.create(Unpooled.buffer(0));
    tempBuffer.writeVarInt(0);
    tempBuffer.raw().writeBytes(incomingBuffer);
    var buffer = PacketBuffer.create(Unpooled.buffer(0));
    buffer.writeVarInt(tempBuffer.raw().readableBytes());
    buffer.raw().writeBytes(tempBuffer.raw());
    outgoingBuffer.writeBytes(buffer.raw());
  }
}
