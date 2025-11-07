package de.lukasbreuer.spectron.event.play;

import de.lukasbreuer.spectron.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class SynchronizePlayerPositionEvent extends Event {
  private final double x;
  private final double y;
  private final double z;
  private final float yaw;
  private final float pitch;
  private final byte flags;
  private final int teleportId;
}
