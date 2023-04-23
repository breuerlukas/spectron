package de.lukasbreuer.bot.connection.packet.cryptography;

import javax.crypto.Cipher;

public enum CipherMode {
  ENCRYPTION,
  DECRYPTION;

  public int identifier() {
    return isEncryption() ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
  }

  public boolean isEncryption() {
    return this == ENCRYPTION;
  }

  public boolean isDecryption() {
    return this == DECRYPTION;
  }
}
