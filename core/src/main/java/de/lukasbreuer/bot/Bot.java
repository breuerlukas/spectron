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
import de.lukasbreuer.bot.connection.packet.inbound.play.PacketDisconnect;
import de.lukasbreuer.bot.connection.packet.inbound.play.PacketKeepAliveRequest;
import de.lukasbreuer.bot.connection.packet.outbound.handshake.PacketHandshake;
import de.lukasbreuer.bot.connection.packet.outbound.login.PacketEncryptionResponse;
import de.lukasbreuer.bot.connection.packet.outbound.login.PacketLoginStart;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketChatCommand;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketClientInformation;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketKeepAliveResponse;
import de.lukasbreuer.bot.connection.packet.outbound.play.PacketPlayerSession;
import de.lukasbreuer.bot.event.EventExecutor;
import de.lukasbreuer.bot.event.HookRegistry;
import de.lukasbreuer.bot.event.module.EnableModuleEvent;
import de.lukasbreuer.bot.event.module.LoadModuleEvent;
import de.lukasbreuer.bot.log.Log;
import de.lukasbreuer.bot.module.Module;
import de.lukasbreuer.bot.module.foundation.FoundationModule;
import de.lukasbreuer.bot.module.login.LoginModule;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class Bot {
  private final String hostname;
  private final short port;
  private final Credentials credentials;
  private final String username;
  private final UUID uuid;
  private Injector injector;
  private Log log;
  private Authentication authentication;
  private EventExecutor eventExecutor;
  private HookRegistry hookRegistry;
  private ConnectionClient client;

  public void initialize() throws Exception {
    injector = Guice.createInjector(BotInjectionModule.create());
    log = Log.create("Core", "/logs/core/");
    authentication = Authentication.create(credentials, uuid);
    eventExecutor = injector.getInstance(EventExecutor.class);
    hookRegistry = injector.getInstance(HookRegistry.class);
  }

  public void connect() throws Exception {
    client = ConnectionClient.create(registerPackets(), hostname, port,
      eventExecutor);
    client.connectAsync(this::runModules);
  }

  private void runModules() {
    runModule(LoginModule.create(client, hostname, port, 762, username, uuid,
      authentication, hookRegistry));
    runModule(FoundationModule.create(client, hookRegistry));
  }

  private void runModule(Module module) {
    module.onLoad();
    eventExecutor.execute(LoadModuleEvent.create(module));
    module.onEnable();
    eventExecutor.execute(EnableModuleEvent.create(module));
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
    registry.registerOutgoingPacket(PacketClientInformation.class);
    registry.registerOutgoingPacket(PacketPlayerSession.class);
    registry.registerOutgoingPacket(PacketKeepAliveResponse.class);
    registry.registerOutgoingPacket(de.lukasbreuer.bot.connection.packet.
      outbound.play.PacketChatMessage.class);
    registry.registerOutgoingPacket(PacketChatCommand.class);
  }

  private void registerIncomingPackets(PacketRegistry registry) throws Exception {
    registry.registerIncomingPacket(PacketLoginDisconnect.class);
    registry.registerIncomingPacket(PacketEncryptionRequest.class);
    registry.registerIncomingPacket(PacketSetCompression.class);
    registry.registerIncomingPacket(PacketLoginSuccess.class);
    registry.registerIncomingPacket(PacketDisconnect.class);
    registry.registerIncomingPacket(PacketKeepAliveRequest.class);
    registry.registerIncomingPacket(de.lukasbreuer.bot.connection.packet.
      inbound.play.PacketChatMessage.class);
  }
}
