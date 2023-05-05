package de.lukasbreuer.bot.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class PlayerLocation {
  private double x;
  private double y;
  private double z;
  private float yaw;
  private float pitch;
}
