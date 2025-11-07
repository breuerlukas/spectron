package de.lukasbreuer.spectron.chat.property;

public final class ChatPropertyItalic extends SwitchableChatProperty {
  public static ChatPropertyItalic create(State state) {
    return new ChatPropertyItalic(state);
  }

  private ChatPropertyItalic(State state) {
    super(state);
  }

  @Override
  public String code() {
    return "\u001b[3m";
  }
}
