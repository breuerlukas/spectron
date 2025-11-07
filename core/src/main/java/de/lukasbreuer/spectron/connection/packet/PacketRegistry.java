package de.lukasbreuer.spectron.connection.packet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.inbound.PacketIncoming;
import de.lukasbreuer.spectron.connection.packet.outbound.PacketOutgoing;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class PacketRegistry {
  private final List<PacketOutgoing> outgoingPackets = Lists.newArrayList();
  private final List<PacketIncoming> incomingPackets = Lists.newArrayList();

  public void registerOutgoingPacket(
    Class<? extends PacketOutgoing> packet
  ) throws Exception {
    outgoingPackets.add(packet.getConstructor().newInstance());
  }

  public void registerIncomingPacket(
    Class<? extends PacketIncoming> packet
  ) throws Exception {
    incomingPackets.add(packet.getConstructor().newInstance());
  }

  public Optional<Class<? extends PacketOutgoing>> findOutgoingPacket(
    int id, ProtocolState protocolState
  ) {
    return findPacket(id, protocolState, outgoingPackets);
  }

  public Optional<Class<? extends PacketIncoming>> findIncomingPacket(
    int id, ProtocolState protocolState
  ) {
    return findPacket(id, protocolState, incomingPackets);
  }

  private <T extends Packet> Optional<Class<? extends T>> findPacket(
    int id, ProtocolState protocolState, List<T> packetList
  ) {
    var packetOptional = packetList.stream().filter(entry -> entry.id() == id)
      .filter(entry -> entry.protocolState() == protocolState).findFirst();
    return packetOptional.map(packet -> (Class<? extends T>) packet.getClass());
  }
}
