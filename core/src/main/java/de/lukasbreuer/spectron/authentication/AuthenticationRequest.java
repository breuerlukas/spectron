package de.lukasbreuer.spectron.authentication;

import java.util.concurrent.CompletableFuture;

public interface AuthenticationRequest<T> {
  CompletableFuture<T> send();
}
