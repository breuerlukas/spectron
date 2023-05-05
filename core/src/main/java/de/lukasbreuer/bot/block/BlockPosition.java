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

  @Override
  public String toString() {
    return "BlockPosition{" +
      "x=" + x +
      ", y=" + y +
      ", z=" + z +
      '}';
  }
}
