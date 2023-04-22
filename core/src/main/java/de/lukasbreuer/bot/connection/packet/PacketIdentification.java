package de.lukasbreuer.bot.connection.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(staticName = "create")
public final class PacketIdentification extends ByteToMessageDecoder {
  private final PacketRegistry packetRegistry;

  @Override
  protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) throws Exception {
    var buffer = PacketBuffer.create(byteBuf);
    int id = buffer.readVarInt();
    System.out.println("Id: " + id);
    var packetClass = packetRegistry.findIncomingPacketById(id);
    if (packetClass.isEmpty()) {
      return;
    }
    var packet = packetClass.get().getConstructor().newInstance();
    packet.read(buffer);
    list.add(packet);
  }
}