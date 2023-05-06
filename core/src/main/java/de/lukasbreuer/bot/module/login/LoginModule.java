package de.lukasbreuer.bot.module.login;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import de.lukasbreuer.bot.authentication.Authentication;
import de.lukasbreuer.bot.connection.ConnectionClient;
import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.outbound.handshake.PacketHandshake;
import de.lukasbreuer.bot.connection.packet.outbound.login.PacketLoginStart;
import de.lukasbreuer.bot.event.HookRegistry;
import de.lukasbreuer.bot.module.Module;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class LoginModule extends Module {
  private final Injector injector;
  private final ConnectionClient client;
  private final Authentication authentication;
  @Inject
  private HookRegistry hookRegistry;
  @Inject
  @Named("hostname")
  private String hostname;
  @Inject
  @Named("port")
  private short port;
  @Inject
  @Named("protocolVersion")
  private int protocolVersion;
  @Inject
  @Named("username")
  private String username;
  @Inject
  @Named("uuid")
  private UUID playerUuid;

  @Override
  public void onLoad() {
    injector.injectMembers(this);
  }

  @Override
  public void onEnable() {
    registerHooks();
    requestLogin();
  }

  private void registerHooks() {
    hookRegistry.register(LoginFailure.create());
    hookRegistry.register(LoginEncryptionRequest.create(client, authentication));
    hookRegistry.register(LoginCompressionRequest.create(client));
    hookRegistry.register(LoginSuccess.create(client, authentication));
  }

  private void requestLogin() {
    client.sendPacket(new PacketHandshake(protocolVersion, hostname, port));
    client.protocolState(ProtocolState.LOGIN);
    client.sendPacket(new PacketLoginStart(username, playerUuid));
  }

  @Override
  public void onDisable() {
    hookRegistry.unregister(LoginFailure.class);
    hookRegistry.unregister(LoginEncryptionRequest.class);
    hookRegistry.unregister(LoginCompressionRequest.class);
    hookRegistry.unregister(LoginSuccess.class);
  }
}
