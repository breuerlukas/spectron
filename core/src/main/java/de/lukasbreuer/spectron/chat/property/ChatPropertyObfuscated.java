package de.lukasbreuer.spectron.chat.property;

public final class ChatPropertyObfuscated extends SwitchableChatProperty {
  public static ChatPropertyObfuscated create(State state) {
    return new ChatPropertyObfuscated(state);
  }

  private ChatPropertyObfuscated(State state) {
    super(state);
  }

  @Override
  public String code() {
    return "";
  }
}
