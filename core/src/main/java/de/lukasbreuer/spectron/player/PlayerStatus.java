package de.lukasbreuer.spectron.player;

public enum PlayerStatus {
  ON_GROUND,
  AIRBORNE;

  public boolean isOnGround() {
    return this == ON_GROUND;
  }

  public boolean isAirborne() {
    return this == AIRBORNE;
  }
}
