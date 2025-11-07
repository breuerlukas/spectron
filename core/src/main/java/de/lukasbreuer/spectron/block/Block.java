package de.lukasbreuer.spectron.block;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class Block {
  private final BlockPosition blockPosition;
  private final BlockType blockType;
}
