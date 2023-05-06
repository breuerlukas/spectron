package de.lukasbreuer.bot.world;

import de.lukasbreuer.bot.block.Block;
import de.lukasbreuer.bot.chunk.Chunk;
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

  public List<Chunk> findAll() {
    return List.copyOf(chunks);
  }
}
