package de.lukasbreuer.bot.connection.packet;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class PacketRegistry {
  private final Map<Integer, Class<? extends PacketOutgoing>> outgoingPackets = Maps.newHashMap();
  private final Map<Integer, Class<? extends PacketIncoming>> incomingPackets = Maps.newHashMap();

  public void registerOutgoingPacket(
    Class<? extends PacketOutgoing> packet
  ) throws Exception {
    outgoingPackets.put(findPacketIdByClass(packet), packet);
  }

  public void registerIncomingPacket(
    Class<? extends PacketIncoming> packet
  ) throws Exception {
    incomingPackets.put(findPacketIdByClass(packet), packet);
  }

  private int findPacketIdByClass(Class<? extends Packet> packet) throws Exception {
    return packet.getConstructor().newInstance().id();
  }

  public Optional<Class<? extends PacketOutgoing>> findOutgoingPacketById(int id) {
    return Optional.ofNullable(outgoingPackets.get(id));
  }

  public Optional<Class<? extends PacketIncoming>> findIncomingPacketById(int id) {
    return Optional.ofNullable(incomingPackets.get(id));
  }
}
