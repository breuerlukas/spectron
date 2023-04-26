package de.lukasbreuer.bot.chat.property;

public abstract class SwitchableChatProperty extends ChatProperty {
  public enum State {
    ENABLED,
    DISABLED;

    public boolean isEnabled() {
      return this == ENABLED;
    }

    public boolean isDisabled() {
      return this == DISABLED;
    }
  }

  private final State state;

  protected SwitchableChatProperty(State state) {
    this.state = state;
  }

  @Override
  public String value() {
    if (state.isDisabled()) {
      return "";
    }
    return code();
  }

  public abstract String code();
}
