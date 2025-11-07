package de.lukasbreuer.spectron.connection.packet.cryptography;

public enum CryptographyStatus {
  ENABLED,
  DISABLED;

  public boolean isEnabled() {
    return this == ENABLED;
  }

  public boolean isDisabled() {
    return this == DISABLED;
  }
}
