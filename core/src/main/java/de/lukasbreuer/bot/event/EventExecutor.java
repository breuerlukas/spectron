package de.lukasbreuer.bot.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class EventExecutor {
  private final HookRegistry registry;

  public void execute(Event event) {
    try {
      event.call(registry);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }
}
