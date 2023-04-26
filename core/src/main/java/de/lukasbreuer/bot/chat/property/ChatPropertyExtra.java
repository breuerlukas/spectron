package de.lukasbreuer.bot.chat.property;

import de.lukasbreuer.bot.chat.ChatComponent;
import lombok.Getter;

import java.util.List;

public final class ChatPropertyExtra extends ChatProperty {
  public static ChatPropertyExtra create(List<ChatComponent> components) {
    return new ChatPropertyExtra(components);
  }

  @Getter
  private final List<ChatComponent> components;

  private ChatPropertyExtra(List<ChatComponent> components) {
    this.components = components;
  }

  @Override
  public String value() {
    var result = new StringBuilder();
    for (var component : components) {
      result.append(component.toString());
    }
    return result.toString();
  }
}
