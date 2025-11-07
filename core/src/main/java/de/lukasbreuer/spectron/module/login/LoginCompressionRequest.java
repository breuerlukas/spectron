package de.lukasbreuer.spectron.module.login;

import de.lukasbreuer.spectron.connection.ConnectionClient;
import de.lukasbreuer.spectron.connection.packet.compression.CompressionStatus;
import de.lukasbreuer.spectron.connection.packet.compression.PacketCompression;
import de.lukasbreuer.spectron.connection.packet.compression.PacketDecompression;
import de.lukasbreuer.spectron.event.EventHook;
import de.lukasbreuer.spectron.event.Hook;
import de.lukasbreuer.spectron.event.login.SetCompressionEvent;
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
