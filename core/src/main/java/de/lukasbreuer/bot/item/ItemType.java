package de.lukasbreuer.bot.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ItemType {
  EMPTY(0),
  IRON_PICKAXE(790);

  private final int id;
}

