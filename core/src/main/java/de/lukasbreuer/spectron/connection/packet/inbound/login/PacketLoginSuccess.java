package de.lukasbreuer.spectron.connection.packet.inbound.login;

import de.lukasbreuer.spectron.connection.ProtocolState;
import de.lukasbreuer.spectron.connection.packet.PacketBuffer;
import de.lukasbreuer.spectron.connection.packet.inbound.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class PacketLoginSuccess extends PacketIncoming {
  @Getter
  @Accessors(fluent = true)
  public class Property {
    private final String name;
    private final String value;
    private final String signature;

    public Property(String name, String value, String signature) {
      this.name = name;
      this.value = value;
      this.signature = signature;
    }
  }

  private UUID uuid;
  private String username;
  private Property[] properties;

  public PacketLoginSuccess() {
    super(0x02, ProtocolState.LOGIN);
  }

  @Override
  public void read(PacketBuffer buffer) {
    uuid = buffer.readUUID();
    username = buffer.readString();
    var propertiesNumber = buffer.readVarInt();
    properties = new Property[propertiesNumber];
    for (var i = 0; i < propertiesNumber; i++) {
      var name = buffer.readString();
      var value = buffer.readString();
      var isSigned = buffer.raw().readBoolean();
      properties[i] = new Property(name, value, isSigned ?
        buffer.readString() : "");
    }
  }
}