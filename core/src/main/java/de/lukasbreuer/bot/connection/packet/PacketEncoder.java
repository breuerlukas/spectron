package de.lukasbreuer.bot.connection.packet;

import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public class PacketEncoder extends MessageToByteEncoder<PacketOutgoing> {
  private final ConnectionClient client;

  @Override
  protected void encode(
    ChannelHandlerContext context, PacketOutgoing packet, ByteBuf byteBuf
  ) throws Exception {
    System.out.println("ENCODE1");
    var buffer = PacketBuffer.create(Unpooled.buffer(0));
    if (!client.compressionEnabled()) {
      var tempBuffer = PacketBuffer.create(Unpooled.buffer(0));
      tempBuffer.writeVarInt(packet.id());
      packet.write(tempBuffer);
      buffer.writeVarInt(tempBuffer.raw().readableBytes());
    }
    buffer.writeVarInt(packet.id());
    packet.write(buffer);
    byteBuf.writeBytes(buffer.raw());
  }
}
