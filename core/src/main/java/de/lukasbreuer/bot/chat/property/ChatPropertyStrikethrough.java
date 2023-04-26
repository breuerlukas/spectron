package de.lukasbreuer.bot.chat.property;

public final class ChatPropertyStrikethrough extends SwitchableChatProperty {
  public static ChatPropertyStrikethrough create(State state) {
    return new ChatPropertyStrikethrough(state);
  }

  private ChatPropertyStrikethrough(State state) {
    super(state);
  }

  @Override
  public String code() {
    return "\u001b[9m";
  }
}
