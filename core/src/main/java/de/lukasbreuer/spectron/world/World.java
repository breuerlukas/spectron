package de.lukasbreuer.spectron.world;

import de.lukasbreuer.spectron.block.Block;
import de.lukasbreuer.spectron.block.BlockPosition;
import de.lukasbreuer.spectron.block.BlockType;
import de.lukasbreuer.spectron.chunk.Chunk;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(staticName = "create")
public class World {
  public static World empty() {
    return create(Lists.newArrayList());
  }

  private final List<Chunk> chunks;

  public void addChunk(Chunk chunk) {
    chunks.add(chunk);
  }

  public void updateBlock(Block block) {
    var position = block.blockPosition();
    var chunkX = (int) Math.floor(position.x() / 16D);
    var chunkZ = (int) Math.floor(position.z() / 16D);
    var chunk = findChunkByCoordinates(chunkX, chunkZ);
    if (chunk.isEmpty()) {
      addChunk(Chunk.empty(chunkX, chunkZ));
      updateBlock(block);
      return;
    }
    chunk.get().updateBlock(block);
  }

  public void removeChunk(Chunk chunk) {
    chunks.remove(chunk);
  }

  public Optional<Chunk> findChunkByCoordinates(int x, int z) {
    return chunks.stream()
      .filter(chunk -> chunk.x() == x && chunk.z() == z)
      .findFirst();
  }

  public Block findBlockByPosition(BlockPosition position) {
    var chunkX = (int) Math.floor(position.x() / 16D);
    var chunkZ = (int) Math.floor(position.z() / 16D);
    var chunk = findChunkByCoordinates(chunkX, chunkZ);
    if (chunk.isEmpty()) {
      return Block.create(position, BlockType.AIR);
    }
    return chunk.get().findBlockByPosition(position);
  }

  public List<Chunk> findAll() {
    return List.copyOf(chunks);
  }
}
