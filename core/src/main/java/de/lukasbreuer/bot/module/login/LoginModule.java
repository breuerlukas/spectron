package de.lukasbreuer.bot.module.login;

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
  private final ConnectionClient client;
  private final String hostname;
  private final short port;
  private final int protocolVersion;
  private final String username;
  private final UUID playerUuid;
  private final Authentication authentication;
  private final HookRegistry hookRegistry;

  @Override
  public void onLoad() {

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

  }
}
