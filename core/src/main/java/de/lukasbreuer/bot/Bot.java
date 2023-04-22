package de.lukasbreuer.bot;

import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.connection.packet.PacketRegistry;
import de.lukasbreuer.bot.connection.packet.inbound.login.PacketDisconnect;
import de.lukasbreuer.bot.connection.packet.inbound.login.PacketEncryptionRequest;
import de.lukasbreuer.bot.connection.packet.inbound.login.PacketLoginSuccess;
import de.lukasbreuer.bot.connection.packet.inbound.login.PacketSetCompression;
import de.lukasbreuer.bot.connection.packet.outbound.handshake.PacketHandshake;
import de.lukasbreuer.bot.connection.packet.outbound.login.PacketEncryptionResponse;
import de.lukasbreuer.bot.connection.packet.outbound.login.PacketLoginStart;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketChatMessage;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class Bot {
  private final String hostname;
  private final short port;
  private ConnectionClient connection;

  public void connect() throws Exception {
    connection = ConnectionClient.create(registerPackets(), hostname, port);
    connection.connectAsync(() -> {
      connection.sendPacket(new PacketHandshake(762, hostname, port));
      connection.sendPacket(new PacketLoginStart("Lynoo",
        UUID.fromString("0ffbc827-0a24-4890-a3a4-ea7e19dd1a14")));
    });
  }

  private PacketRegistry registerPackets() throws Exception {
    var registry = PacketRegistry.create();
    registry.registerOutgoingPacket(PacketHandshake.class);
    registry.registerOutgoingPacket(PacketLoginStart.class);
    registry.registerOutgoingPacket(PacketEncryptionResponse.class);
    registry.registerOutgoingPacket(PacketChatMessage.class);
    registry.registerIncomingPacket(PacketDisconnect.class);
    registry.registerIncomingPacket(PacketEncryptionRequest.class);
    registry.registerIncomingPacket(PacketSetCompression.class);
    registry.registerIncomingPacket(PacketLoginSuccess.class);
    return registry;
  }
}
