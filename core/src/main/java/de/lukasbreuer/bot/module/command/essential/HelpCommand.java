package de.lukasbreuer.bot.module.command.essential;

import de.lukasbreuer.bot.log.Log;
import de.lukasbreuer.bot.module.command.Command;

public final class HelpCommand extends Command {
  public static HelpCommand create(Log log) {
    return new HelpCommand(log);
  }

  private HelpCommand(Log log) {
    super(log, "help", new String[] {"info", "command", "commands"}, new String[0]);
  }

  @Override
  public boolean execute(String[] arguments) {
    log().info("Commands: ");
    log().info("- chat <message>");
    return true;
  }
}