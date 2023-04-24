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
  private String minecraftAccessToken;
  @Getter
  private PlayerCertificate playerCertificate;

  public CompletableFuture<Boolean> authenticate(String serverHash) {
    var futureResult = new CompletableFuture<Boolean>();
    microsoftLogin().thenAccept(result ->
      minecraftLogin(result.accessToken(), result.userHash()).thenAccept(accessToken ->
        minecraftClientAuthentication(serverHash, accessToken).thenAccept(futureResult::complete)));
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
    var future = new CompletableFuture<String>();
    future.thenAccept(token -> minecraftAccessToken = token);
    MinecraftLoginRequest.create(accessToken, userHash).send()
      .thenAccept(future::complete);
    return future;
  }

  private CompletableFuture<Boolean> minecraftClientAuthentication(
    String serverHash, String accessToken
  ) {
    var futureResult = new CompletableFuture<Boolean>();
    certificatePlayer(accessToken).thenAccept(certificate ->
      joinMinecraftServer(serverHash, accessToken).thenAccept(futureResult::complete));
    return futureResult;
  }

  private CompletableFuture<PlayerCertificate> certificatePlayer(String accessToken) {
    var future = new CompletableFuture<PlayerCertificate>();
    future.thenAccept(certificate -> playerCertificate = certificate);
    MinecraftCertificateRequest.create(accessToken).send()
      .thenAccept(future::complete);
    return future;
  }

  private CompletableFuture<Boolean> joinMinecraftServer(
    String serverHash, String accessToken
  ) {
    return MinecraftJoinServerRequest.create(accessToken,
      playerUuid.toString().replace("-", ""), serverHash).send();
  }
}