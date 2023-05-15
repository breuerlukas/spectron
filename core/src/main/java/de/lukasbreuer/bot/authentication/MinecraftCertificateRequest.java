package de.lukasbreuer.bot.authentication;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "create")
public final class MinecraftCertificateRequest implements AuthenticationRequest<PlayerCertificate> {
  private final String accessToken;

  private static final String MINECRAFT_CERTIFICATE_URL =
    "https://api.minecraftservices.com/player/certificates";

  @Override
  public CompletableFuture<PlayerCertificate> send() {
    var request = HttpRequest.newBuilder()
      .uri(URI.create(MINECRAFT_CERTIFICATE_URL))
      .header("Authorization", "Bearer " + accessToken)
      .header("Content-Length", "0")
      .POST(HttpRequest.BodyPublishers.ofString("")).build();
    return HttpClient.newHttpClient()
      .sendAsync(request, HttpResponse.BodyHandlers.ofString())
      .thenApply(HttpResponse::body)
      .thenApply(this::createCertificateResult);
  }

  private PlayerCertificate createCertificateResult(String body) {
    var jsonBody = new JSONObject(body);
    var keyPair = jsonBody.getJSONObject("keyPair");
    var privateKeyContent = keyPair.getString("privateKey")
      .replace("\n", "").replace("\\n", "")
      .replace("-----BEGIN RSA PRIVATE KEY-----", "")
      .replace("-----END RSA PRIVATE KEY-----", "");
    var privateKey = createPrivateKey(privateKeyContent);
    var publicKeyContent = keyPair.getString("publicKey")
      .replace("\n", "").replace("\\n", "")
      .replace("-----BEGIN RSA PUBLIC KEY-----", "")
      .replace("-----END RSA PUBLIC KEY-----", "");
    var publicKey = createPublicKey(publicKeyContent);
    var publicKeySignature = jsonBody.getString("publicKeySignatureV2");
    var expirationTime = extractExpirationTime(jsonBody.getString("expiresAt"));
    return PlayerCertificate.create(privateKey, publicKey, publicKeySignature,
      expirationTime);
  }

  private PrivateKey createPrivateKey(String privateKeyContent) {
    try {
      var keyFactory = KeyFactory.getInstance("RSA");
      var keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
      return keyFactory.generatePrivate(keySpec);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return null;
  }

  private PublicKey createPublicKey(String publicKeyContent) {
    try {
      var keyFactory = KeyFactory.getInstance("RSA");
      var keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
      return keyFactory.generatePublic(keySpec);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return null;
  }

  private long extractExpirationTime(String expirationContent) {
    var temporalAccessor = DateTimeFormatter.ISO_INSTANT.parse(expirationContent);
    var instant = Instant.from(temporalAccessor);
    var expiresAt = Date.from(instant);
    return expiresAt.getTime();
  }
}
