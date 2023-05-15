package de.lukasbreuer.bot.connection.packet.inbound.play;

import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Getter
@Accessors(fluent = true)
public final class PacketRemoveEntities extends PacketIncoming {
  private List<Integer> entityIds;

  public PacketRemoveEntities() {
    super(0x3E, ProtocolState.PLAY);
  }

  @Override
  public void read(PacketBuffer buffer) {
    var count = buffer.readVarInt();
    entityIds = Lists.newArrayList();
    for (var i = 0; i < count; i++) {
      entityIds.add(buffer.readVarInt());
    }
  }
}
