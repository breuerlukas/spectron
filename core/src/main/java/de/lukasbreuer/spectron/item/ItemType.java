package de.lukasbreuer.spectron.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ItemType {
  UNKNOWN(-1),
  EMPTY(0),
  IRON_PICKAXE(790);

  private final int id;
}

