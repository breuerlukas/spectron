package de.lukasbreuer.spectron.connection.packet.compression;

public enum CompressionStatus {
  ENABLED,
  DISABLED;

  public boolean isEnabled() {
    return this == ENABLED;
  }

  public boolean isDisabled() {
    return this == DISABLED;
  }
}
