package de.lukasbreuer.bot.connection.channel;

import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;
import de.lukasbreuer.bot.connection.packet.inbound.login.*;
import de.lukasbreuer.bot.connection.packet.inbound.play.PacketPlayerChatMessage;
import de.lukasbreuer.bot.connection.packet.inbound.play.PacketDisconnect;
import de.lukasbreuer.bot.connection.packet.inbound.play.PacketKeepAliveRequest;
import de.lukasbreuer.bot.connection.packet.inbound.play.PacketSystemChatMessage;
import de.lukasbreuer.bot.event.EventExecutor;
import de.lukasbreuer.bot.event.login.EncryptionRequestEvent;
import de.lukasbreuer.bot.event.login.LoginDisconnectEvent;
import de.lukasbreuer.bot.event.login.LoginSuccessEvent;
import de.lukasbreuer.bot.event.login.SetCompressionEvent;
import de.lukasbreuer.bot.event.play.PlayerChatMessageEvent;
import de.lukasbreuer.bot.event.play.DisconnectEvent;
import de.lukasbreuer.bot.event.play.KeepAliveEvent;
import de.lukasbreuer.bot.event.play.SystemChatMessageEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class ChannelPacketInbox extends SimpleChannelInboundHandler<PacketIncoming> {
  private final EventExecutor eventExecutor;

  @Override
  protected void channelRead0(
    ChannelHandlerContext context, PacketIncoming incomingPacket
  ) {
    if (incomingPacket.protocolState().isLogin()) {
      processIncomingLoginPacket(incomingPacket);
      return;
    }
    if (incomingPacket.protocolState().isPlay()) {
      processIncomingPlayPacket(incomingPacket);
    }
  }

  private void processIncomingLoginPacket(PacketIncoming incomingPacket) {
    if (incomingPacket instanceof PacketLoginDisconnect packet) {
      eventExecutor.execute(LoginDisconnectEvent.create(packet.reason()));
    } else if (incomingPacket instanceof PacketEncryptionRequest packet) {
      eventExecutor.execute(EncryptionRequestEvent.create(packet.serverId(),
        packet.publicKey(), packet.verifyToken()));
    } else if (incomingPacket instanceof PacketSetCompression packet) {
      eventExecutor.execute(SetCompressionEvent.create(packet.threshold()));
    } else if (incomingPacket instanceof PacketLoginSuccess packet) {
      eventExecutor.execute(LoginSuccessEvent.create(packet.uuid(),
        packet.username(), packet.properties()));
    }
  }

  private void processIncomingPlayPacket(PacketIncoming incomingPacket) {
    if (incomingPacket instanceof PacketDisconnect packet) {
      eventExecutor.execute(DisconnectEvent.create(packet.reason()));
    } else if (incomingPacket instanceof PacketKeepAliveRequest packet) {
      eventExecutor.execute(KeepAliveEvent.create(packet.number()));
    } else if (incomingPacket instanceof PacketPlayerChatMessage packet) {
      eventExecutor.execute(PlayerChatMessageEvent.create(packet.senderId(),
        packet.message(), packet.timestamp()));
    } else if (incomingPacket instanceof PacketSystemChatMessage packet) {
      eventExecutor.execute(SystemChatMessageEvent.create(packet.content(),
        packet.displayType()));
    }
  }
}

