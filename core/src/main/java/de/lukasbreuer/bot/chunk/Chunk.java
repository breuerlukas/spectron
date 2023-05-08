package de.lukasbreuer.bot.chunk;

import de.lukasbreuer.bot.block.Block;
import de.lukasbreuer.bot.block.BlockPosition;
import de.lukasbreuer.bot.block.BlockType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class Chunk {
  public static Chunk empty(int x, int z) {
    return create(x, z, Lists.newArrayList());
  }

  private final int x;
  private final int z;
  private final List<Block> blocks;

  public void updateBlock(Block block) {
    blocks.stream()
      .filter(chunkBlock -> chunkBlock.blockPosition().equals(block.blockPosition()))
      .findFirst().ifPresent(blocks::remove);
    blocks.add(block);
  }

  public Block findBlockByPosition(BlockPosition position) {
    var block = blocks.stream()
      .filter(chunkBlock -> chunkBlock.blockPosition().equals(position))
      .findFirst();
    return block.orElseGet(() -> Block.create(position, BlockType.AIR));
  }
}
