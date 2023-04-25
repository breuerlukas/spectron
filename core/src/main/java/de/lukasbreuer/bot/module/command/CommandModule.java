package de.lukasbreuer.bot.module.command;

import de.lukasbreuer.bot.authentication.Authentication;
import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.log.Log;
import de.lukasbreuer.bot.module.Module;
import de.lukasbreuer.bot.module.command.implementation.ChatCommand;
import de.lukasbreuer.bot.module.command.implementation.HelpCommand;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class CommandModule extends Module {
  private final Log log;
  private final CommandRegistry commandRegistry;
  private final ConnectionClient client;
  private final Authentication authentication;
  private final UUID playerUuid;

  @Override
  public void onLoad() {
  }

  @Override
  public void onEnable() {
    registerCommands();
    new Thread(() -> CommandTask.create(log, commandRegistry).start()).start();
  }

  private void registerCommands() {
    commandRegistry.register(HelpCommand.create(log));
    commandRegistry.register(ChatCommand.create(log, client, authentication,
      playerUuid));
  }

  @Override
  public void onDisable() {
    unregisterCommands();
  }

  private void unregisterCommands() {
    for (var command : commandRegistry.findAll()) {
      commandRegistry.unregister(command);
    }
  }
}
