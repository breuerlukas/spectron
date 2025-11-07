package de.lukasbreuer.spectron.module.foundation;

import de.lukasbreuer.spectron.event.EventHook;
import de.lukasbreuer.spectron.event.Hook;
import de.lukasbreuer.spectron.event.HookPriority;
import de.lukasbreuer.spectron.event.play.SetContainerContentEvent;
import de.lukasbreuer.spectron.event.play.SetContainerSlotEvent;
import de.lukasbreuer.spectron.inventory.Inventory;
import de.lukasbreuer.spectron.inventory.InventoryRegistry;
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
