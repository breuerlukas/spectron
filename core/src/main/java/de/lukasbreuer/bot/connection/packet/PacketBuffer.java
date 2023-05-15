package de.lukasbreuer.bot.connection.packet;

import de.lukasbreuer.bot.block.BlockPosition;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.BitSet;
import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class PacketBuffer {
  private final ByteBuf buffer;

  public void writeVarInt(int value) {
    int part;
    do {
      part = value & 0x7F;
      value >>>= 7;
      if (value != 0) {
        part |= 0x80;
      }
      buffer.writeByte(part);
    } while (value != 0);
  }

  public void writeString(String value) {
    var bytes = value.getBytes(StandardCharsets.UTF_8);
    writeVarInt(bytes.length);
    buffer.writeBytes(bytes);
  }

  public void writeStringWithoutHeader(String value) {
    var bytes = value.getBytes(StandardCharsets.UTF_8);
    buffer.writeBytes(bytes);
  }

  public void writeVarShort(short value) {
    var low = value & 0x7FFF;
    var high = (value & 0x7F8000) >> 15;
    if (high != 0) {
      low = low | 0x8000;
    }
    buffer.writeShort(low);
    if (high != 0) {
      buffer.writeByte(high);
    }
  }

  public void writeUUID(UUID uuid) {
    buffer.writeLong(uuid.getMostSignificantBits());
    buffer.writeLong(uuid.getLeastSignificantBits());
  }

  public void writeBlockPosition(BlockPosition position) {
    buffer.writeLong(((position.x() & 0x3FFFFFF) << 38) |
      ((position.z() & 0x3FFFFFF) << 12) | (position.y() & 0xFFF));
  }

  public void writeBitSet(BitSet bits, int size) {
    buffer.writeBytes(Arrays.copyOf(bits.toByteArray(), (size + 8) >> 3));
  }

  public int readVarInt() {
    var out = 0;
    var bytes = 0;
    byte in;
    do {
      in = buffer.readByte();
      out |= (in & 0x7F) << (bytes++ * 7);
    } while ((in & 0x80) == 0x80);
    return out;
  }

  public String readString() {
    var length = readVarInt();
    var bytes = new byte[length];
    buffer.readBytes(bytes);
    return new String(bytes);
  }

  public long readVarLong() {
    long value = 0;
    int position = 0;
    byte currentByte;
    while (true) {
      currentByte = buffer.readByte();
      value |= (long) (currentByte & 0x7F) << position;
      if ((currentByte & 0x80) == 0) break;
      position += 7;
    }
    return value;
  }

  public UUID readUUID() {
    var mostSignificantBits = buffer.readLong();
    var leastSignificantBits = buffer.readLong();
    return new UUID(mostSignificantBits, leastSignificantBits);
  }

  public BlockPosition readBlockPosition() {
    var value = buffer.readLong();
    return BlockPosition.create(value >> 38, value << 52 >> 52, value << 26 >> 38);
  }

  public ByteBuf raw() {
    return buffer;
  }

  public byte[] bytes() {
    var temp = new byte[buffer.readableBytes()];
    buffer.readBytes(temp);
    return temp;
  }
}
