package de.lukasbreuer.spectron.chat.property;

public final class ChatPropertyText extends ChatProperty {
  public static ChatPropertyText create(String text) {
    return new ChatPropertyText(text);
  }

  private final String text;

  private ChatPropertyText(String text) {
    this.text = text;
  }

  @Override
  public String value() {
    return text;
  }
}
