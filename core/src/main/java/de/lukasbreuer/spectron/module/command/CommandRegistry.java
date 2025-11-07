package de.lukasbreuer.spectron.module.command;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class CommandRegistry {
  private final List<Command> commands = Lists.newArrayList();

  public void register(Command command) {
    commands.add(command);
  }

  public void unregister(Command command) {
    commands.remove(command);
  }

  public Optional<Command> find(String input) {
    return commands.stream()
      .filter(command -> command.name().equalsIgnoreCase(input) ||
        Arrays.stream(command.aliases()).anyMatch(alias -> alias.equalsIgnoreCase(input)))
      .findFirst();
  }

  public List<Command> findAll() {
    return List.copyOf(commands);
  }
}
