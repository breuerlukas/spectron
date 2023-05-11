package de.lukasbreuer.bot.block;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BlockType {
  AIR(0),
  STONE(1),
  GRASS(9),
  DIRT(10),
  COBBLESTONE(14),
  GOLD_ORE(119),
  IRON_ORE(121),
  COAL_ORE(123),
  LAPIS_LAZULI_ORE(516),
  DIAMOND_ORE(4270),
  REDSTONE_ORE(5731),
  EMERALD_ORE(7507),
  QUARTZ_BLOCK(9091),
  QUARTZ_STAIRS(9107),
  QUARTZ_SLAB(11140);

  private final int id;
}
