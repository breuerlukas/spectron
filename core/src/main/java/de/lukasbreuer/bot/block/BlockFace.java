package de.lukasbreuer.bot.block;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BlockFace {
  BOTTOM(0),
  TOP(1),
  NORTH(2),
  SOUTH(3),
  WEST(4),
  EAST(5);

  private final int value;
}
