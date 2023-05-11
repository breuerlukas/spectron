package de.lukasbreuer.bot.inventory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class InventoryRegistry {
  private final List<Inventory> inventories = Lists.newArrayList();

  public void register(Inventory inventory) {
    inventories.add(inventory);
  }

  public void unregister(Inventory inventory) {
    inventories.remove(inventory);
  }

  public Optional<Inventory> findInventoryByWindowId(byte windowId) {
    return inventories.stream()
      .findFirst();
  }

  public List<Inventory> findAll() {
    return List.copyOf(inventories);
  }
}
