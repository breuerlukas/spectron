package de.lukasbreuer.bot.connection.packet.outbound.play;

import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;
import de.lukasbreuer.bot.item.Item;
import de.lukasbreuer.bot.item.ItemType;

import java.util.Map;

public final class PacketClickContainer extends PacketOutgoing {
  private final byte windowId;
  private final int lastStateId;
  private final short slot;
  private final byte button;
  private final int mode;
  private final Map<Integer, Item> content;

  public PacketClickContainer(
    byte windowId, int lastStateId, short slot, byte button, int mode,
    Map<Integer, Item> content
  ) {
    super(0x0B, ProtocolState.PLAY);
    this.windowId = windowId;
    this.lastStateId = lastStateId;
    this.slot = slot;
    this.button = button;
    this.mode = mode;
    this.content = content;
  }

  public PacketClickContainer() {
    super(0x0B, ProtocolState.PLAY);
    this.windowId = -1;
    this.lastStateId = -1;
    this.slot = -1;
    this.button = -1;
    this.mode = -1;
    this.content = null;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.raw().writeByte(windowId);
    buffer.writeVarInt(lastStateId);
    buffer.raw().writeShort(slot);
    buffer.raw().writeByte(button);
    buffer.writeVarInt(mode);
    buffer.writeVarInt(content.size());
    for (var entry : content.entrySet()) {
      buffer.raw().writeShort(entry.getKey());
      if (entry.getValue().type() == ItemType.EMPTY) {
        buffer.raw().writeBoolean(false);
        continue;
      }
      buffer.raw().writeBoolean(true);
      buffer.writeVarInt(entry.getValue().type().id());
      buffer.raw().writeByte(entry.getValue().amount());
      buffer.raw().writeByte(0);
    }
    buffer.raw().writeByte(0);
  }
}
