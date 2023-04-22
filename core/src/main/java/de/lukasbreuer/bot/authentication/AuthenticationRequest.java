package de.lukasbreuer.bot.authentication;

import java.util.concurrent.CompletableFuture;

public interface AuthenticationRequest<T> {
  CompletableFuture<T> send();
}
