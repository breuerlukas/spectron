package de.lukasbreuer.spectron.module;

public abstract class Module {
  public abstract void onLoad();

  public abstract void onEnable();

  public abstract void onDisable();
}
