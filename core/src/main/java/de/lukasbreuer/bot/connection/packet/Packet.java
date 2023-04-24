package de.lukasbreuer.bot.connection.packet;

import de.lukasbreuer.bot.connection.ProtocolState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Packet {
  private final int id;
  private final ProtocolState protocolState;
}
