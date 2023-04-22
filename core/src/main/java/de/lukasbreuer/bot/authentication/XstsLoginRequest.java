package de.lukasbreuer.bot.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "create")
public final class XstsLoginRequest implements AuthenticationRequest<XstsLoginRequest.XstsResult> {
  @Getter
  @Accessors(fluent = true)
  class XstsResult {
    private final String accessToken;
    private final String userHash;

    public XstsResult(String accessToken, String userHash) {
      this.accessToken = accessToken;
      this.userHash = userHash;
    }
  }

  private final String accessToken;

  private static final String XSTS_LOGIN_URL =
    "https://xsts.auth.xboxlive.com/xsts/authorize";

  @Override
  public CompletableFuture<XstsResult> send() {
    var request = HttpRequest.newBuilder()
      .uri(URI.create(XSTS_LOGIN_URL))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(createRequestBody())).build();
    return HttpClient.newHttpClient()
      .sendAsync(request, HttpResponse.BodyHandlers.ofString())
      .thenApply(HttpResponse::body)
      .thenApply(this::extractXstsResult);
  }

  private XstsResult extractXstsResult(String body) {
    var jsonBody = new JSONObject(body);
    var token = jsonBody.getString("Token");
    var userHash = jsonBody.getJSONObject("DisplayClaims")
      .getJSONArray("xui").getJSONObject(0).getString("uhs");
    return new XstsResult(token, userHash);
  }

  private String createRequestBody() {
    var body = new JSONObject();
    var properties = new JSONObject();
    properties.put("SandboxId", "RETAIL");
    var userTokens = new JSONArray();
    userTokens.put(accessToken);
    properties.put("UserTokens", userTokens);
    body.put("Properties", properties);
    body.put("RelyingParty", "rp://api.minecraftservices.com/");
    body.put("TokenType", "JWT");
    return body.toString();
  }
}