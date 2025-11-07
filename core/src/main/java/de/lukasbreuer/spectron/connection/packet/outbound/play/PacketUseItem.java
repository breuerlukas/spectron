package de.lukasbreuer.spectron.connection.packet.outbound.play;

import de.lukasbreuer.spectron.block.BlockFace;
import de.lukasbreuer.spectron.block.BlockPosition;
import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.outbound.PacketOutgoing;

public final class PacketUseItem extends PacketOutgoing {
  private final BlockPosition blockPosition;
  private final BlockFace blockFace;
  private final float cursorX;
  private final float cursorY;
  private final float cursorZ;

  public PacketUseItem(
    BlockPosition blockPosition, BlockFace blockFace, float cursorX,
    float cursorY, float cursorZ
  ) {
    super(0x31, ProtocolState.PLAY);
    this.blockPosition = blockPosition;
    this.blockFace = blockFace;
    this.cursorX = cursorX;
    this.cursorY = cursorY;
    this.cursorZ = cursorZ;
  }

  public PacketUseItem() {
    super(0x31, ProtocolState.PLAY);
    this.blockPosition = null;
    this.blockFace = null;
    this.cursorX = -1;
    this.cursorY = -1;
    this.cursorZ = -1;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeVarInt(0);
    buffer.writeBlockPosition(blockPosition);
    buffer.writeVarInt(blockFace.value());
    buffer.raw().writeFloat(cursorX);
    buffer.raw().writeFloat(cursorY);
    buffer.raw().writeFloat(cursorZ);
    buffer.raw().writeBoolean(false);
    buffer.writeVarInt(0);
  }
}
