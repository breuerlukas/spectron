package de.lukasbreuer.bot.chat;

import de.lukasbreuer.bot.chat.property.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(staticName = "create")
public final class ChatComponent {
  private final JSONObject component;
  private final List<ChatProperty> properties = Lists.newArrayList();

  public void interpret() {
    checkTextProperty();
    checkBoldProperty();
    checkItalicProperty();
    checkUnderlinedProperty();
    checkStrikethroughProperty();
    checkObfuscatedProperty();
    checkColorProperty();
    checkExtraProperty();
  }

  private void checkTextProperty() {
    if (component.has("text")) {
      properties.add(ChatPropertyText.create(component.getString("text")));
    }
  }

  private void checkBoldProperty() {
    if (component.has("bold")) {
      properties.add(ChatPropertyBold.create(findSwitchableState("bold")));
    }
  }

  private void checkItalicProperty() {
    if (component.has("italic")) {
      properties.add(ChatPropertyItalic.create(findSwitchableState("italic")));
    }
  }

  private void checkUnderlinedProperty() {
    if (component.has("underlined")) {
      properties.add(ChatPropertyUnderlined.create(findSwitchableState("underlined")));
    }
  }

  private void checkStrikethroughProperty() {
    if (component.has("strikethrough")) {
      properties.add(ChatPropertyStrikethrough.create(findSwitchableState("strikethrough")));
    }
  }

  private void checkObfuscatedProperty() {
    if (component.has("obfuscated")) {
      properties.add(ChatPropertyObfuscated.create(findSwitchableState("obfuscated")));
    }
  }

  private void checkColorProperty() {
    if (component.has("color")) {
      properties.add(ChatPropertyColor.of(component.getString("color")));
    }
  }

  private void checkExtraProperty() {
    if (component.has("extra")) {
      var array = component.getJSONArray("extra");
      var extraComponents = Lists.<ChatComponent>newArrayList();
      for (var i = 0; i < array.length(); i++) {
        var extraComponent = ChatComponent.create(array.getJSONObject(i));
        extraComponent.interpret();
        extraComponents.add(extraComponent);
      }
      properties.add(ChatPropertyExtra.create(extraComponents));
    }
  }

  private SwitchableChatProperty.State findSwitchableState(String identifier) {
    return component.getBoolean(identifier) ?
      SwitchableChatProperty.State.ENABLED : SwitchableChatProperty.State.DISABLED;
  }

  private static final String ANSI_RESET_TAG = "\u001B[0m";

  @Override
  public String toString() {
    var textProperty = findTextProperty();
    var extraProperty = findExtraProperty();
    if (textProperty.isEmpty() && extraProperty.isEmpty()) {
      return "";
    }
    var result = new StringBuilder();
    for (var property : regularProperties()) {
      result.append(property.value());
    }
    textProperty.ifPresent(property -> result.append(property.value()));
    result.append(ANSI_RESET_TAG);
    extraProperty.ifPresent(property -> result.append(property.value()));
    return result.toString();
  }

  private Optional<ChatProperty> findTextProperty() {
    return properties.stream()
      .filter(property -> property instanceof ChatPropertyText)
      .findFirst();
  }

  private Optional<ChatProperty> findExtraProperty() {
    return properties.stream()
      .filter(property -> property instanceof ChatPropertyExtra)
      .findFirst();
  }

  private List<ChatProperty> regularProperties() {
    return properties.stream()
      .filter(property -> !(property instanceof ChatPropertyText))
      .filter(property -> !(property instanceof ChatPropertyExtra))
      .toList();
  }
}
