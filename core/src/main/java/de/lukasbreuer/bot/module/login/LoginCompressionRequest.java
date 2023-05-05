package de.lukasbreuer.bot.module.login;

import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.connection.packet.compression.CompressionStatus;
import de.lukasbreuer.bot.connection.packet.compression.PacketCompression;
import de.lukasbreuer.bot.connection.packet.compression.PacketDecompression;
import de.lukasbreuer.bot.event.EventHook;
import de.lukasbreuer.bot.event.Hook;
import de.lukasbreuer.bot.event.login.SetCompressionEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class LoginCompressionRequest implements Hook {
  private final ConnectionClient client;

  @EventHook
  private void setCompression(SetCompressionEvent event) {
    var pipeline = client.channel().pipeline();
    pipeline.addBefore("packet-encoder", "packet-compression",
      PacketCompression.create());
    pipeline.addAfter("packet-decoder", "packet-decompression",
      PacketDecompression.create());
    client.compressionStatus(CompressionStatus.ENABLED);
  }
}
