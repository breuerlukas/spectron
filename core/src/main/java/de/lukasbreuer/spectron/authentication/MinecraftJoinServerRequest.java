package de.lukasbreuer.spectron.authentication;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "create")
public final class MinecraftJoinServerRequest implements AuthenticationRequest<Boolean> {
  private final String accessToken;
  private final String profileUuid;
  private final String serverHash;

  private static final String MINECRAFT_LOGIN_URL =
    "https://sessionserver.mojang.com/session/minecraft/join";

  @Override
  public CompletableFuture<Boolean> send() {
    var request = HttpRequest.newBuilder()
      .uri(URI.create(MINECRAFT_LOGIN_URL))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(createRequestBody())).build();
    return HttpClient.newHttpClient()
      .sendAsync(request, HttpResponse.BodyHandlers.ofString())
      .thenApply(HttpResponse::statusCode)
      .thenApply(code -> code == 200 || code == 204);
  }

  private String createRequestBody() {
    var body = new JSONObject();
    body.put("accessToken", accessToken);
    body.put("selectedProfile", profileUuid);
    body.put("serverId", serverHash);
    return body.toString();
  }
}
