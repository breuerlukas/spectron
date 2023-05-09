package de.lukasbreuer.bot.connection.packet.inbound.play;

import de.lukasbreuer.bot.chunk.Chunk;
import de.lukasbreuer.bot.connection.ProtocolState;
import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.inbound.PacketIncoming;
import dev.dewy.nbt.Nbt;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.io.DataInputStream;
import java.io.InputStream;

@Getter
@Accessors(fluent = true)
public final class PacketChunkData extends PacketIncoming {
  private Chunk chunk;

  public PacketChunkData() {
    super(0x24, ProtocolState.PLAY);
  }

  @Override
  public void read(PacketBuffer buffer) throws Exception {
    var chunkX = buffer.raw().readInt();
    var chunkZ = buffer.raw().readInt();
    System.out.println("Chunk: " + (chunkX * 16) + "/" + (chunkZ * 16));
    var stream = new DataInputStream(new InputStream() {
      @Override
      public int read() {
        return buffer.raw().readByte();
      }
    });
    var a = new Nbt().fromStream(stream);
    var dataSize = buffer.readVarInt();
    //System.out.println(dataSize);
    var data = PacketBuffer.create(buffer.raw().readBytes(dataSize));
    var blockCount = data.raw().readShort();
    readChunkSection(buffer);
    //System.out.println(chunkX + ":" + chunkZ);
    //System.out.println(buffer.raw().readableBytes());
    var numberOfBlocks = buffer.readVarInt();
    //System.out.println("Number of blocks: " + numberOfBlocks);
    /*for (var i = 0; i < numberOfBlocks; i++) {
      var xz = buffer.raw().readByte();
      var y = buffer.raw().readShort();
      var type = buffer.readVarInt();
      if (type == 138) {
        System.out.println("Beacon");
      }
    }*/
  }

  private static final int SECTION_WIDTH = 16;
  private static final int SECTION_HEIGHT = 16;

  private void readChunkSection(PacketBuffer buffer) {
    var bitsPerEntry = buffer.raw().readByte();
    int individualValueMask = ((1 << bitsPerEntry) - 1);
    var paletteLength = buffer.readVarInt();
    for (var i = 0; i < paletteLength; i++) {
      buffer.readVarInt();
    }
    var dataArrayLength = buffer.readVarInt();
    var dataArray = new long[dataArrayLength];
    for (int i = 0; i < dataArrayLength; i++) {
      dataArray[i] = buffer.raw().readLong();
    }
    for (int y = 0; y < SECTION_HEIGHT; y++) {
      for (int z = 0; z < SECTION_WIDTH; z++) {
        for (int x = 0; x < SECTION_WIDTH; x++) {
          int blockNumber = (((y * SECTION_HEIGHT) + z) * SECTION_WIDTH) + x;
          int startLong = (blockNumber * bitsPerEntry) / 64;
          int startOffset = (blockNumber * bitsPerEntry) % 64;
          int endLong = ((blockNumber + 1) * bitsPerEntry - 1) / 64;

          int data;
          if (startLong == endLong) {
            data = (int)(dataArray[startLong] >> startOffset);
          } else {
            int endOffset = 64 - startOffset;
            data = (int)(dataArray[startLong] >> startOffset | dataArray[endLong] << endOffset);
          }
          data &= individualValueMask;

          // data should always be valid for the palette
          // If you're reading a power of 2 minus one (15, 31, 63, 127, etc...) that's out of bounds,
          // you're probably reading light data instead

          System.out.println("Got: " + data + " at " + x + "/" + y + "/" + z);
        }
      }
    }
  }
}
