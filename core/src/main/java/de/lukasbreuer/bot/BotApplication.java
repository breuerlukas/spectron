package de.lukasbreuer.bot;

public final class BotApplication {
  public static void main(String[] args) throws Exception {
    Bot.create("localhost", (short) 25565).connect();
  }
}
