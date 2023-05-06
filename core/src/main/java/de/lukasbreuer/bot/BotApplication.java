package de.lukasbreuer.bot;

public final class BotApplication {
  public static void main(String[] args) throws Exception {
    var bot = Bot.create();
    bot.initialize();
    bot.connect();
  }
}
