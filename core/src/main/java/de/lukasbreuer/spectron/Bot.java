package de.lukasbreuer.spectron;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import de.lukasbreuer.spectron.authentication.Authentication;
import de.lukasbreuer.spectron.connection.ConnectionClient;
import de.lukasbreuer.spectron.connection.packet.PacketRegistry;
import de.lukasbreuer.spectron.connection.packet.inbound.login.PacketEncryptionRequest;
import de.lukasbreuer.spectron.connection.packet.inbound.login.PacketLoginDisconnect;
import de.lukasbreuer.spectron.connection.packet.inbound.login.PacketLoginSuccess;
import de.lukasbreuer.spectron.connection.packet.inbound.login.PacketSetCompression;
import de.lukasbreuer.spectron.connection.packet.inbound.play.*;
import de.lukasbreuer.spectron.connection.packet.outbound.handshake.PacketHandshake;
import de.lukasbreuer.spectron.connection.packet.outbound.login.PacketEncryptionResponse;
import de.lukasbreuer.spectron.connection.packet.outbound.login.PacketLoginStart;
import de.lukasbreuer.spectron.connection.packet.outbound.play.*;
import de.lukasbreuer.spectron.credentials.Credentials;
import de.lukasbreuer.spectron.event.EventExecutor;
import de.lukasbreuer.spectron.event.module.EnableModuleEvent;
import de.lukasbreuer.spectron.event.module.LoadModuleEvent;
import de.lukasbreuer.spectron.module.Module;
import de.lukasbreuer.spectron.module.command.CommandModule;
import de.lukasbreuer.spectron.module.foundation.FoundationModule;
import de.lukasbreuer.spectron.module.login.LoginModule;
import de.lukasbreuer.spectron.player.PlayerLocation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class Bot {
  private Injector injector;
  private EventExecutor eventExecutor;
  private Authentication authentication;
  private ConnectionClient client;
  private final PlayerLocation playerLocation = PlayerLocation.create(0, 0, 0, 0, 0);

  public void initialize() {
    injector = Guice.createInjector(BotInjectionModule.create());
    eventExecutor = injector.getInstance(EventExecutor.class);
    authentication = Authentication.create(injector.getInstance(Credentials.class));
  }

  public void connect() throws Exception {
    client = ConnectionClient.create(registerPackets(),
      findNamedInstance(String.class, "hostname"),
      findNamedInstance(Short.class, "port"), eventExecutor);
    client.connectAsync(this::runModules);
  }

  private void runModules() {
    runModule(CommandModule.create(injector, client, authentication));
    runModule(LoginModule.create(injector, client, authentication));
    runModule(FoundationModule.create(injector, client, playerLocation));
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
    registry.registerOutgoingPacket(PacketChatMessage.class);
    registry.registerOutgoingPacket(PacketChatCommand.class);
    registry.registerOutgoingPacket(PacketConfirmTeleportation.class);
    registry.registerOutgoingPacket(PacketSetPlayerPosition.class);
    registry.registerOutgoingPacket(PacketPlayerAction.class);
    registry.registerOutgoingPacket(PacketSwingArm.class);
    registry.registerOutgoingPacket(PacketClickContainer.class);
    registry.registerOutgoingPacket(PacketUseItem.class);
  }

  private void registerIncomingPackets(PacketRegistry registry) throws Exception {
    registry.registerIncomingPacket(PacketLoginDisconnect.class);
    registry.registerIncomingPacket(PacketEncryptionRequest.class);
    registry.registerIncomingPacket(PacketSetCompression.class);
    registry.registerIncomingPacket(PacketLoginSuccess.class);
    registry.registerIncomingPacket(PacketDisconnect.class);
    registry.registerIncomingPacket(PacketKeepAliveRequest.class);
    //registry.registerIncomingPacket(PacketChunkData.class);
    registry.registerIncomingPacket(PacketPlayerChatMessage.class);
    registry.registerIncomingPacket(PacketSystemChatMessage.class);
    registry.registerIncomingPacket(PacketSynchronizePlayerPosition.class);
    registry.registerIncomingPacket(PacketSpawnPlayer.class);
    registry.registerIncomingPacket(PacketRemoveEntities.class);
    registry.registerIncomingPacket(PacketSetContainerContent.class);
    registry.registerIncomingPacket(PacketSetContainerSlot.class);
    registry.registerIncomingPacket(PacketBlockUpdate.class);
  }

  private <T> T findNamedInstance(Class<? extends T> type, String name) {
    return injector.getInstance(Key.get(type, Names.named(name)));
  }
}
