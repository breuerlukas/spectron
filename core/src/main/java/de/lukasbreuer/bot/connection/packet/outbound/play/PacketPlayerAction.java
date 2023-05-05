package de.lukasbreuer.bot.connection.packet.outbound.play;

import de.lukasbreuer.bot.block.BlockFace;
import de.lukasbreuer.bot.block.BlockPosition;
import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;

public final class PacketPlayerAction extends PacketOutgoing {
  private final int status;
  private final BlockPosition blockPosition;
  private final BlockFace blockFace;

  public PacketPlayerAction(
    int status, BlockPosition blockPosition, BlockFace blockFace
  ) {
    super(0x1D, ProtocolState.PLAY);
    this.status = status;
    this.blockPosition = blockPosition;
    this.blockFace = blockFace;
  }

  public PacketPlayerAction() {
    super(0x1D, ProtocolState.PLAY);
    this.status = -1;
    this.blockPosition = null;
    this.blockFace = null;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeVarInt(status);
    buffer.writeBlockPosition(blockPosition);
    buffer.raw().writeByte(blockFace.value());
    buffer.writeVarInt(0);
  }
}