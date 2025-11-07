package de.lukasbreuer.spectron.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class Item {
  public static Item empty() {
    return Item.create(ItemType.EMPTY, 0);
  }

  @Getter
  private final ItemType type;
  @Getter
  private final int amount;
}
