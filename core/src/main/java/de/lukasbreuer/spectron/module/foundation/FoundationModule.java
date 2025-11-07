package de.lukasbreuer.spectron.module.foundation;

import com.google.inject.Inject;
import com.google.inject.Injector;
import de.lukasbreuer.spectron.connection.ConnectionClient;
import de.lukasbreuer.spectron.event.HookRegistry;
import de.lukasbreuer.spectron.inventory.InventoryRegistry;
import de.lukasbreuer.spectron.log.Log;
import de.lukasbreuer.spectron.module.Module;
import de.lukasbreuer.spectron.player.PlayerLocation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class FoundationModule extends Module {
  private final Injector injector;
  private final ConnectionClient client;
  private final PlayerLocation playerLocation;
  @Inject
  private Log log;
  @Inject
  private HookRegistry hookRegistry;
  @Inject
  private InventoryRegistry inventoryRegistry;

  @Override
  public void onLoad() {
    injector.injectMembers(this);
  }

  @Override
  public void onEnable() {
    registerHooks();
  }

  private void registerHooks() {
    hookRegistry.register(KeepAlive.create(client));
    hookRegistry.register(PlayerDisconnect.create(log));
    hookRegistry.register(SynchronizePlayerPosition.create(client, playerLocation));
    hookRegistry.register(PlayerInventoryTrigger.create(inventoryRegistry));
  }

  @Override
  public void onDisable() {
    hookRegistry.unregister(KeepAlive.class);
    hookRegistry.unregister(PlayerDisconnect.class);
    hookRegistry.unregister(SynchronizePlayerPosition.class);
    hookRegistry.unregister(PlayerInventoryTrigger.class);
  }
}
