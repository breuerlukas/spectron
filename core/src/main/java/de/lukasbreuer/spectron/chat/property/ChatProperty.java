package de.lukasbreuer.spectron.chat.property;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ChatProperty {
  public abstract String value();
}
