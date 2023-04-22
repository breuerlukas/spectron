package de.lukasbreuer.bot.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class Authentication {
  private final Credentials credentials;
  private final UUID playerUuid;
  private final String serverHash;
  @Getter
  private String accessToken;

  public CompletableFuture<Boolean> authenticate() {
    var futureResult = new CompletableFuture<Boolean>();
    microsoftLogin().thenAccept(result ->
      minecraftLogin(result.accessToken(), result.userHash()).thenAccept(accessToken ->
        joinMinecraftServer(accessToken).thenAccept(futureResult::complete)));
    return futureResult;
  }

  private CompletableFuture<XstsLoginRequest.XstsResult> microsoftLogin() {
    var futureResult = new CompletableFuture<XstsLoginRequest.XstsResult>();
    MicrosoftLoginRequest.create(credentials.email(), credentials.password())
      .send().thenAccept(microsoftLoginToken -> XboxLiveLoginRequest.create(microsoftLoginToken)
        .send().thenAccept(xboxLiveLoginToken -> XstsLoginRequest.create(xboxLiveLoginToken)
          .send().thenAccept(futureResult::complete)));
    return futureResult;
  }

  private CompletableFuture<String> minecraftLogin(
    String accessToken, String userHash
  ) {
    return MinecraftLoginRequest.create(accessToken, userHash).send();
  }

  private CompletableFuture<Boolean> joinMinecraftServer(String accessToken) {
    this.accessToken = accessToken;
    return MinecraftJoinServerRequest.create(accessToken,
      playerUuid.toString().replace("-", ""), serverHash).send();
  }
}