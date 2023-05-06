package de.lukasbreuer.bot.module.foundation;

import com.google.inject.Inject;
import com.google.inject.Injector;
import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.event.HookRegistry;
import de.lukasbreuer.bot.module.Module;
import de.lukasbreuer.bot.player.PlayerLocation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class FoundationModule extends Module {
  private final Injector injector;
  private final ConnectionClient client;
  private final PlayerLocation playerLocation;
  @Inject
  private HookRegistry hookRegistry;

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
    hookRegistry.register(PlayerDisconnect.create());
    hookRegistry.register(SynchronizePlayerPosition.create(client, playerLocation));
  }

  @Override
  public void onDisable() {
    hookRegistry.unregister(KeepAlive.class);
    hookRegistry.unregister(PlayerDisconnect.class);
    hookRegistry.unregister(SynchronizePlayerPosition.class);
  }
}
