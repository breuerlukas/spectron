package de.lukasbreuer.bot;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.lukasbreuer.bot.authentication.Authentication;
import de.lukasbreuer.bot.authentication.Credentials;
import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.connection.packet.PacketRegistry;
import de.lukasbreuer.bot.connection.packet.inbound.login.PacketEncryptionRequest;
import de.lukasbreuer.bot.connection.packet.inbound.login.PacketLoginDisconnect;
import de.lukasbreuer.bot.connection.packet.inbound.login.PacketLoginSuccess;
import de.lukasbreuer.bot.connection.packet.inbound.login.PacketSetCompression;
import de.lukasbreuer.bot.connection.packet.outbound.handshake.PacketHandshake;
import de.lukasbreuer.bot.connection.packet.outbound.login.PacketEncryptionResponse;
import de.lukasbreuer.bot.connection.packet.outbound.login.PacketLoginStart;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketChatMessage;
import de.lukasbreuer.bot.event.EventExecutor;
import de.lukasbreuer.bot.log.Log;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class Bot {
  private final String hostname;
  private final short port;
  private final Credentials credentials;
  private final UUID uuid;
  private Injector injector;
  private Log log;
  private Authentication authentication;
  private ConnectionClient connection;

  public void initialize() throws Exception {
    injector = Guice.createInjector(BotInjectionModule.create());
    log = Log.create("Core", "/logs/core/");
    authentication = Authentication.create(credentials, uuid);
  }

  public void connect() throws Exception {
    connection = ConnectionClient.create(registerPackets(), hostname, port,
      injector.getInstance(EventExecutor.class));
    connection.connectAsync(() -> {

    });
  }

  private PacketRegistry registerPackets() throws Exception {
    var registry = injector.getInstance(PacketRegistry.class);
    registerOutgoingPackets(registry);
    registerIncomingPackets(registry);
    return registry;
  }

  private void registerOutgoingPackets(PacketRegistry registry) throws Exception {
    registry.registerOutgoingPacket(PacketHandshake.class);
    registry.registerOutgoingPacket(PacketLoginStart.class);
    registry.registerOutgoingPacket(PacketEncryptionResponse.class);
    registry.registerOutgoingPacket(PacketChatMessage.class);
  }

  private void registerIncomingPackets(PacketRegistry registry) throws Exception {
    registry.registerIncomingPacket(PacketLoginDisconnect.class);
    registry.registerIncomingPacket(PacketEncryptionRequest.class);
    registry.registerIncomingPacket(PacketSetCompression.class);
    registry.registerIncomingPacket(PacketLoginSuccess.class);
  }
}
