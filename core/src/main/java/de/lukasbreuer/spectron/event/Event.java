package de.lukasbreuer.spectron.event;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {
  public void call(HookRegistry registry) throws Exception {
    var hookMethods = findEventHookMethods(registry);
    for (var hookMethod : orderEventHooksByPriority(hookMethods)) {
      var hook = hookMethod.getKey();
      var method = hookMethod.getValue();
      method.setAccessible(true);
      method.invoke(hook, this);
    }
  }

  private List<Map.Entry<Hook, Method>> orderEventHooksByPriority(
    List<Map.Entry<Hook, Method>> methods
  ) {
    var result = methods.stream().sorted(Comparator.comparingInt(entry ->
        entry.getValue().getAnnotation(EventHook.class).priority().value()))
      .collect(Collectors.toList());
    Collections.reverse(result);
    return result;
  }

  private List<Map.Entry<Hook, Method>> findEventHookMethods(HookRegistry registry) {
    var hookMethods = Lists.<Map.Entry<Hook, Method>>newArrayList();
    for (var hook : registry.findAll()) {
      for (var method : hook.getClass().getDeclaredMethods()) {
        if (isCorrespondingMethod(method)) {
          hookMethods.add(new AbstractMap.SimpleEntry<>(hook, method));
        }
      }
    }
    return hookMethods;
  }

  private boolean isCorrespondingMethod(Method method) {
    if (!method.isAnnotationPresent(EventHook.class)) {
      return false;
    }
    var parameters = method.getParameters();
    if (parameters.length != 1 || parameters[0].getType() != getClass()) {
      return false;
    }
    return true;
  }
}
