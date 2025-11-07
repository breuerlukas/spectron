package de.lukasbreuer.spectron.connection.packet.inbound.play;

import com.google.common.collect.Maps;
import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.inbound.PacketIncoming;
import de.lukasbreuer.spectron.item.Item;
import de.lukasbreuer.spectron.item.ItemType;
import dev.dewy.nbt.Nbt;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

@Getter
@Accessors(fluent = true)
public final class PacketSetContainerContent extends PacketIncoming {
  private byte windowId;
  private int lastStateId;
  private Map<Integer, Item> content;

  public PacketSetContainerContent() {
    super(0x12, ProtocolState.PLAY);
  }

  @Override
  public void read(PacketBuffer buffer) throws Exception {
    windowId = buffer.raw().readByte();
    lastStateId = buffer.readVarInt();
    var count = buffer.readVarInt();
    content = Maps.newHashMap();
    for (var i = 0; i < count; i++) {
      var present = buffer.raw().readBoolean();
      if (!present) {
        content.put(i, Item.empty());
        continue;
      }
      var itemId = buffer.readVarInt();
      var itemType = Arrays.stream(ItemType.values())
        .filter(type -> type.id() == itemId).findFirst().orElse(ItemType.UNKNOWN);
      var itemCount = buffer.raw().readByte();
      readItemNbtTag(buffer);
      content.put(i, Item.create(itemType, itemCount));
    }
  }

  private void readItemNbtTag(PacketBuffer buffer) throws Exception {
    var copy = PacketBuffer.create(Unpooled.copiedBuffer(buffer.raw()));
    var status = copy.raw().readByte();
    if (status == 0) {
      return;
    }
    var stream = new DataInputStream(new InputStream() {
      @Override
      public int read() {
        return buffer.raw().readByte();
      }
    });
    new Nbt().fromStream(stream);
  }
}
