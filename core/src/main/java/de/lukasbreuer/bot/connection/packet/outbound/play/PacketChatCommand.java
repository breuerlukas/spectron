package de.lukasbreuer.bot.connection.packet.outbound.play;

import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.BitSet;

public final class PacketChatCommand extends PacketOutgoing {
  @Getter
  @Accessors(fluent = true)
  public class Argument {
    private final String argument;
    private final byte[] signature;

    public Argument(String argument, byte[] signature) {
      this.argument = argument;
      this.signature = signature;
    }
  }

  private final String command;
  private final long timestamp;
  private final long salt;
  private final Argument[] arguments;

  public PacketChatCommand(
    String command, long timestamp, long salt, Argument[] arguments
  ) {
    super(0x04, ProtocolState.PLAY);
    this.command = command;
    this.timestamp = timestamp;
    this.salt = salt;
    this.arguments = arguments;
  }

  public PacketChatCommand() {
    super(0x04, ProtocolState.PLAY);
    this.command = "";
    this.timestamp = -1;
    this.salt = -1;
    this.arguments = null;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeString(command);
    buffer.raw().writeLong(timestamp);
    buffer.raw().writeLong(salt);
    buffer.writeVarInt(arguments.length);
    for (var argument : arguments) {
      buffer.writeString(argument.argument());
      buffer.raw().writeBytes(argument.signature());
    }
    buffer.writeVarInt(0);
    buffer.writeBitSet(new BitSet(), 20);
  }
}