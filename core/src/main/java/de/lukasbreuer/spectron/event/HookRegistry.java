package de.lukasbreuer.spectron.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;

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

  public void unregister(Class<? extends Hook> hookClass) {
    findByClass(hookClass).ifPresent(this::unregister);
  }

  public Optional<Hook> findByClass(Class<? extends Hook> hookClass) {
    return hooks.stream()
      .filter(hook -> hook.getClass().equals(hookClass))
      .findFirst();
  }

  public List<Hook> findAll() {
    return List.copyOf(hooks);
  }
}
