package de.lukasbreuer.bot.module.foundation;

import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.event.HookRegistry;
import de.lukasbreuer.bot.module.Module;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class FoundationModule extends Module {
  private final ConnectionClient client;
  private final HookRegistry hookRegistry;

  @Override
  public void onLoad() {

  }

  @Override
  public void onEnable() {
    registerHooks();
  }

  private void registerHooks() {
    hookRegistry.register(KeepAlive.create(client));
    hookRegistry.register(PlayerDisconnect.create());
    hookRegistry.register(SynchronizePlayerPosition.create(client));
  }

  @Override
  public void onDisable() {

  }
}
