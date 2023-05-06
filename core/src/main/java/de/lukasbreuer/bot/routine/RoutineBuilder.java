package de.lukasbreuer.bot.routine;

import com.google.common.collect.Lists;
import de.lukasbreuer.bot.chat.ChatCommand;
import de.lukasbreuer.bot.chat.ChatMessage;
import de.lukasbreuer.bot.routine.assignment.Assignment;
import de.lukasbreuer.bot.routine.assignment.ChatAssignment;
import de.lukasbreuer.bot.routine.assignment.CommandAssignment;
import de.lukasbreuer.bot.routine.assignment.DelayAssignment;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(staticName = "create")
public final class RoutineBuilder {
  private final List<Assignment> assignments = Lists.newArrayList();

  public RoutineBuilder command(ChatCommand command) {
    assignments.add(CommandAssignment.create(command));
    return this;
  }

  public RoutineBuilder chat(ChatMessage message) {
    assignments.add(ChatAssignment.create(message));
    return this;
  }

  public <T extends Assignment> RoutineBuilder individual(T assignment) {
    assignments.add(assignment);
    return this;
  }

  public RoutineBuilder delay(long milliseconds) {
    assignments.add(DelayAssignment.create(milliseconds));
    return this;
  }

  public Routine build() {
    return Routine.create(assignments);
  }
}
