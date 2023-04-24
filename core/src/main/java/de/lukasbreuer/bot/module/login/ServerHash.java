package de.lukasbreuer.bot.module.login;

import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Formatter;

@RequiredArgsConstructor(staticName = "create")
public final class ServerHash {
  public static String build(
    String serverId, byte[] sharedSecret, byte[] publicKey
  ) throws Exception {
    return create(serverId, sharedSecret, publicKey).build();
  }

  private final String serverId;
  private final byte[] sharedSecret;
  private final byte[] publicKey;

  public String build() throws Exception {
    var crypt = MessageDigest.getInstance("SHA-1");
    crypt.reset();
    crypt.update(serverId.getBytes(StandardCharsets.US_ASCII));
    crypt.update(sharedSecret);
    crypt.update(publicKey);
    return byteArrayToHex(crypt.digest());
  }

  private String byteArrayToHex(byte[] hash) {
    var formatter = new Formatter();
    for (byte b : hash) {
      formatter.format("%02x", b);
    }
    var result = formatter.toString();
    formatter.close();
    return result;
  }
}
