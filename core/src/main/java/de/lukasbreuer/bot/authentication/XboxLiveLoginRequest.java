package de.lukasbreuer.bot.authentication;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "create")
public final class XboxLiveLoginRequest implements AuthenticationRequest<String> {
  private final String accessToken;

  private static final String XBOX_LIVE_LOGIN_URL =
    "https://user.auth.xboxlive.com/user/authenticate";

  @Override
  public CompletableFuture<String> send() {
    var request = HttpRequest.newBuilder()
      .uri(URI.create(XBOX_LIVE_LOGIN_URL))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(createRequestBody())).build();
    return HttpClient.newHttpClient()
      .sendAsync(request, HttpResponse.BodyHandlers.ofString())
      .thenApply(HttpResponse::body)
      .thenApply(this::extractToken);
  }

  private String extractToken(String body) {
    var jsonBody = new JSONObject(body);
    return jsonBody.getString("Token");
  }

  private String createRequestBody() {
    var body = new JSONObject();
    var properties = new JSONObject();
    properties.put("AuthMethod", "RPS");
    properties.put("SiteName", "user.auth.xboxlive.com");
    properties.put("RpsTicket", accessToken);
    body.put("Properties", properties);
    body.put("RelyingParty", "http://auth.xboxlive.com");
    body.put("TokenType", "JWT");
    return body.toString();
  }
}
