package de.lukasbreuer.bot.chat.property;

import lombok.Getter;
import lombok.experimental.Accessors;

public final class ChatPropertyColor extends ChatProperty {
  public static ChatPropertyColor of(String color) {
    return new ChatPropertyColor(ChatColor.valueOf(color.toUpperCase()));
  }

  public static ChatPropertyColor create(ChatColor color) {
    return new ChatPropertyColor(color);
  }

  @Accessors(fluent = true)
  public enum ChatColor {
    BLACK("\u001b[38;2;0;0;0m"),
    DARK_BLUE("\u001b[38;2;0;0;170m"),
    DARK_GREEN("\u001b[38;2;0;170;0m"),
    DARK_AQUA("\u001b[38;2;0;170;170m"),
    DARK_RED("\u001b[38;2;170;0;0m"),
    DARK_PURPLE("\u001b[38;2;170;0;170m"),
    GOLD("\u001b[38;2;255;170;0m"),
    GRAY("\u001b[38;2;170;170;170m"),
    DARK_GRAY("\u001b[38;2;85;85;85m"),
    BLUE("\u001b[38;2;85;85;255m"),
    GREEN("\u001b[38;2;85;255;85m"),
    AQUA("\u001b[38;2;85;255;255m"),
    RED("\u001b[38;2;255;85;85m"),
    LIGHT_PURPLE("\u001b[38;2;255;85;255m"),
    YELLOW("\u001b[38;2;255;255;85m"),
    WHITE("\u001b[38;2;255;255;255m");

    @Getter
    private final String code;

    ChatColor(String code) {
      this.code = code;
    }
  }

  private final ChatColor color;

  private ChatPropertyColor(ChatColor color) {
    this.color = color;
  }

  @Override
  public String value() {
    return color.code();
  }
}
