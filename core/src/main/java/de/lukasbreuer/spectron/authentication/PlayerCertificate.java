package de.lukasbreuer.spectron.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public class PlayerCertificate {
  private final PrivateKey privateKey;
  private final PublicKey publicKey;
  private final String publicKeySignature;
  private final long expiration;
}
