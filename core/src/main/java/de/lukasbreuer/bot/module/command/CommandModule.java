package de.lukasbreuer.bot.module.command;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import de.lukasbreuer.bot.authentication.Authentication;
import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.log.Log;
import de.lukasbreuer.bot.module.Module;
import de.lukasbreuer.bot.module.command.essential.ChatCommand;
import de.lukasbreuer.bot.module.command.essential.HelpCommand;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class CommandModule extends Module {
  private final Injector injector;
  private final ConnectionClient client;
  private final Authentication authentication;
  @Inject
  private Log log;
  @Inject
  private CommandRegistry commandRegistry;
  @Inject
  @Named("uuid")
  private UUID playerUuid;
  private Thread commandTaskThread;

  @Override
  public void onLoad() {
    injector.injectMembers(this);
  }

  @Override
  public void onEnable() {
    registerCommands();
    commandTaskThread = new Thread(() ->
      CommandTask.create(log, commandRegistry).start());
    commandTaskThread.start();
  }

  private void registerCommands() {
    commandRegistry.register(HelpCommand.create(log));
    commandRegistry.register(ChatCommand.create(log, client, authentication,
      playerUuid));
  }

  @Override
  public void onDisable() {
    commandTaskThread.interrupt();
    unregisterCommands();
  }

  private void unregisterCommands() {
    for (var command : commandRegistry.findAll()) {
      commandRegistry.unregister(command);
    }
  }
}
