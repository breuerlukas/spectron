package de.lukasbreuer.spectron.connection;

import de.lukasbreuer.spectron.connection.channel.ChannelEquipment;
import de.lukasbreuer.spectron.connection.packet.PacketRegistry;
import de.lukasbreuer.spectron.connection.packet.compression.CompressionStatus;
import de.lukasbreuer.spectron.connection.packet.cryptography.CryptographyStatus;
import de.lukasbreuer.spectron.connection.packet.outbound.PacketOutgoing;
import de.lukasbreuer.spectron.event.EventExecutor;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class ConnectionClient {
  private final PacketRegistry packetRegistry;
  private final String hostname;
  private final int port;
  private final EventExecutor eventExecutor;
  @Getter
  private Channel channel;
  private EventLoopGroup group;
  @Getter @Setter
  private ProtocolState protocolState = ProtocolState.HANDSHAKE;
  @Getter @Setter
  private CryptographyStatus cryptographyStatus = CryptographyStatus.DISABLED;
  @Getter @Setter
  private CompressionStatus compressionStatus = CompressionStatus.DISABLED;
  @Getter @Setter
  private UUID sessionId;

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
      .handler(ChannelEquipment.create(this, packetRegistry, eventExecutor))
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
