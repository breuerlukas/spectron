package de.lukasbreuer.bot.routine.assignment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class DelayAssignment implements Assignment {
  private final long delay;

  @Override
  public void call() throws Exception {
    Thread.sleep(delay);
  }
}
