package de.lukasbreuer.spectron;

public final class BotApplication {
  public static void main(String[] args) throws Exception {
    System.setProperty("jdk.httpclient.allowRestrictedHeaders", "Content-Length");
    var bot = Bot.create();
    bot.initialize();
    bot.connect();
  }
}
