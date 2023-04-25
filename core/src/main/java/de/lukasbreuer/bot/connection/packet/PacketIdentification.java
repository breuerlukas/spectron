package de.lukasbreuer.bot.connection.packet;

import de.lukasbreuer.bot.connection.ConnectionClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(staticName = "create")
public final class PacketIdentification extends ByteToMessageDecoder {
  private final ConnectionClient client;
  private final PacketRegistry packetRegistry;

  @Override
  protected void decode(
    ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list
  ) {
    try {
      var buffer = PacketBuffer.create(byteBuf);
      int id = buffer.readVarInt();
      var packetClass = packetRegistry.findIncomingPacket(id,
        client.protocolState());
      if (packetClass.isEmpty()) {
        buffer.raw().skipBytes(buffer.raw().readableBytes());
        return;
      }
      var packet = packetClass.get().getConstructor().newInstance();
      packet.read(buffer);
      list.add(packet);
    } catch (Exception exception) {
    }
  }
}