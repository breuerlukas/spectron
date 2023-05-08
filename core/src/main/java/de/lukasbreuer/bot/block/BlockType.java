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
  GRASS(2),
  DIRT(3),
  COBBLESTONE(4),
  GOLD_ORE(14),
  IRON_ORE(15),
  COAL_ORE(16),
  LAPIS_LAZULI_ORE(21),
  SLAB(44),
  DIAMOND_ORE(56),
  REDSTONE_ORE(73),
  END_PORTAL(119),
  EMERALD_ORE(129),
  QUARTZ_BLOCK(155),
  QUARTZ_STAIRS(156);

  private final int id;
}
