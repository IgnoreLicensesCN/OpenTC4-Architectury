package thaumcraft.common.lib.network.misc;

@Deprecated(forRemoval = true,since = "we use /fillbiome impl so that this is not needed")
public class PacketBiomeChange {
//   private int x;
//   private int z;
//   private short biome;
//
//   public PacketBiomeChange() {
//   }
//
//   public PacketBiomeChange(int x, int z, short biome) {
//      this.x = x;
//      this.z = z;
//      this.biome = biome;
//   }
//
//   public void toBytes(ByteBuf buffer) {
//      buffer.writeInt(this.x);
//      buffer.writeInt(this.z);
//      buffer.writeShort(this.biome);
//   }
//
//   public void fromBytes(ByteBuf buffer) {
//      this.x = buffer.readInt();
//      this.z = buffer.readInt();
//      this.biome = buffer.readShort();
//   }
//
//   public IMessage onMessage(PacketBiomeChange message, MessageContext ctx) {
//      Utils.setBiomeAt(Thaumcraft.getClientWorld(), message.x, message.z, BiomeGenBase.getBiome(message.biome));
//      return null;
//   }
}
