package de.lukasbreuer.bot;

import de.lukasbreuer.bot.authentication.Credentials;

import java.util.UUID;

public final class BotApplication {
  public static void main(String[] args) throws Exception {
    var bot = Bot.create("localhost", (short) 25565,
      Credentials.create("lukas.breuer2004@gmail.com", "lukas2004510tilobreuer"),
      "Lynoo", UUID.fromString("0ffbc827-0a24-4890-a3a4-ea7e19dd1a14"));
    bot.initialize();
    bot.connect();
  }
}
