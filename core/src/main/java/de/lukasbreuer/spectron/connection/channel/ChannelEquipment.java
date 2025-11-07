package de.lukasbreuer.spectron.connection.channel;

import de.lukasbreuer.spectron.connection.ConnectionClient;
import de.lukasbreuer.spectron.connection.packet.PacketDecoder;
import de.lukasbreuer.spectron.connection.packet.PacketEncoder;
import de.lukasbreuer.spectron.connection.packet.PacketIdentification;
import de.lukasbreuer.spectron.connection.packet.PacketRegistry;
import de.lukasbreuer.spectron.event.EventExecutor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class ChannelEquipment extends ChannelInitializer<Channel> {
  private final ConnectionClient client;
  private final PacketRegistry packetRegistry;
  private final EventExecutor eventExecutor;

  @Override
  protected void initChannel(Channel channel) {
    channel.pipeline().addLast("packet-encoder", PacketEncoder.create(client));
    channel.pipeline().addLast("packet-decoder", PacketDecoder.create());
    channel.pipeline().addLast("packet-identification",
      PacketIdentification.create(client, packetRegistry));
    channel.pipeline().addLast("packet-inbox",
      ChannelPacketInbox.create(eventExecutor));
  }
}