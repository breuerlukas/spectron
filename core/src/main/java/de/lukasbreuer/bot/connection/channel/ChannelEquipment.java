package de.lukasbreuer.bot.connection.channel;

import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.connection.packet.PacketDecoder;
import de.lukasbreuer.bot.connection.packet.PacketEncoder;
import de.lukasbreuer.bot.connection.packet.PacketIdentification;
import de.lukasbreuer.bot.connection.packet.PacketRegistry;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class ChannelEquipment extends ChannelInitializer<Channel> {
  private final ConnectionClient client;
  private final PacketRegistry packetRegistry;

  @Override
  protected void initChannel(Channel channel) {
    channel.pipeline().addLast("packet-encoder", PacketEncoder.create(client));
    channel.pipeline().addLast("packet-decoder", PacketDecoder.create());
    channel.pipeline().addLast("packet-identification", PacketIdentification.create(packetRegistry));
    channel.pipeline().addLast("packet-inbox", ChannelPacketInbox.create(client, packetRegistry));
  }
}