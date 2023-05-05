package de.lukasbreuer.bot.block;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BlockType {
  STONE(1),
  GRASS(2),
  DIRT(3),
  COBBLESTONE(4),
  GOLD_ORE(14),
  IRON_ORE(15),
  COAL_ORE(16),
  LAPIS_LAZULI_ORE(21),
  DIAMOND_ORE(56),
  REDSTONE_ORE(73),
  END_PORTAL(119),
  EMERALD_ORE(129);

  private final int id;
}
