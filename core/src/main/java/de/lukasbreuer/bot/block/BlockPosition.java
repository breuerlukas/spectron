package de.lukasbreuer.bot.block;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class BlockPosition {
  private final long x;
  private final long y;
  private final long z;

  public boolean equals(BlockPosition otherPosition) {
    return x == otherPosition.x() && y == otherPosition.y() &&
      z == otherPosition.z();
  }
}
