package de.lukasbreuer.bot.routine;

import de.lukasbreuer.bot.routine.assignment.Assignment;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "create")
public class Routine {
  private final List<Assignment> assignments;

  public CompletableFuture<Void> execute() {
    var future = new CompletableFuture<Void>();
    new Thread(() -> callAssignments(future)).start();
    return future;
  }

  private void callAssignments(CompletableFuture<Void> future) {
    for (var assignment : assignments) {
      try {
        assignment.call();
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
    future.complete(null);
  }
}
