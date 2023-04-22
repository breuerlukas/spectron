package de.lukasbreuer.bot.connection;

import de.lukasbreuer.bot.authentication.Authentication;
import de.lukasbreuer.bot.connection.channel.ChannelEquipment;
import de.lukasbreuer.bot.connection.packet.PacketRegistry;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class ConnectionClient {
  private final PacketRegistry packetRegistry;
  private final String hostname;
  private final int port;
  @Getter
  private Channel channel;
  private EventLoopGroup group;
  @Setter
  @Getter
  private Authentication authentication;
  @Setter
  @Getter
  private byte[] key;
  @Setter
  @Getter
  private boolean compressionEnabled;
  @Setter
  @Getter
  private boolean login = false;

  public void connectAsync(Runnable callback) {
    new Thread(() -> connect(callback)).start();
  }

  private void connect(Runnable callback) {
    connect();
    callback.run();
  }

  public void connect() {
    group = new NioEventLoopGroup();
    channel = new Bootstrap()
      .group(group)
      .channel(NioSocketChannel.class)
      .handler(ChannelEquipment.create(this, packetRegistry))
      .connect(hostname, port)
      .syncUninterruptibly().channel();
  }

  public <T extends PacketOutgoing> void sendPacket(T packet) {
    channel.writeAndFlush(packet);
  }

  public void disconnect() {
    group.shutdownGracefully();
    channel.close();
  }
}
