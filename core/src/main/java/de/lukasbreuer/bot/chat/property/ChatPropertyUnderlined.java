package de.lukasbreuer.bot.chat.property;

public final class ChatPropertyUnderlined extends SwitchableChatProperty {
  public static ChatPropertyUnderlined create(State state) {
    return new ChatPropertyUnderlined(state);
  }

  private ChatPropertyUnderlined(State state) {
    super(state);
  }

  @Override
  public String code() {
    return "\u001b[4m";
  }
}
