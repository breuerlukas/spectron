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

  public void update(double x, double y, double z, float yaw, float pitch) {
    x(x);
    y(y);
    z(z);
    yaw(yaw);
    pitch(pitch);
  }
}
