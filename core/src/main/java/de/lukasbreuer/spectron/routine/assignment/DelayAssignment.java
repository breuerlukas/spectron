package de.lukasbreuer.spectron.routine.assignment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class DelayAssignment implements Assignment {
  private final long delay;

  @Override
  public void call() throws Exception {
    Thread.sleep(delay);
  }
}
