package de.lukasbreuer.bot.connection.packet.inbound.play;

import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;
import de.lukasbreuer.bot.item.Item;
import de.lukasbreuer.bot.item.ItemType;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;

@Getter
@Accessors(fluent = true)
public final class PacketSetContainerSlot extends PacketIncoming {
  private byte windowId;
  private int lastStateId;
  private short slot;
  private Item item;

  public PacketSetContainerSlot() {
    super(0x14, ProtocolState.PLAY);
  }

  @Override
  public void read(PacketBuffer buffer) throws Exception {
    windowId = buffer.raw().readByte();
    lastStateId = buffer.readVarInt();
    slot = buffer.raw().readShort();
    var present = buffer.raw().readBoolean();
    if (!present) {
      item = Item.empty();
      return;
    }
    var itemId = buffer.readVarInt();
    var itemType = Arrays.stream(ItemType.values())
      .filter(type -> type.id() == itemId).findFirst().orElse(ItemType.EMPTY);
    var itemCount = buffer.raw().readByte();
    item = Item.create(itemType, itemCount);
  }
}
