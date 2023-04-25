package de.lukasbreuer.bot.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum HookPriority {
  FIRST(2),
  BEFORE(1),
  NEUTRAL(0),
  BEHIND(-1),
  LAST(-2);

  private final int value;
}
