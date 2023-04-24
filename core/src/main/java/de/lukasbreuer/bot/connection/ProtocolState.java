package de.lukasbreuer.bot.connection;

public enum ProtocolState {
  HANDSHAKE,
  STATUS,
  LOGIN,
  PLAY;

  public boolean isHandshake() {
    return this == HANDSHAKE;
  }

  public boolean isStatus() {
    return this == STATUS;
  }

  public boolean isLogin() {
    return this == LOGIN;
  }

  public boolean isPlay() {
    return this == PLAY;
  }
}
