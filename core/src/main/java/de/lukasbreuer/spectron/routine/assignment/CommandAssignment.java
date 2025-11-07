package de.lukasbreuer.spectron.routine.assignment;

import de.lukasbreuer.spectron.chat.ChatCommand;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class CommandAssignment implements Assignment {
  private final ChatCommand command;

  @Override
  public void call() throws Exception {
    command.send();
  }
}
