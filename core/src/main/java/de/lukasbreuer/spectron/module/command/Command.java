package de.lukasbreuer.spectron.module.command;

import de.lukasbreuer.spectron.log.Log;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Command {
  @Getter(AccessLevel.PROTECTED)
  private final Log log;
  @Getter
  private final String name;
  @Getter
  private final String[] aliases;
  @Getter
  private final String[] arguments;

  public abstract boolean execute(String[] arguments) throws Exception;
}
