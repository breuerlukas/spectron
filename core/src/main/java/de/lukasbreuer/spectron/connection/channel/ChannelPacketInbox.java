package de.lukasbreuer.spectron.connection.channel;

import de.lukasbreuer.spectron.connection.packet.inbound.PacketIncoming;
import de.lukasbreuer.spectron.connection.packet.inbound.login.*;
import de.lukasbreuer.spectron.connection.packet.inbound.play.*;
import de.lukasbreuer.spectron.event.EventExecutor;
import de.lukasbreuer.spectron.event.login.EncryptionRequestEvent;
import de.lukasbreuer.spectron.event.login.LoginDisconnectEvent;
import de.lukasbreuer.spectron.event.login.LoginSuccessEvent;
import de.lukasbreuer.spectron.event.login.SetCompressionEvent;
import de.lukasbreuer.spectron.event.play.*;
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
    } else if (incomingPacket instanceof PacketSynchronizePlayerPosition packet) {
      eventExecutor.execute(SynchronizePlayerPositionEvent.create(packet.x(),
        packet.y(), packet.z(), packet.yaw(), packet.pitch(), packet.flags(),
        packet.teleportId()));
    } else if (incomingPacket instanceof PacketSpawnPlayer packet) {
      eventExecutor.execute(SpawnPlayerEvent.create(packet.playerUuid(),
        packet.entityId()));
    } else if (incomingPacket instanceof PacketRemoveEntities packet) {
      for (var entityId : packet.entityIds()) {
        eventExecutor.execute(RemoveEntityEvent.create(entityId));
      }
    } else if (incomingPacket instanceof PacketSetContainerContent packet) {
      eventExecutor.execute(SetContainerContentEvent.create(packet.windowId(),
        packet.lastStateId(), packet.content()));
    } else if (incomingPacket instanceof PacketSetContainerSlot packet) {
      eventExecutor.execute(SetContainerSlotEvent.create(packet.windowId(),
        packet.lastStateId(), packet.slot(), packet.item()));
    } else if (incomingPacket instanceof PacketBlockUpdate packet) {
      for (var block : packet.blocks()) {
        eventExecutor.execute(BlockUpdateEvent.create(block));
      }
    }
  }
}

