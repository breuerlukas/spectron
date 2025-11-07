package de.lukasbreuer.spectron.inventory;

import com.google.common.collect.Maps;
import de.lukasbreuer.spectron.item.Item;
import de.lukasbreuer.spectron.item.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Optional;

@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class Inventory {
  public static Inventory empty(byte windowId) {
    return create(windowId, Maps.newHashMap());
  }

  @Getter
  private final byte windowId;
  private Map<Integer, Item> content;

  public void update(int slot, Item item) {
    content.put(slot, item);
  }

  public void updateAll(Map<Integer, Item> content) {
    this.content = Maps.newHashMap(content);
  }

  public Optional<Item> findItemBySlot(int slot) {
    return Optional.ofNullable(content.get(slot));
  }

  public Map<Integer, Item> findItemsOfType(ItemType type) {
    var result = Maps.<Integer, Item>newHashMap();
    content.entrySet().stream()
      .filter(entry -> entry.getValue().type() == type)
      .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
    return result;
  }

  public Map<Integer, Item> content() {
    return Maps.newHashMap(content);
  }
}
