package de.lukasbreuer.bot.module;

public abstract class Module {
  public abstract void onLoad();

  public abstract void onEnable();

  public abstract void onDisable();
}
