package de.lukasbreuer.spectron.authentication;


import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "create")
public final class MinecraftLoginRequest implements AuthenticationRequest<String> {
  private final String accessToken;
  private final String userHash;

  private static final String MINECRAFT_LOGIN_URL =
    "https://api.minecraftservices.com/authentication/login_with_xbox";

  @Override
  public CompletableFuture<String> send() {
    var request = HttpRequest.newBuilder()
      .uri(URI.create(MINECRAFT_LOGIN_URL))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(createRequestBody())).build();
    return HttpClient.newHttpClient()
      .sendAsync(request, HttpResponse.BodyHandlers.ofString())
      .thenApply(HttpResponse::body)
      .thenApply(this::extractToken);
  }

  private String extractToken(String body) {
    var jsonBody = new JSONObject(body);
    return jsonBody.getString("access_token");
  }

  private static final String IDENTITY_TOKEN_FORMAT = "XBL3.0 x=%s;%s";

  private String createRequestBody() {
    var body = new JSONObject();
    body.put("identityToken", String.format(IDENTITY_TOKEN_FORMAT,
      userHash, accessToken));
    body.put("ensureLegacyEnabled", true);
    return body.toString();
  }
}