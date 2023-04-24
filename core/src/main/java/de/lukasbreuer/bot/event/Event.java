package de.lukasbreuer.bot.event;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {
  public void call(HookRegistry registry) throws Exception {
    var hookMethods = findEventHookMethods(registry);
    for (var hookMethod : hookMethods.entrySet()) {
      var hook = hookMethod.getKey();
      var method = hookMethod.getValue();
      method.setAccessible(true);
      method.invoke(hook, this);
    }
  }

  private Map<Hook, Method> findEventHookMethods(HookRegistry registry) {
    var hookMethods = Maps.<Hook, Method>newHashMap();
    for (var hook : registry.findAll()) {
      for (var method : hook.getClass().getDeclaredMethods()) {
        if (isCorrespondingMethod(method)) {
          hookMethods.put(hook, method);
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
