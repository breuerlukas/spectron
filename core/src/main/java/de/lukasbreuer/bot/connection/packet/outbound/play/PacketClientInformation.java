package de.lukasbreuer.bot.connection.packet.outbound.play;

import de.lukasbreuer.bot.connection.packet.PacketBuffer;
import de.lukasbreuer.bot.connection.packet.outbound.PacketOutgoing;

public final class PacketClientInformation extends PacketOutgoing {
  private final String locale;
  private final int viewDistance;
  private final int chatMode;
  private final boolean chatColors;
  private final byte skinParts;
  private final int mainHand;
  private final boolean textFiltering;
  private final boolean serverListing;

  public PacketClientInformation(
    String locale, int viewDistance, int chatMode, boolean chatColors,
    byte skinParts, int mainHand, boolean textFiltering, boolean serverListing
  ) {
    super(0x08);
    this.locale = locale;
    this.viewDistance = viewDistance;
    this.chatMode = chatMode;
    this.chatColors = chatColors;
    this.skinParts = skinParts;
    this.mainHand = mainHand;
    this.textFiltering = textFiltering;
    this.serverListing = serverListing;
  }

  public PacketClientInformation() {
    super(0x08);
    this.locale = "";
    this.viewDistance = -1;
    this.chatMode = -1;
    this.chatColors = false;
    this.skinParts = -1;
    this.mainHand = -1;
    this.textFiltering = false;
    this.serverListing = false;
  }

  @Override
  public void write(PacketBuffer buffer) {
    buffer.writeString(locale);
    buffer.raw().writeByte(viewDistance);
    buffer.writeVarInt(chatMode);
    buffer.raw().writeBoolean(chatColors);
    buffer.raw().writeByte(skinParts);
    buffer.writeVarInt(mainHand);
    buffer.raw().writeBoolean(textFiltering);
    buffer.raw().writeBoolean(serverListing);
  }
}
