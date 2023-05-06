package de.lukasbreuer.bot.routine;

import com.google.common.collect.Lists;
import de.lukasbreuer.bot.routine.assignment.Assignment;
import de.lukasbreuer.bot.routine.assignment.ChatAssignment;
import de.lukasbreuer.bot.routine.assignment.CommandAssignment;
import de.lukasbreuer.bot.routine.assignment.DelayAssignment;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(staticName = "create")
public final class RoutineBuilder {
  private final List<Assignment> assignments = Lists.newArrayList();

  public void command(CommandAssignment commandAssignment) {
    assignments.add(commandAssignment);
  }

  public void chat(ChatAssignment chatAssignment) {
    assignments.add(chatAssignment);
  }

  public <T extends Assignment> void individual(T assignment) {
    assignments.add(assignment);
  }

  public void delay(long milliseconds) {
    assignments.add(DelayAssignment.create(milliseconds));
  }

  public Routine build() {
    return Routine.create(assignments);
  }
}
