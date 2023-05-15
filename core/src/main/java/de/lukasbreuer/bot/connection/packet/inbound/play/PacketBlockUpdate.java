package de.lukasbreuer.bot.connection.packet.inbound.play;

import de.lukasbreuer.bot.block.Block;
import de.lukasbreuer.bot.block.BlockPosition;
import de.lukasbreuer.bot.block.BlockType;
import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;

@Getter
@Accessors(fluent = true)
public final class PacketBlockUpdate extends PacketIncoming {
  private Block[] blocks;

  public PacketBlockUpdate() {
    super(0x43, ProtocolState.PLAY);
  }

  @Override
  public void read(PacketBuffer buffer) {
    var encodedChunkCoordinates = buffer.raw().readLong();
    var chunkX = (encodedChunkCoordinates >> 42) * 16;
    var chunkY = (encodedChunkCoordinates << 44 >> 44) * 16;
    var chunkZ = (encodedChunkCoordinates << 22 >> 42) * 16;
    buffer.raw().readBoolean();
    var updatedBlockSize = buffer.readVarInt();
    blocks = new Block[updatedBlockSize];
    for (var i = 0; i < updatedBlockSize; i++) {
      var value = buffer.readVarLong();
      var stateId = value >> 12;
      var xyzValue = value ^ stateId << 12;
      var localX = xyzValue >> 8;
      var zyValue = xyzValue ^ localX << 8;
      var localZ = zyValue >> 4;
      var localY = zyValue ^ localZ << 4;
      var blockType = Arrays.stream(BlockType.values())
        .filter(type -> type.id() == stateId).findFirst().orElse(BlockType.AIR);
      blocks[i] = Block.create(BlockPosition.create(chunkX + localX,
        chunkY + localY, chunkZ + localZ), blockType);
    }
  }
}
