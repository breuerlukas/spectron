package de.lukasbreuer.bot.module.foundation;

import de.lukasbreuer.bot.event.EventHook;
import de.lukasbreuer.bot.event.Hook;
import de.lukasbreuer.bot.event.HookPriority;
import de.lukasbreuer.bot.event.play.SetContainerContentEvent;
import de.lukasbreuer.bot.event.play.SetContainerSlotEvent;
import de.lukasbreuer.bot.inventory.Inventory;
import de.lukasbreuer.bot.inventory.InventoryRegistry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class PlayerInventoryTrigger implements Hook {
  private final InventoryRegistry inventoryRegistry;

  @EventHook(priority = HookPriority.BEFORE)
  private void setContainerContent(SetContainerContentEvent event) {
    if (inventoryRegistry.findInventoryByWindowId(event.windowId()).isEmpty()) {
      inventoryRegistry.register(Inventory.create(event.windowId(), event.content()));
      return;
    }
    inventoryRegistry.findInventoryByWindowId(event.windowId()).get()
      .updateAll(event.content());
  }

  @EventHook(priority = HookPriority.BEFORE)
  private void setContainerSlot(SetContainerSlotEvent event) {
    if (inventoryRegistry.findInventoryByWindowId(event.windowId()).isEmpty()) {
      inventoryRegistry.register(Inventory.empty(event.windowId()));
    }
    inventoryRegistry.findInventoryByWindowId(event.windowId()).get()
      .update(event.slot(), event.item());
  }
}
