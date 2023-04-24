package de.lukasbreuer.bot.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class HookRegistry {
  private final List<Hook> hooks = Lists.newArrayList();

  public void register(Hook hook) {
    hooks.add(hook);
  }

  public void unregister(Hook hook) {
    hooks.remove(hook);
  }

  public List<Hook> findAll() {
    return List.copyOf(hooks);
  }
}
