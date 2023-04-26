package de.lukasbreuer.bot.chat.property;

public final class ChatPropertyBold extends SwitchableChatProperty {
  public static ChatPropertyBold create(State state) {
    return new ChatPropertyBold(state);
  }

  private ChatPropertyBold(State state) {
    super(state);
  }

  @Override
  public String code() {
    return "\u001b[1m";
  }
}
